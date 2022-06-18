package t4.sers.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

import t4.sers.R;

public class AddFootprintsActivity extends AppCompatActivity {

    private EditText footprints_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfootprints);
        footprints_date = findViewById(R.id.pick_date);
    }
    public void showDatePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);      //取得現在的日期年月日
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(view.getContext(), (view1, year1, month1, day1) -> {
            month1 = month1 + 1;

            DatePickerVerification dpv = new DatePickerVerification(year1, month1, day1);
            Date date_picker = dpv.processDatePickerResult(AddFootprintsActivity.this);
            dpv.dateVerification(AddFootprintsActivity.this, date_picker);
            String date_string = dpv.getDateString(0);

            if(date_string == getResources().getString(R.string.notification_dateHint)) {
                // 輸入不合法
                footprints_date.setTextColor(getResources().getColor(R.color.red_500));
                footprints_date.setText(date_string);   // 填入結果
            }
            else{
                // 輸入合法
                footprints_date.setTextColor(getResources().getColor(R.color.black));
                footprints_date.setText(date_string);   // 填入結果
            }
        }, year, month, day).show();
    }

}
