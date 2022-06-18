package t4.sers.activity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tapadoo.alerter.Alerter;

import t4.sers.R;
import t4.sers.fragment.CourseFragment;
import t4.sers.fragment.Debug;
import t4.sers.fragment.PersonalFragment;
import t4.sers.fragment.SchoolFragment;
import t4.sers.fragment.SettingFragment;
import t4.sers.receiver.WarningReceiver;

public class LobbyActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    String studentName;
    String studentCourse;
    String studentPhoto;
    String studentEmail;

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "社交距離警示";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().show();

        Intent intent = getIntent();

        studentName = intent.getStringExtra("studentName");
        studentCourse = intent.getStringExtra("studentCourse");
        studentPhoto = intent.getStringExtra("imageURI");
        studentEmail = intent.getStringExtra("studentEmail");

        Alerter.create(LobbyActivity.this)
                .setBackgroundColorRes(R.color.green_500)
                .setTitle("登入成功！")
                .setText("Hello, " + intent.getStringExtra("studentName"))
                .show();

        /* 一個 Firebase 登入的範例 */
        /* TODO: 寫一個 Util 來方便使用寫入、讀取資料的功能 */
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("app").document("version");
        documentReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                Log.d("Firestore", "App version = " + documentSnapshot.getData());
            }else{
                Log.d("Firestore", "Cached get failed: ", task.getException());
            }
        });

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
                Debug debug = Debug.newInstance();
                getSupportActionBar().setTitle("測試");
                fragmentManager.replace(R.id.fragmentContainerView, debug).commit();
                return true;
            }
            if (item.getItemId() == R.id.school) {
                SchoolFragment schoolFragment = SchoolFragment.newInstance("bla", "use");
                getSupportActionBar().setTitle("全校防疫資訊");
                fragmentManager.replace(R.id.fragmentContainerView, schoolFragment).commit();
                return true;
            }
            if (item.getItemId() == R.id.course) {
                CourseFragment courseFragment = CourseFragment.newInstance("bla", "use");
                getSupportActionBar().setTitle("課程防疫資訊");
                fragmentManager.replace(R.id.fragmentContainerView, courseFragment).commit();
                return true;
            }
            return false;
        });

        createNotificationChannel();

        Intent notifyIntent = new Intent(this, WarningReceiver.class);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, repeatInterval, notifyPendingIntent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lobby_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_notification_rapid) {
            Intent intent = new Intent(this, StudentNotificationActivity_rapid.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_notification_PCR) {
            Intent intent = new Intent(this, StudentNotificationActivity_PCR.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_notification_course) {
            Intent intent = new Intent(this, StudentNotificationActivity_course.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_other_footprints) {
            Intent intent = new Intent(this, OtherFootprintsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_report){
            Intent reportIntent = new Intent(this, ReportActivity.class);
            startActivity(reportIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "社交距離通知", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("提供即時的暴露資訊");
            mNotificationManager.createNotificationChannel(notificationChannel);

        }
    }
}
