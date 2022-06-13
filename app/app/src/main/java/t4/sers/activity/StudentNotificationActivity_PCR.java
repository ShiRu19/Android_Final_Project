package t4.sers.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import t4.sers.R;

public class StudentNotificationActivity_PCR extends AppCompatActivity {

    private LinearLayout LinearLayout_pcr_positive;
    private LinearLayout LinearLayout_insulation_yes;
    private RadioGroup RG_pcr;

    private EditText EditText_positive_date;
    private ImageButton ImgBtn_pcr_date;

    private Button Btn_certification;
    private ImageButton ImgBtn_pcr_certification_delete;
    private ImageView ImgView_pcr_certification;

    private RadioGroup RG_isolation;
    private RadioButton RB_insulation_yes;
    private EditText EditText_isolation_date_start;
    private EditText EditText_isolation_date_end;

    private Button Btn_next;

    private boolean isPositive;
    private boolean isInsulation;
    private boolean startDateFillIn;
    private boolean endDateFillIn;
    private boolean isDataCorrect_positive_date;
    private boolean isDataCorrect_isolation_date_start;
    private boolean isDataCorrect_isolation_date_end;
    private boolean isDataCorrect_pcr_certification;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "t4.sers.activity.positivesharedprefs";
    private static final String ET_positive_date = "EditText pcr positive date";
    private static final String ET_isolation_date_start = "EditText isolation start date";
    private static final String ET_isolation_date_end = "EditText isolation end date";
    private static final String IV_pcr_certification = "ImageView pcr certification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notification_pcr);
        isPositive = false;
        isInsulation = true;
        startDateFillIn = false;
        endDateFillIn = false;
        isDataCorrect_positive_date = false;
        isDataCorrect_isolation_date_start = false;
        isDataCorrect_isolation_date_end = false;
        isDataCorrect_pcr_certification = false;

        LinearLayout_pcr_positive = findViewById(R.id.LinearLayout_pcr_positive);
        LinearLayout_insulation_yes = findViewById(R.id.LinearLayout_insulation_yes);

        RG_pcr = findViewById(R.id.RadioGroup_pcr);

        EditText_positive_date = findViewById(R.id.EditText_PCR_date);
        ImgBtn_pcr_date = findViewById(R.id.ImageButton_PCR_date);

        Btn_certification = findViewById(R.id.Button_PCR_certification);
        ImgBtn_pcr_certification_delete = findViewById(R.id.Button_PCR_certification_delete);
        ImgView_pcr_certification = findViewById(R.id.ImageView_PCR_certification);

        RG_isolation = findViewById(R.id.RadioGroup_insulation);
        RB_insulation_yes = findViewById(R.id.Radio_insulation_yes);
        EditText_isolation_date_start = findViewById(R.id.EditText_insulation_startDate);
        EditText_isolation_date_end = findViewById(R.id.EditText_insulation_endDate);

