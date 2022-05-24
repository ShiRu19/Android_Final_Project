package t4.sers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tapadoo.alerter.Alerter;

import t4.sers.R;
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
        String studentPhoto = intent.getStringExtra("imageURI");

        Alerter.create(LobbyActivity.this)
                .setBackgroundColorRes(R.color.green_500)
                .setTitle("登入成功！")
                .setText("Hello, " + intent.getStringExtra("studentName"))
                .show();

        getSupportActionBar().setTitle("個人");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new PersonalFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
            fragmentManager = fragmentManager.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
            if(item.getItemId() == R.id.person) {
                getSupportActionBar().setTitle("個人");
                fragmentManager.replace(R.id.fragmentContainerView, new PersonalFragment()).commit();
                return true;
            }
            if (item.getItemId() == R.id.setting) {
                SettingFragment settingFragment = SettingFragment.newInstance(studentName, studentPhoto);
                getSupportActionBar().setTitle("設定");
                fragmentManager.replace(R.id.fragmentContainerView, settingFragment).commit();
                return true;
            }
            return false;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu);
        return true;
    }

}
