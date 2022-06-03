package t4.sers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> 4e60930e0b6d037b11debab1f36a10f4fb607360
=======
>>>>>>> 4e60930e0b6d037b11debab1f36a10f4fb607360
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import t4.sers.R;
import t4.sers.fragment.DatePickerFragment;

public class StudentNotificationActivity extends AppCompatActivity {

    private EditText rapidAntigenTest_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notification);
        rapidAntigenTest_date = findViewById(R.id.EditText_rapidAntigenTest_date);
    }

    //
    // 選取日期
    //
    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void processDatePickerResult(int year, int month, int day) {
        // *********************************
        // * 確認選取日期是否介於一個月內 (31天)
        // *********************************

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 選取日期
        Date date_picker = null;
        try {
            date_picker = sdf.parse(year + "-" + month + "-" + day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 現在日期
        Calendar calendar_now = Calendar.getInstance();
        int year_now = calendar_now.get(Calendar.YEAR);
        int month_now = calendar_now.get(Calendar.MONTH) + 1;
        int day_now = calendar_now.get(Calendar.DAY_OF_MONTH);

        Date date_now = null;
        try {
            date_now = sdf.parse(year_now + "-" + month_now + "-" + day_now);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 31天前日期
        Date date = new Date();
        Calendar calendar_limit = Calendar.getInstance();
        calendar_limit.setTime(date);
        calendar_limit.set(Calendar.DATE, calendar_limit.get(Calendar.DATE) - 31);
        int year_limit = calendar_limit.get(Calendar.YEAR);
        int month_limit = calendar_limit.get(Calendar.MONTH) + 1;
        int day_limit = calendar_limit.get(Calendar.DAY_OF_MONTH);

        Date date_limit = null;
        try {
            date_limit = sdf.parse(year_limit + "-" + month_limit + "-" + day_limit);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 驗證
        boolean isDateLegal = dateVerification(date_picker, date_now, date_limit);

        // 通過驗證，顯示在 EditText
        if(isDateLegal) {
            String month_string = Integer.toString(month);
            String day_string = Integer.toString(day);
            String year_string = Integer.toString(year);

            if(month < 10) {
                month_string = "0" + month_string;
            }
            if(day < 10) {
                day_string = "0" + day_string;
            }

            String date_string = year_string + "/" + month_string + "/" + day_string;
            rapidAntigenTest_date.setText(date_string);
        }
    }

    //
    // 日期驗證
    // 輸入範圍為通報日前一個月內
    //
    public boolean dateVerification(Date date_picker, Date date_now, Date date_limit){
        // 驗證選取日期是否在區間內
        if(date_picker.compareTo(date_now) > 0) {
            // 選取日期在範圍區間之後
            Toast.makeText(this, "選取日期不可大於現在", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(date_picker.compareTo(date_limit) < 0) {
            // 選取日期在範圍區間之前
            Toast.makeText(this, "選取日期不可小於前一個月", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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