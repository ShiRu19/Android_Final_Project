package t4.sers.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import t4.sers.R;

public class StudentNotificationActivity_PCR extends AppCompatActivity {

    private RadioButton RB_pcr_positive;
    private boolean isPositive;
    private boolean startDateFillIn;
    private boolean endDateFillIn;
    private EditText PCRPositive_date;
    private EditText isolation_date_start;
    private EditText isolation_date_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notification_pcr);
        RB_pcr_positive = findViewById(R.id.radio_pcr_positive);
        isPositive = false;
        startDateFillIn = false;
        endDateFillIn = false;
        PCRPositive_date = findViewById(R.id.EditText_PCR_date);
        isolation_date_start = findViewById(R.id.EditText_insulation_startDate);
        isolation_date_end = findViewById(R.id.EditText_insulation_endDate);
    }

    //
    // 選取日期
    //
    public void showDatePicker_PCR_date(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);      //取得現在的日期年月日
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                DatePickerVerification dpv = new DatePickerVerification(year, month, day);

                Date date_picker = dpv.processDatePickerResult(StudentNotificationActivity_PCR.this);
                dpv.dateVerification(StudentNotificationActivity_PCR.this, date_picker);
                String date_string = dpv.getDateString(0);


                if(date_string == getResources().getString(R.string.notification_dateHint)) {
                    // 輸入不合法
                    PCRPositive_date.setTextColor(getResources().getColor(R.color.red_500));
                    PCRPositive_date.setText(date_string);   // 填入結果
                }
                else{
                    // 輸入合法
                    PCRPositive_date.setTextColor(getResources().getColor(R.color.black));
                    PCRPositive_date.setText(date_string);   // 填入結果
                }
            }
        }, year, month, day).show();
    }

    //
    // 選取日期
    //
    public void showDatePicker_isolation_date_start(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);      //取得現在的日期年月日
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                startDateFillIn = false;

                DatePickerVerification dpv = new DatePickerVerification(year, month, day);
                Date date_picker = dpv.processDatePickerResult(StudentNotificationActivity_PCR.this);
                dpv.dateVerification(StudentNotificationActivity_PCR.this, date_picker);
                String date_string = dpv.getDateString(0);

                if(date_string == getResources().getString(R.string.notification_dateHint)) {
                    // 輸入不合法
                    isolation_date_start.setTextColor(getResources().getColor(R.color.red_500)); // 紅字表示
                    isolation_date_start.setText(date_string);   // 填入結果
                    startDateFillIn = false;
                }
                else{
                    // 輸入合法，檢查是否在結束日之前
                    boolean startDateLegal = false;

                    if(endDateFillIn) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_end = null;
                        String date_end_string = isolation_date_end.getText().toString();
                        int year_start = Integer.parseInt(date_end_string.substring(0, 4));
                        int month_start = Integer.parseInt(date_end_string.substring(5, 7));
                        int day_start = Integer.parseInt(date_end_string.substring(8, 10));
                        try {
                            date_end = sdf.parse(year_start + "-" + month_start + "-" + day_start);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        // 輸入合法，再檢查是否大於開始時間
                        startDateLegal = dpv.dateVerification_twoDate(StudentNotificationActivity_PCR.this, date_picker, date_end);
                        date_string = dpv.getDateString(0);
                    }
                    else{
                        startDateLegal = true;
                    }

                    if(startDateLegal){
                        isolation_date_start.setTextColor(getResources().getColor(R.color.black)); // 黑字表示
                        isolation_date_start.setText(date_string);   // 填入結果
                        startDateFillIn = true;
                    }
                    else{
                        Toast.makeText(StudentNotificationActivity_PCR.this, "起始日應該在結束日之前", Toast.LENGTH_SHORT).show();
                        isolation_date_start.setTextColor(getResources().getColor(R.color.red_500)); // 黑字表示
                        isolation_date_start.setText(getResources().getString(R.string.notification_dateHint));   // 填入結果
                        startDateFillIn = false;
                    }
                }
            }
        }, year, month, day).show();
    }

    //
    // 選取日期
    //
    public void showDatePicker_isolation_date_end(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);      //取得現在的日期年月日
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                endDateFillIn = false;
                boolean endDateLegal = false;

                DatePickerVerification dpv = new DatePickerVerification(year, month, day);
                Date date_picker = dpv.processDatePickerResult(StudentNotificationActivity_PCR.this);
                String date_string;

                String date_start_string = isolation_date_start.getText().toString();
                if(!startDateFillIn) {
                    endDateLegal = true;
                    String month_string = month + "";
                    String day_string = day + "";
                    String year_string = year + "";
                    if(month < 10) {
                        month_string = "0" + month_string;
                    }
                    if(day < 10) {
                        day_string = "0" + day_string;
                    }
                    date_string = year_string + "/" + month_string + "/" + day_string;
                }
                else{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date_start = null;
                    int year_start = Integer.parseInt(date_start_string.substring(0, 4));
                    int month_start = Integer.parseInt(date_start_string.substring(5, 7));
                    int day_start = Integer.parseInt(date_start_string.substring(8, 10));
                    try {
                        date_start = sdf.parse(year_start + "-" + month_start + "-" + day_start);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // 輸入合法，再檢查是否大於開始時間
                    endDateLegal = dpv.dateVerification_twoDate(StudentNotificationActivity_PCR.this, date_start, date_picker);
                    date_string = dpv.getDateString(1);
                }

                if(endDateLegal) {
                    // 輸入合法，填寫成功
                    isolation_date_end.setTextColor(getResources().getColor(R.color.black));
                    isolation_date_end.setText(date_string);   // 填入結果
                    endDateFillIn = true;
                }
                else {
                    // 輸入不合法，清空結束日期
                    isolation_date_end.setText(date_string);
                    isolation_date_end.setTextColor(getResources().getColor(R.color.red_500));
                    endDateFillIn = false;
                }
            }
        }, year, month, day).show();
    }

    //
    // 照片顯示
    //
    public void uploadImage(@NonNull View view) {
        ImageView image = findViewById(R.id.ImageView_PCR_certification);
        image.setImageDrawable(getResources().getDrawable(R.drawable.ntut));
        image.setVisibility(view.VISIBLE);

        ImageButton deleteButton = findViewById(R.id.Button_PCR_certification_delete);
        deleteButton.setVisibility(view.VISIBLE);
    }
}