        Btn_next = findViewById(R.id.Button_next_rapid);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        RG_pcr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkID) {
                if(checkID == R.id.Radio_pcr_positive) {
                    isPositive = true;
                    LinearLayout_pcr_positive.setVisibility(View.VISIBLE);
                    nextStatus();
                }
                else {
                    isPositive = false;
                    LinearLayout_pcr_positive.setVisibility(View.GONE);
                    nextStatus();
                }
            }
        });

        RG_isolation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkID) {
                if(checkID == R.id.Radio_insulation_yes) {
                    isInsulation = true;
                    LinearLayout_insulation_yes.setVisibility(View.VISIBLE);
                    nextStatus();
                }
                else {
                    isInsulation = false;
                    LinearLayout_insulation_yes.setVisibility(View.GONE);
                    nextStatus();
                }
            }
        });

        EditText_positive_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker_PCR_date(view);
            }
        });

        ImgBtn_pcr_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker_PCR_date(view);
            }
        });

        Btn_certification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDataCorrect_pcr_certification = uploadImage(view);
                nextStatus();
            }
        });

        ImgBtn_pcr_certification_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage(view);
                isDataCorrect_pcr_certification = false;
                nextStatus();
            }
        });

        EditText_isolation_date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker_isolation_date_start(view);
            }
        });

        EditText_isolation_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker_isolation_date_end(view);
            }
        });

        EditText_positive_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if(s.length() != 0) isDataCorrect_positive_date = true;
                else isDataCorrect_positive_date = false;
                nextStatus();
            }
        });

        EditText_isolation_date_start.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if(s.length() != 0) isDataCorrect_isolation_date_start = true;
                else isDataCorrect_isolation_date_start = false;
                nextStatus();
            }
        });

        EditText_isolation_date_end.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if(s.length() != 0) isDataCorrect_isolation_date_end = true;
                else isDataCorrect_isolation_date_end = false;
                nextStatus();
            }
        });

        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Intent intent = new Intent(StudentNotificationActivity_PCR.this, StudentNotificationActivity_course.class);
                startActivity(intent);
            }
        });
    }

    private void nextStatus() {
        boolean isOpenNext = false;

        if(isPositive && isInsulation) {
            if(isDataCorrect_positive_date && isDataCorrect_pcr_certification && isDataCorrect_isolation_date_start && isDataCorrect_isolation_date_end) {
                isOpenNext = true;
            }
            else {
                isOpenNext = false;
            }
        }
        else if(isPositive && !isInsulation) {
            if(isDataCorrect_positive_date && isDataCorrect_pcr_certification) {
                isOpenNext = true;
            }
            else{
                isOpenNext = false;
            }
        }
        else if(!isPositive) {
            isOpenNext = true;
        }

        if (isOpenNext) Btn_next.setEnabled(true);
        else Btn_next.setEnabled(false);
    }

    //
    // 選取日期 (單日)
    //
    private void showDatePicker_PCR_date(View view) {
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


                if(date_string.length() == 0) {
                    // 輸入不合法
                    EditText_positive_date.setHintTextColor(getResources().getColor(R.color.red_500));
                    EditText_positive_date.setText(null);   // 清空
                }
                else{
                    // 輸入合法
                    EditText_positive_date.setTextColor(getResources().getColor(R.color.black));
                    EditText_positive_date.setText(date_string);   // 填入結果
                }
            }
        }, year, month, day).show();
    }

    //
    // 選取日期 (起始日)
    //
    private void showDatePicker_isolation_date_start(View view) {
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

                if(date_string.length() == 0) {
                    // 輸入不合法
                    EditText_isolation_date_start.setHintTextColor(getResources().getColor(R.color.red_500)); // 紅字表示
                    EditText_isolation_date_start.setText(null);   // 清空
                    startDateFillIn = false;
                }
                else{
                    // 輸入合法，檢查是否在結束日之前
                    boolean startDateLegal = false;

                    if(endDateFillIn) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_end = null;
                        String date_end_string = EditText_isolation_date_end.getText().toString();
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
                        EditText_isolation_date_start.setTextColor(getResources().getColor(R.color.black)); // 黑字表示
                        EditText_isolation_date_start.setText(date_string);   // 填入結果
                        startDateFillIn = true;
                    }
                    else{
                        Toast.makeText(StudentNotificationActivity_PCR.this, "起始日應該在結束日之前", Toast.LENGTH_SHORT).show();
                        EditText_isolation_date_start.setHintTextColor(getResources().getColor(R.color.red_500)); // 黑字表示
                        EditText_isolation_date_start.setText(null);   // 清空
                        startDateFillIn = false;
                    }
                }
            }
        }, year, month, day).show();
    }

    //
    // 選取日期 (結束日)
    //
    private void showDatePicker_isolation_date_end(View view) {
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

                String date_start_string = EditText_isolation_date_start.getText().toString();
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
                    EditText_isolation_date_end.setTextColor(getResources().getColor(R.color.black));
                    EditText_isolation_date_end.setText(date_string);   // 填入結果
                    endDateFillIn = true;
                }
                else {
                    // 輸入不合法，清空結束日期
                    EditText_isolation_date_end.setText(date_string);
                    EditText_isolation_date_end.setHintTextColor(getResources().getColor(R.color.red_500));
                    endDateFillIn = false;
                }
            }
        }, year, month, day).show();
    }

    //
    // 照片顯示
    //
    private boolean uploadImage(@NonNull View view) {
        ImgView_pcr_certification.setImageDrawable(getResources().getDrawable(R.drawable.ntut));
        ImgView_pcr_certification.setVisibility(view.VISIBLE);

        ImgBtn_pcr_certification_delete.setVisibility(view.VISIBLE);
        return true;
    }

    //
    // 照片清除
    //
    private void deleteImage(View view) {
        ImgBtn_pcr_certification_delete.setVisibility(view.INVISIBLE);
        ImgView_pcr_certification.setVisibility(view.INVISIBLE);
        ImgView_pcr_certification.setImageDrawable(null);
    }

    //
    // 儲存資料
    //
    private void saveData() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(ET_positive_date, EditText_positive_date.getText().toString());
        editor.putString(ET_isolation_date_start, EditText_isolation_date_start.getText().toString());
        editor.putString(ET_isolation_date_end, EditText_isolation_date_end.getText().toString());
        editor.apply();
    }
}