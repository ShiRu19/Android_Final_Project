package t4.sers.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.tapadoo.alerter.Alerter;

import org.joda.time.DateTime;

import t4.sers.R;

public class WarningLiftActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_lift);
        sharedPreferences = getSharedPreferences("warningLiftTime", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    public void click(View view){
        CheckBox checkBox = findViewById(R.id.warning_lift_checkBox);
        if(checkBox.isChecked()){
            DateTime dateTime = DateTime.now();
            sharedPreferencesEditor.putLong("liftTime", dateTime.getMillis());
            sharedPreferencesEditor.commit();
            Toast.makeText(this, "完成解除警示狀態", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Alerter.create(WarningLiftActivity.this)
                    .setBackgroundColorRes(R.color.green_500)
                    .setTitle("你必須要勾選同意衛教資訊！")
                    .show();
        }
    }
}