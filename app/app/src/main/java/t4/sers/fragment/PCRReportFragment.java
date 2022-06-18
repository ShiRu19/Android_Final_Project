package t4.sers.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import t4.sers.R;
import t4.sers.activity.DatePickerVerification;

public class PCRReportFragment extends Fragment {

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
    private static final String is_PCR_positive = "Is PCR positive";
    private static final String is_PCR_isolation = "Is PCR isolation";

    public PCRReportFragment() {
        // Required empty public constructor
    }

    public static PCRReportFragment newInstance() {
        return new PCRReportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report_pcr, container, false);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                if(data != null && data.getData() != null){
                    Uri selectedImageUri = data.getData();
                    Bitmap selectedImageBitmap = null;
                    try {
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isDataCorrect_pcr_certification = true;
                    ImgView_pcr_certification.setImageBitmap(selectedImageBitmap);
                    ImgView_pcr_certification.setTag(selectedImageUri.toString());
                    ImgView_pcr_certification.setVisibility(View.VISIBLE);
                    saveData();
                }
            }
        });

        isPositive = false;
        isInsulation = true;
        startDateFillIn = false;
        endDateFillIn = false;
        isDataCorrect_positive_date = false;
        isDataCorrect_isolation_date_start = false;
        isDataCorrect_isolation_date_end = false;
        isDataCorrect_pcr_certification = false;

        LinearLayout_pcr_positive = view.findViewById(R.id.LinearLayout_pcr_positive);
        LinearLayout_insulation_yes = view.findViewById(R.id.LinearLayout_insulation_yes);

        RG_pcr = view.findViewById(R.id.RadioGroup_pcr);

        EditText_positive_date = view.findViewById(R.id.EditText_PCR_date);
        ImgBtn_pcr_date = view.findViewById(R.id.ImageButton_PCR_date);

        Btn_certification = view.findViewById(R.id.Button_PCR_certification);
        ImgBtn_pcr_certification_delete = view.findViewById(R.id.Button_PCR_certification_delete);
        ImgView_pcr_certification = view.findViewById(R.id.ImageView_PCR_certification);

        RG_isolation = view.findViewById(R.id.RadioGroup_insulation);
        RB_insulation_yes = view.findViewById(R.id.Radio_insulation_yes);
        EditText_isolation_date_start = view.findViewById(R.id.EditText_insulation_startDate);
        EditText_isolation_date_end = view.findViewById(R.id.EditText_insulation_endDate);

        Btn_next = view.findViewById(R.id.Button_next_rapid);

        if(getActivity() != null) {
            mPreferences = getActivity().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        }

        RG_pcr.setOnCheckedChangeListener((radioGroup, checkID) -> {
            if(checkID == R.id.Radio_pcr_positive) {
                isPositive = true;
                LinearLayout_pcr_positive.setVisibility(View.VISIBLE);
            }
            else {
                isPositive = false;
                LinearLayout_pcr_positive.setVisibility(View.GONE);
            }
            nextStatus();
        });

        RG_isolation.setOnCheckedChangeListener((radioGroup, checkID) -> {
            if(checkID == R.id.Radio_insulation_yes) {
                isInsulation = true;
                LinearLayout_insulation_yes.setVisibility(View.VISIBLE);
            }
            else {
                isInsulation = false;
                LinearLayout_insulation_yes.setVisibility(View.GONE);
            }
            nextStatus();
        });

        EditText_positive_date.setOnClickListener(this::showDatePicker_PCR_date);

        ImgBtn_pcr_date.setOnClickListener(this::showDatePicker_PCR_date);

        Btn_certification.setOnClickListener(view13 -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
            nextStatus();
        });

        ImgBtn_pcr_certification_delete.setOnClickListener(view14 -> {
            deleteImage();
            isDataCorrect_pcr_certification = false;
            nextStatus();
        });

        EditText_isolation_date_start.setOnClickListener(this::showDatePicker_isolation_date_start);

        EditText_isolation_date_end.setOnClickListener(this::showDatePicker_isolation_date_end);

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
                isDataCorrect_isolation_date_start = s.length() != 0;
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
                isDataCorrect_isolation_date_end = s.length() != 0;
                nextStatus();
            }
        });

        Btn_next.setOnClickListener(view17 -> {
            saveData();
        });
        return view;
    }
    //
    // 選取日期 (單日)
    //
    private void showDatePicker_PCR_date(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);      //取得現在的日期年月日
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(view.getContext(), (view1, year1, month1, day1) -> {
            month1 = month1 + 1;

            DatePickerVerification dpv = new DatePickerVerification(year1, month1, day1);

            Date date_picker = dpv.processDatePickerResult(view1.getContext());
            dpv.dateVerification(view1.getContext(), date_picker);
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
                Date date_picker = dpv.processDatePickerResult(view.getContext());
                dpv.dateVerification(view.getContext(), date_picker);
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
                        startDateLegal = dpv.dateVerification_twoDate(view.getContext(), date_picker, date_end);
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
                        Toast.makeText(view.getContext(), "起始日應該在結束日之前", Toast.LENGTH_SHORT).show();
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

        new DatePickerDialog(view.getContext(), (view1, year1, month1, day1) -> {
            month1 = month1 + 1;
            endDateFillIn = false;
            boolean endDateLegal;

            DatePickerVerification dpv = new DatePickerVerification(year1, month1, day1);
            Date date_picker = dpv.processDatePickerResult(view1.getContext());
            String date_string;

            String date_start_string = EditText_isolation_date_start.getText().toString();
            if(!startDateFillIn) {
                endDateLegal = true;
                String month_string = month1 + "";
                String day_string = day1 + "";
                String year_string = year1 + "";
                if(month1 < 10) {
                    month_string = "0" + month_string;
                }
                if(day1 < 10) {
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
                endDateLegal = dpv.dateVerification_twoDate(view1.getContext(), date_start, date_picker);
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
        }, year, month, day).show();
    }

    //
    // 照片顯示
    //
    private boolean uploadImage() {
        ImgView_pcr_certification.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ntut, null));
        ImgView_pcr_certification.setVisibility(View.VISIBLE);

        ImgBtn_pcr_certification_delete.setVisibility(View.VISIBLE);
        return true;
    }

    //
    // 照片清除
    //
    private void deleteImage() {
        ImgBtn_pcr_certification_delete.setVisibility(View.INVISIBLE);
        ImgView_pcr_certification.setVisibility(View.INVISIBLE);
        ImgView_pcr_certification.setImageDrawable(null);
    }

    //
    // 儲存資料
    //
    private void saveData() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        if(isPositive && isInsulation) {
            editor.putString(ET_positive_date, EditText_positive_date.getText().toString());
            editor.putString(ET_isolation_date_start, EditText_isolation_date_start.getText().toString());
            editor.putString(ET_isolation_date_end, EditText_isolation_date_end.getText().toString());
            editor.putInt(is_PCR_positive, 1);
            editor.putInt(is_PCR_isolation, 1);
        }
        else if(isPositive) {
            editor.putString(ET_positive_date, EditText_positive_date.getText().toString());
            editor.putString(ET_isolation_date_start, "");
            editor.putString(ET_isolation_date_end, "");
            editor.putInt(is_PCR_positive, 1);
            editor.putInt(is_PCR_isolation, 0);
        }
        else{
            editor.putString(ET_positive_date, "");
            editor.putString(ET_isolation_date_start, "");
            editor.putString(ET_isolation_date_end, "");
            editor.putInt(is_PCR_positive, 0);
            editor.putInt(is_PCR_isolation, 0);
        }
        editor.apply();
    }

    //
    // next button 狀態是否開啟
    //
    private void nextStatus() {
        boolean isOpenNext = false;

        if(isPositive && isInsulation) {
            if(isDataCorrect_positive_date && isDataCorrect_pcr_certification && isDataCorrect_isolation_date_start && isDataCorrect_isolation_date_end) {
                isOpenNext = true;
            }
        }
        else if(isPositive) {
            if(isDataCorrect_positive_date && isDataCorrect_pcr_certification) {
                isOpenNext = true;
            }
        }
        else {
            isOpenNext = true;
        }

        Btn_next.setEnabled(isOpenNext);
    }
}
