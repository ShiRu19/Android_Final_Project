package t4.sers.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.shuhart.stepview.StepView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import t4.sers.R;
import t4.sers.activity.DatePickerVerification;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RapidTestReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RapidTestReportFragment extends Fragment {

    private EditText EditText_rapid_date;

    private ImageButton ImgBtn_rapid_certification_delete;
    private ImageView ImgView_rapid_certification;

    private Button Btn_next;

    private boolean dateCompleted = false;
    private boolean imageCompleted = false;

    private SharedPreferences mPreferences;
    private static final String ET_rapid_date = "EditText rapid test positive date";
    private static final String IV_rapid_certification = "ImageView rapid certification";


    public RapidTestReportFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static RapidTestReportFragment newInstance() {
        RapidTestReportFragment fragment = new RapidTestReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report_rapid, container, false);

        EditText_rapid_date = view.findViewById(R.id.EditText_rapidAntigenTest_date);
        ImageButton imgBtn_rapid_date = view.findViewById(R.id.ImageButton_rapidAntigenTest_date);
        Button btn_certification = view.findViewById(R.id.Button_rapidAntigenTest_certification);
        ImgBtn_rapid_certification_delete = view.findViewById(R.id.Button_rapidAntigenTest_certification_delete);
        ImgView_rapid_certification = view.findViewById(R.id.ImageView_rapidAntigenTest_certification);
        Btn_next = view.findViewById(R.id.Button_next_rapid);

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
                    imageCompleted = true;
                    ImgView_rapid_certification.setImageBitmap(selectedImageBitmap);
                    ImgView_rapid_certification.setTag(selectedImageUri.toString());
                    checkAllDataCompleted();
                }
            }
        });

        if(getActivity() != null) {
            String sharedPrefFile = "t4.sers.activity.positivesharedprefs";
            mPreferences = getActivity().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        }

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
                else dateCompleted = EditText_rapid_date.getText().length() != 0;
                checkAllDataCompleted();
            }
        });

        EditText_rapid_date.setOnClickListener(this::showDatePicker);

        imgBtn_rapid_date.setOnClickListener(this::showDatePicker);

        btn_certification.setOnClickListener(view13 -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
            checkAllDataCompleted();
        });

        ImgBtn_rapid_certification_delete.setOnClickListener(view14 -> {
            deleteImage();
            imageCompleted = false;
            checkAllDataCompleted();
        });

        Btn_next.setOnClickListener(view15 -> {
            saveData();

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.report_fragment_container, PCRReportFragment.newInstance()).commit();

            StepView stepView = getActivity().findViewById(R.id.step_view);
            stepView.go(2, true);
        });

        return view;
    }

    private void checkAllDataCompleted() {
        setBtnEnabled(dateCompleted && imageCompleted);
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

        new DatePickerDialog(view.getContext(), (view1, year1, month1, day1) -> {
            month1 = month1 + 1;

            DatePickerVerification dpv = new DatePickerVerification(year1, month1, day1);
            Date date_picker = dpv.processDatePickerResult(view1.getContext());
            dpv.dateVerification(view1.getContext(), date_picker);
            String date_string = dpv.getDateString(0);

            // 填入結果
            if(date_string.equals(getResources().getString(R.string.notification_dateHint))) {
                // 輸入不合法
                EditText_rapid_date.setTextColor(getResources().getColor(R.color.red_500));
            }
            else{
                // 輸入合法
                EditText_rapid_date.setTextColor(getResources().getColor(R.color.black));
            }
            EditText_rapid_date.setText(date_string);   // 填入結果
        }, year, month, day).show();
    }

    //
    // 照片清除
    //
    public void deleteImage() {
        ImgBtn_rapid_certification_delete.setVisibility(View.INVISIBLE);
        ImgView_rapid_certification.setVisibility(View.INVISIBLE);
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