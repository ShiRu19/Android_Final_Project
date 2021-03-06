package t4.sers.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tapadoo.alerter.Alerter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import t4.sers.R;

class AuthFailedException extends Exception{
    public AuthFailedException(String msg){
        super(msg);
    }
}

public class MainActivity extends AppCompatActivity {

    ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    SharedPreferences loginPreference;
    SharedPreferences.Editor loginPreferenceEditor;

    private static Context context;

    public static Context getContext(){
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_hello);

        context = getContext();

        loginPreference = getSharedPreferences("loginPref", MODE_PRIVATE);
        loginPreferenceEditor = loginPreference.edit();

        new Handler().postDelayed(() -> {

            getSupportActionBar().show();

            setContentView(R.layout.activity_login);

            if(loginPreference.getBoolean("rememberMe", false)){
                String username = loginPreference.getString("username", "");
                String password = loginPreference.getString("password", "");
                ((EditText) findViewById(R.id.editTextAccount)).setText(username);
                ((EditText) findViewById(R.id.editTextTextPassword)).setText(password);
                ((CheckBox) findViewById(R.id.login_rememberme)).setChecked(true);
            }

            Alerter.create(MainActivity.this)
                    .setTitle("???????????????")
                    .setBackgroundColorRes(R.color.orange_500)
                    .setText("??? app ??????????????????????????????????????????")
                    .show();
        }, 2000);

    }

    public void loginOnClick(View view){
        String studentID = ((EditText) findViewById(R.id.editTextAccount)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();
        boolean rememberMe = ((CheckBox) findViewById(R.id.login_rememberme)).isChecked();
        Alerter.create(MainActivity.this)
                .setTitle("?????????")
                .setText("?????????...")
                .enableInfiniteDuration(true)
                .enableProgress(true)
                .disableOutsideTouch()
                .show();
        Runnable runnable = () -> {
            try{
                String loginPostURL = getString(R.string.login_post_URL);
                String photoPostURL = getString(R.string.photo_post_URL);
                Connection.Response response = Jsoup.connect(String.format(loginPostURL, studentID, password, 110, 2))
                        .ignoreContentType(true)
                        .method(Connection.Method.POST)
                        .execute();
                if(response.statusCode() == 200) {
                    JsonElement element = JsonParser.parseString(response.parse().body().text());
                    JsonObject jsonObject = element.getAsJsonObject();
                    String status = jsonObject.get("status").getAsString();
                    if(status.equalsIgnoreCase("ok")){

                        Intent intent = new Intent(MainActivity.this, LobbyActivity.class);
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        String name = data.get("studentName").getAsString();
                        String email = data.get("studentEmail").getAsString();
                        String role = data.get("studentRole").getAsString();
                        String userPhoto = data.get("userPhoto").getAsString();
                        String firebaseToken = data.get("firebaseToken").getAsString();
                        JsonObject studentCourseObject = data.get("studentCourse").getAsJsonObject();
                        JsonArray studentCourseArray = studentCourseObject.get("data").getAsJsonArray();
                        String studentDepartment = studentCourseObject.get("studentDepartment").getAsString();

                        intent.putExtra("firebaseToken", firebaseToken);
                        intent.putExtra("studentName", name);
                        intent.putExtra("studentEmail", email);
                        intent.putExtra("studentRole", role);
                        intent.putExtra("studentCourse", studentCourseArray.toString());

                        if(!userPhoto.equals("")) {
                            response = Jsoup.connect(String.format(photoPostURL, studentID, password, userPhoto))
                                    .ignoreContentType(true)
                                    .method(Connection.Method.POST)
                                    .execute();

                            byte[] blob = response.bodyAsBytes();

                            File temp = new File(getCacheDir(), "tempBMP.jpg");
                            FileOutputStream outStream = new FileOutputStream(temp);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outStream);
                            outStream.close();
                            intent.putExtra("imageURI", temp.getAbsolutePath());
                            loginPreferenceEditor = loginPreferenceEditor.putString("imageURI", temp.getAbsolutePath());
                        }else{
                            intent.putExtra("imageURI", "");
                        }

                        loginPreferenceEditor = loginPreferenceEditor.putBoolean("rememberMe", rememberMe);
                        loginPreferenceEditor = loginPreferenceEditor.putString("studentName", name);
                        loginPreferenceEditor = loginPreferenceEditor.putString("studentEmail", email);
                        loginPreferenceEditor = loginPreferenceEditor.putString("username", studentID);
                        loginPreferenceEditor = loginPreferenceEditor.putString("password", password);
                        loginPreferenceEditor = loginPreferenceEditor.putString("department", studentDepartment);
                        loginPreferenceEditor = loginPreferenceEditor.putString("courseDataJson", studentCourseArray.toString());
                        loginPreferenceEditor.commit();

                        Alerter.create(MainActivity.this)
                                .setTitle("???????????? Firebase...")
                                .setText("?????????...")
                                .enableProgress(true)
                                .disableOutsideTouch()
                                .show();

                        FirebaseApp.initializeApp(this);
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signInWithCustomToken(firebaseToken).addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                startActivity(intent);
                                finish();
                            }else{
                                Log.w("Firebase", "signInWithCustomToken:failure", task.getException());
                                Alerter.create(MainActivity.this)
                                        .setBackgroundColorRes(R.color.red_500)
                                        .setTitle("Firebase ???????????????")
                                        .setText("???????????????????????????")
                                        .show();
                            }
                        });

                    }
                } else {
                    throw new AuthFailedException("????????????");
                }
            } catch (IOException e){
                e.printStackTrace();
                Alerter.create(MainActivity.this)
                        .setBackgroundColorRes(R.color.red_500)
                        .setTitle("???????????????")
                        .setText(Objects.requireNonNull(e.getMessage()))
                        .show();
            } catch (AuthFailedException e){
                Alerter.create(MainActivity.this)
                        .setBackgroundColorRes(R.color.red_500)
                        .setTitle("???????????????")
                        .setText(Objects.requireNonNull(e.getMessage()))
                        .show();
            }
        };
        mExecutor.execute(runnable);
    }
}