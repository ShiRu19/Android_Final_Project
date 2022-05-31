package t4.sers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


import t4.sers.R;
import t4.sers.fragment.Debug;
import t4.sers.fragment.PersonalFragment;
import t4.sers.fragment.SettingFragment;

public class StudentNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notification);
    }

    public void uploadImage(View view) {
        ImageView image = findViewById(R.id.ImageView_rapidAntigenTest_certification);
        image.setImageDrawable(getResources().getDrawable(R.drawable.ntut));
        image.setVisibility(view.VISIBLE);

        ImageButton deleteButton = findViewById(R.id.Button_rapidAntigenTest_certification_delete);
        deleteButton.setVisibility(view.VISIBLE);
    }
}