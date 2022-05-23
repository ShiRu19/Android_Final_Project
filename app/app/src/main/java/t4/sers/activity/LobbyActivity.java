package t4.sers.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tapadoo.alerter.Alerter;

import java.io.FileInputStream;

import t4.sers.R;

public class LobbyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        Alerter.create(LobbyActivity.this)
                .setBackgroundColorRes(R.color.green_500)
                .setTitle("登入成功！")
                .setText("Hello, " + intent.getStringExtra("studentName"))
                .show();

    }

}
