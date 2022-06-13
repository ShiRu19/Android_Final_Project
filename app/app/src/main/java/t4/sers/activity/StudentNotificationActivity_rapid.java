package t4.sers.activity;

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
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import t4.sers.R;

public class StudentNotificationActivity_rapid extends AppCompatActivity {

    private EditText EditText_rapid_date;
    private ImageButton ImgBtn_rapid_date;

    private Button Btn_certification;
    private ImageButton ImgBtn_rapid_certification_delete;
    private ImageView ImgView_rapid_certification;

    private Button Btn_next;

    private boolean dateCompleted = false;
    private boolean imageCompleted = false;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "t4.sers.activity.positivesharedprefs";
    private static final String ET_rapid_date = "EditText rapid test positive date";
    private static final String IV_rapid_certification = "ImageView rapid certification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notification_rapid);
        EditText_rapid_date = findViewById(R.id.EditText_rapidAntigenTest_date);
        ImgBtn_rapid_date = findViewById(R.id.ImageButton_rapidAntigenTest_date);
        Btn_certification = findViewById(R.id.Button_rapidAntigenTest_certification);
        ImgBtn_rapid_certification_delete = findViewById(R.id.Button_rapidAntigenTest_certification_delete);
        ImgView_rapid_certification = findViewById(R.id.ImageView_rapidAntigenTest_certification);
        Btn_next = findViewById(R.id.Button_next_rapid);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        EditText_rapid_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0 || editable == getResources().getText(R.string.notification_dateHint)) {
                    dateCompleted = false;
                }
                else if(EditText_rapid_date.getText().length() == 0) {
                    dateCompleted = false;
                }
                else {
                    dateCompleted = true;
                }
                checkAllDataCompleted();
            }
        });

        EditText_rapid_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });

        ImgBtn_rapid_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });

        Btn_certification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadImage(view)) {
                    imageCompleted = true;
                }
                else{
                    imageCompleted = false;
                }
                checkAllDataCompleted();
            }
        });

        ImgBtn_rapid_certification_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage(view);
                imageCompleted = false;
                checkAllDataCompleted();
            }
        });

        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Intent intent = new Intent(StudentNotificationActivity_rapid.this, StudentNotificationActivity_PCR.class);
                startActivity(intent);
            }
        });
    }

    private void checkAllDataCompleted() {
        if(dateCompleted && imageCompleted) {
            setBtnEnabled(true);
        }
        else{
            setBtnEnabled(false);
        }
    }

    private void setBtnEnabled(boolean enabled) {
        Btn_next.setEnabled(enabled);
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
                    EditText_rapid_date.setTextColor(getResources().getColor(R.color.red_500));
                    EditText_rapid_date.setText(date_string);   // 填入結果
                }
                else{
                    // 輸入合法
                    EditText_rapid_date.setTextColor(getResources().getColor(R.color.black));
                    EditText_rapid_date.setText(date_string);   // 填入結果
                }
            }
        }, year, month, day).show();
    }


    //
    // 照片顯示
    //
    public boolean uploadImage(View view) {
        ImgView_rapid_certification.setImageDrawable(getResources().getDrawable(R.drawable.ntut));
        ImgView_rapid_certification.setVisibility(view.VISIBLE);

        ImgBtn_rapid_certification_delete.setVisibility(view.VISIBLE);
        return true;
    }

    //
    // 照片清除
    //
    public void deleteImage(View view) {
        ImgBtn_rapid_certification_delete.setVisibility(view.INVISIBLE);
        ImgView_rapid_certification.setVisibility(view.INVISIBLE);
        ImgView_rapid_certification.setImageDrawable(null);
    }

    //
    // 儲存資料
    //
    private void saveData() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(ET_rapid_date, EditText_rapid_date.getText().toString());
        editor.apply();
    }
}