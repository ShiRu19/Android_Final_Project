package t4.sers.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import t4.sers.R;

public class AddFootprintsActivity extends AppCompatActivity {

    private EditText footprints_date,footprints_time1,footprints_time2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfootprints);
        footprints_date = findViewById(R.id.pick_date);
        footprints_time1 = findViewById(R.id.pick_time1);
        footprints_time2 = findViewById(R.id.pick_time2);
    }
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
                Date date_picker = dpv.processDatePickerResult(AddFootprintsActivity.this);
                dpv.dateVerification(AddFootprintsActivity.this, date_picker);
                String date_string = dpv.getDateString(0);

                if(date_string == getResources().getString(R.string.addfootprints_dateHint)) {
                    // 輸入不合法
                    footprints_date.setTextColor(getResources().getColor(R.color.red_500));
                    footprints_date.setText(date_string);   // 填入結果
                }
                else{
                    // 輸入合法
                    footprints_date.setTextColor(getResources().getColor(R.color.black));
                    footprints_date.setText(date_string);   // 填入結果
                }
            }
        }, year, month, day).show();
    }
    int h1,h2,m1,m2;
    public void showTimePicker_start(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);      //取得現在的小時分鐘
        int min = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(AddFootprintsActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                h1=hourOfDay;
                m1=minute;
                String mTime1 = String.valueOf(hourOfDay) + " : " + String.valueOf(minute);
                footprints_time1.setText(mTime1);// 填入結果
            }

        }, hour, min, true).show();
    }
    public void showTimePicker_end(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);      //取得現在的小時分鐘
        int min = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(AddFootprintsActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                h2=hourOfDay;
                m2=minute;
                String mTime2 = String.valueOf(hourOfDay) + " : " + String.valueOf(minute);
                if(h1<h2){
                    footprints_time2.setText(mTime2);// 填入結果
                }
                else if(h1==h2 & m1<m2){
                    footprints_time2.setText(mTime2);// 填入結果
                }
                else{
                    hint();
                    mTime2 = "    "+" : ";
                    footprints_time2.setText(mTime2);
                }
            }
        }, hour, min, true).show();
    }

    public void hint(){
        Toast toast = Toast.makeText(this,"請選擇正確的時間！",Toast.LENGTH_LONG);
        toast.show();
    }

}
