package t4.sers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tapadoo.alerter.Alerter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import t4.sers.R;

public class MainActivity extends AppCompatActivity {

    Handler mHandler = new Handler(Looper.getMainLooper());
    ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_hello);

        new Handler().postDelayed(() -> {
            setContentView(R.layout.activity_login);
            Alerter.create(MainActivity.this)
                    .setTitle("請先登入！")
                    .setBackgroundColorRes(R.color.orange_500)
                    .setText("此 app 使用「北科校園入口網站」登入")
                    .show();
        }, 2000);

    }

    public void loginOnClick(View view){
        String studentID = ((EditText) findViewById(R.id.editTextAccount)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();
        Alerter.create(MainActivity.this)
                .setTitle("登入中")
                .setText("請稍後...")
                .show();
        Runnable runnable = () -> {
            try{
                String postURL = getString(R.string.login_post_URL);
                Connection.Response response = Jsoup.connect(String.format(postURL, studentID, password))
                        .ignoreContentType(true)
                        .method(Connection.Method.POST)
                        .execute();
                if(response.statusCode() == 200) {
                    JsonElement element = JsonParser.parseString(response.parse().body().text());
                    JsonObject jsonObject = element.getAsJsonObject();
                    String status = jsonObject.get("status").getAsString();
                    if(status.equalsIgnoreCase("ok")){
                        Alerter.create(MainActivity.this)
                                .setBackgroundColorRes(R.color.green_500)
                                .setTitle("登入成功！")
                                .show();
                        Intent intent = new Intent(MainActivity.this, LobbyActivity.class);
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        String name = data.get("studentName").getAsString();
                        String email = data.get("studentEmail").getAsString();
                        String role = data.get("studentRole").getAsString();
                        intent.putExtra("studentName", name);
                        intent.putExtra("studentEmail", email);
                        intent.putExtra("studentRole", role);
                        startActivity(intent);
                        finish();
                    }else{
                        Alerter.create(MainActivity.this)
                                .setBackgroundColorRes(R.color.red_500)
                                .setTitle("登入失敗！")
                                .show();
                    }
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        };
        mExecutor.execute(runnable);
    }

    private void wentLobby(){
        Intent intent = new Intent(MainActivity.this, LobbyActivity.class);
        startActivity(intent);
        finish();
    }
}