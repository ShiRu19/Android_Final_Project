package t4.sers.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import t4.sers.R;

public class StudentNotificationActivity_rapid extends AppCompatActivity {

    private EditText rapidAntigenTest_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notification_rapid);
        rapidAntigenTest_date = findViewById(R.id.EditText_rapidAntigenTest_date);
    }

    //
    // 選取日期
    //
    public void showDatePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);      //取得現在的日期年月日
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                DatePickerVerification dpv = new DatePickerVerification(year, month, day);
                Date date_picker = dpv.processDatePickerResult(StudentNotificationActivity_rapid.this);
                dpv.dateVerification(StudentNotificationActivity_rapid.this, date_picker);
                String date_string = dpv.getDateString(0);

                if(date_string == getResources().getString(R.string.notification_dateHint)) {
                    // 輸入不合法
                    rapidAntigenTest_date.setTextColor(getResources().getColor(R.color.red_500));
                    rapidAntigenTest_date.setText(date_string);   // 填入結果
                }
                else{
                    // 輸入合法
                    rapidAntigenTest_date.setTextColor(getResources().getColor(R.color.black));
                    rapidAntigenTest_date.setText(date_string);   // 填入結果
                }
            }
        }, year, month, day).show();
    }


    //
    // 照片顯示
    //
    public void uploadImage(View view) {
        ImageView image = findViewById(R.id.ImageView_rapidAntigenTest_certification);
        image.setImageDrawable(getResources().getDrawable(R.drawable.ntut));
        image.setVisibility(view.VISIBLE);

        ImageButton deleteButton = findViewById(R.id.Button_rapidAntigenTest_certification_delete);
        deleteButton.setVisibility(view.VISIBLE);
    }
}