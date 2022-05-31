package t4.sers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tapadoo.alerter.Alerter;

import t4.sers.R;
import t4.sers.fragment.Debug;
import t4.sers.fragment.PersonalFragment;
import t4.sers.fragment.SettingFragment;

public class LobbyActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().show();

        Intent intent = getIntent();

        String studentName = intent.getStringExtra("studentName");
        String studentCourse = intent.getStringExtra("studentCourse");
        String studentPhoto = intent.getStringExtra("imageURI");
        String studentEmail = intent.getStringExtra("studentEmail");

        Alerter.create(LobbyActivity.this)
                .setBackgroundColorRes(R.color.green_500)
                .setTitle("登入成功！")
                .setText("Hello, " + intent.getStringExtra("studentName"))
                .show();

        getSupportActionBar().setTitle("個人防疫資訊");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, PersonalFragment.newInstance(studentCourse, "")).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
            fragmentManager = fragmentManager.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
            if(item.getItemId() == R.id.person) {
                getSupportActionBar().setTitle("個人防疫資訊");
                fragmentManager.replace(R.id.fragmentContainerView, PersonalFragment.newInstance(studentCourse, "")).commit();
                return true;
            }
            if (item.getItemId() == R.id.setting) {
                SettingFragment settingFragment = SettingFragment.newInstance(studentName, studentPhoto, studentEmail);
                getSupportActionBar().setTitle("設定");
                fragmentManager.replace(R.id.fragmentContainerView, settingFragment).commit();
                return true;
            }
            if (item.getItemId() == R.id.debug) {
                Debug debugFragment = Debug.newInstance("bla", "use");
                getSupportActionBar().setTitle("測試用");
                fragmentManager.replace(R.id.fragmentContainerView, debugFragment).commit();
                return true;
            }
            return false;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lobby_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_notification) {
            Intent intent = new Intent(this, StudentNotificationActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
