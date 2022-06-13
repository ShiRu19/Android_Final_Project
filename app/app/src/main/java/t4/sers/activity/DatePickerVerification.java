package t4.sers.activity;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import t4.sers.R;

public class DatePickerVerification extends AppCompatActivity {

    public int year;
    public int month;
    public int day;
    public String date_string;
    public String date_string_end;

    public DatePickerVerification(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        date_string = "";
        date_string_end = "";
    }

    public Date processDatePickerResult(Context context) {
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

        return date_picker;
    }

    public String getDateString(int index) {
        if(index == 0) {
            return date_string;
        }
        else if(index == 1) {
            return date_string_end;
        }
        else {
            return "";
        }
    }

    //
    // 日期驗證
    // 輸入範圍為通報日前一個月內
    //
    public void dateVerification(Context context, @NonNull Date date_picker){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

        // 驗證選取日期是否在區間內
        if(date_picker.compareTo(date_now) > 0) {
            // 選取日期在範圍區間之後
            Toast.makeText(context, "選取日期不可大於現在", Toast.LENGTH_LONG).show();
            date_string = "";
            return;
        }
        else if(date_picker.compareTo(date_limit) < 0) {
            // 選取日期在範圍區間之前
            Toast.makeText(context, "選取日期不可小於前一個月", Toast.LENGTH_LONG).show();
            date_string = "";
            return;
        }
        else {
            String month_string = Integer.toString(month);
            String day_string = Integer.toString(day);
            String year_string = Integer.toString(year);
            if(month < 10) {
                month_string = "0" + month_string;
            }
            if(day < 10) {
                day_string = "0" + day_string;
            }
            date_string = year_string + "/" + month_string + "/" + day_string;
        }
    }

    private String setDateString(Date date) {
        // 選取日期在範圍區間之後
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String month_string = Integer.toString(month);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);

        if(month < 10) {
            month_string = "0" + month_string;
        }
        if(day < 10) {
            day_string = "0" + day_string;
        }

        return year_string + "/" + month_string + "/" + day_string;
    }

    public boolean dateVerification_twoDate(Context context, Date date_start, @NonNull Date date_end) {
        // 驗證選取日期是否在區間內
        if(date_end.compareTo(date_start) > 0) {
            date_string = setDateString(date_start);
            date_string_end = setDateString(date_end);
            return true;
        }
        else {
            //date_string = context.getResources().getString(R.string.notification_dateHint);
            //date_string_end = context.getResources().getString(R.string.notification_dateHint);
            date_string = "";
            date_string_end = "";
            Toast.makeText(context, "請選取正確隔離日期", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
