package t4.sers.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import t4.sers.R;
import t4.sers.activity.LobbyActivity;
import t4.sers.activity.ReportActivity;
import t4.sers.util.FirebaseUpload;
import t4.sers.util.HashUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FinishReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinishReportFragment extends Fragment {

    private static FinishReportFragment instance;
    private static final String ET_rapid_date = "EditText rapid test positive date";
    private static final String IV_rapid_certification = "ImageView rapid certification";
    private static final String ET_positive_date = "EditText pcr positive date";
    private static final String IV_pcr_certification = "ImageView pcr certification";
    private static final String ET_isolation_date_start = "EditText isolation start date";
    private static final String ET_isolation_date_end = "EditText isolation end date";
    private static final String is_PCR_positive = "Is PCR positive";
    private static final String is_PCR_negative = "Is PCR negative";
    private static final String is_PCR_unknown = "Is PCR unknown";
    private static final String is_PCR_isolation = "Is PCR isolation";

    private final String UPDATE_STATUS_LOADING = "loading";
    private final String UPDATE_STATUS_OK = "OK";
    private final String UPDATE_STATUS_ERROR = "ERROR";

    public FinishReportFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FinishReportFragment newInstance() {
        instance = new FinishReportFragment();
        Bundle args = new Bundle();
        instance.setArguments(args);
        return instance;
    }

    public static FinishReportFragment getInstance(){
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_finish, container, false);

        SharedPreferences loginPref = getActivity().getSharedPreferences("loginPref", MODE_PRIVATE);
        SharedPreferences dataPref = getActivity().getSharedPreferences("t4.sers.activity.positivesharedprefs", MODE_PRIVATE);

        String studentID = loginPref.getString("username", "");
        Uri rapidTestUri = Uri.parse(dataPref.getString(IV_rapid_certification, ""));

        Map<String, String> rapidTestStatusMessage = new HashMap<>();
        rapidTestStatusMessage.put(UPDATE_STATUS_LOADING, "正在上傳快篩證明圖片至資料庫");
        rapidTestStatusMessage.put(UPDATE_STATUS_OK, "成功上傳快篩證明圖片至資料庫");
        rapidTestStatusMessage.put(UPDATE_STATUS_ERROR, "上傳快篩證明圖片至資料庫失敗");
        photoUpload(HashUtil.hash(studentID) + "_rapid.png", rapidTestUri, R.id.textView26, R.id.progressBar2, R.id.progressBar2_imageview_ok, R.id.progressBar2_imageview_error, rapidTestStatusMessage);

        Uri pcrUri = Uri.parse(dataPref.getString(IV_pcr_certification, ""));

        if(!dataPref.getString(IV_pcr_certification, "").equals("")) {
            Map<String, String> PCRStatusMessage = new HashMap<>();
            PCRStatusMessage.put(UPDATE_STATUS_LOADING, "正在上傳 PCR 證明圖片至資料庫");
            PCRStatusMessage.put(UPDATE_STATUS_OK, "成功上傳 PCR 證明圖片至資料庫");
            PCRStatusMessage.put(UPDATE_STATUS_ERROR, "上傳 PCR 證明圖片至資料庫失敗");
            photoUpload(HashUtil.hash(studentID) + "_pcr.png", pcrUri, R.id.textView27, R.id.progressBar3, R.id.progressBar3_imageview_ok, R.id.progressBar3_imageview_error, PCRStatusMessage);
        }else{
            LinearLayout linearLayout = view.findViewById(R.id.finish_pcr_linear_layout);
            linearLayout.setVisibility(View.GONE);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("AdministratorSeen", false);
        map.put("AdministratorVerdict", 0);
        map.put("Isolate", dataPref.getInt(is_PCR_isolation, 0) != 0);
        map.put("PCRResult", dataPref.getInt(is_PCR_unknown, 0) != 0);
        map.put("ReportTime", Timestamp.now());
        map.put("StudentDepartment", loginPref.getString("department", ""));
        map.put("StudentID", studentID);
        map.put("StudentName", loginPref.getString("studentName", ""));
        map.put("documentID", HashUtil.hash(studentID));

        try {
            String isolateDateString = dataPref.getString(ET_isolation_date_start, "");
            String pcrResultDateString = dataPref.getString(ET_positive_date, "");
            String rapidTestResultDateString = dataPref.getString(ET_rapid_date, "");
            if(!isolateDateString.equals("")) {
                Date isolateDate = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN).parse(isolateDateString);
                map.put("IsolateDate", new Timestamp(isolateDate));
            }
            if(!pcrResultDateString.equals("")) {
                Date pcrResultDate = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN).parse(pcrResultDateString);
                map.put("PCRResultDate", new Timestamp(pcrResultDate));
            }
            if(!rapidTestResultDateString.equals("")) {
                Date rapidTestDate = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN).parse(rapidTestResultDateString);
                map.put("RapidTestDate", new Timestamp(rapidTestDate));
            }
        }catch (ParseException e){
            e.printStackTrace();
        }

        Map<String, String> dataUploadStatusMessage = new HashMap<>();
        dataUploadStatusMessage.put(UPDATE_STATUS_LOADING, "正在上傳確診資料至資料庫");
        dataUploadStatusMessage.put(UPDATE_STATUS_OK, "成功上傳確診資料至資料庫");
        dataUploadStatusMessage.put(UPDATE_STATUS_ERROR, "上傳確診資料至資料庫失敗");

        dataUpload(HashUtil.hash(studentID), map, R.id.textView25, R.id.progressBar1, R.id.progressBar1_imageview_ok, R.id.progressBar1_imageview_error, dataUploadStatusMessage);

        view.findViewById(R.id.finish_button).setOnClickListener(view1 -> {
            cleanup();
            startActivity(new Intent(getActivity(), LobbyActivity.class));
        });

        return view;
    }

    public void photoUpload(String imageName, Uri uri, int textViewID, int progressBarID, int statusOKImageviewID, int statusErrorImageviewID, Map<String, String> message) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference(imageName);
        reference.putFile(uri).addOnProgressListener(snapshot -> {
            updateStatusTextViewUI(UPDATE_STATUS_LOADING, textViewID, progressBarID, statusOKImageviewID, statusErrorImageviewID, message);
        }).addOnSuccessListener(taskSnapshot -> {
            updateStatusTextViewUI(UPDATE_STATUS_OK, textViewID, progressBarID, statusOKImageviewID, statusErrorImageviewID, message);
        }).addOnFailureListener(e -> {
            updateStatusTextViewUI(UPDATE_STATUS_ERROR, textViewID, progressBarID, statusOKImageviewID, statusErrorImageviewID, message);
        });
    }

    public void dataUpload(String dataName, Map<String, Object> data, int textViewID, int progressBarID, int statusOKImageviewID, int statusErrorImageviewID, Map<String, String> message){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("report").document(dataName).set(data).addOnSuccessListener(unused -> {
            updateStatusTextViewUI(UPDATE_STATUS_OK, textViewID, progressBarID, statusOKImageviewID, statusErrorImageviewID, message);
        }).addOnFailureListener(e -> {
            updateStatusTextViewUI(UPDATE_STATUS_ERROR, textViewID, progressBarID, statusOKImageviewID, statusErrorImageviewID, message);
        });
    }

    public void updateStatusTextViewUI(String status, int textViewID, int progressBarID, int statusOKImageviewID, int statusErrorImageviewID, Map<String, String> message){
        if(getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if(status.equals(UPDATE_STATUS_LOADING)) {
                    TextView textView = getActivity().findViewById(textViewID);
                    textView.setText(message.get(UPDATE_STATUS_LOADING));
                }else if(status.equals(UPDATE_STATUS_OK)){
                    TextView textView = getActivity().findViewById(textViewID);
                    textView.setText(message.get(UPDATE_STATUS_OK));
                    ProgressBar progressBar = getActivity().findViewById(progressBarID);
                    ImageView okIcon = getActivity().findViewById(statusOKImageviewID);
                    progressBar.setVisibility(View.GONE);
                    okIcon.setVisibility(View.VISIBLE);
                }else if(status.equals(UPDATE_STATUS_ERROR)){
                    TextView textView = getActivity().findViewById(textViewID);
                    textView.setText(message.get(UPDATE_STATUS_ERROR));
                    ProgressBar progressBar = getActivity().findViewById(progressBarID);
                    ImageView errorIcon = getActivity().findViewById(statusErrorImageviewID);
                    progressBar.setVisibility(View.GONE);
                    errorIcon.setVisibility(View.VISIBLE);
                }
                ProgressBar progressBar1 = getActivity().findViewById(R.id.progressBar1);
                ProgressBar progressBar2 = getActivity().findViewById(R.id.progressBar2);
                ProgressBar progressBar3 = getActivity().findViewById(R.id.progressBar3);
                Button button = getActivity().findViewById(R.id.finish_button);
                button.setEnabled(progressBar1.getVisibility() == View.GONE && progressBar2.getVisibility() == View.GONE && progressBar3.getVisibility() == View.GONE);
            });
        }
    }

    private void cleanup() {
        SharedPreferences dataPref = getActivity().getSharedPreferences("t4.sers.activity.positivesharedprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = dataPref.edit();
        editor.clear();
        editor.commit();
    }
}