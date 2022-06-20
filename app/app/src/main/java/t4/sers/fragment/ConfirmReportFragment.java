package t4.sers.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuhart.stepview.StepView;

import java.io.IOException;

import t4.sers.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmReportFragment extends Fragment {

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

    public ConfirmReportFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ConfirmReportFragment newInstance() { return new ConfirmReportFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report_confirm, container, false);

        TextView TextView_rapid_date = view.findViewById(R.id.TextView_rapidAntigenTest_date);
        TextView TextView_PCR_date = view.findViewById(R.id.TextView_PCR_date);
        TextView TextView_PCR_isolation_startDate = view.findViewById(R.id.TextView_PCR_isolation_startDate);
        TextView TextView_PCR_isolation_endDate = view.findViewById(R.id.TextView_PCR_isolation_endDate);

        ImageView ImageView_rapid_certification = view.findViewById(R.id.ImageView_rapidAntigenTest_certification);
        ImageView ImageView_PCR_certification = view.findViewById(R.id.ImageView_PCR_certification);

        LinearLayout LinearLayout_PCR_positive = view.findViewById(R.id.linearLayout_pcr_positive);
        LinearLayout LinearLayout_PCR_negative = view.findViewById(R.id.linearLayout_pcr_negative);
        LinearLayout LinearLayout_PCR_unknown = view.findViewById(R.id.linearLayout_pcr_unknown);
        LinearLayout LinearLayout_isolation_yes = view.findViewById(R.id.LinearLayout_pcr_isolation_yes);
        LinearLayout LinearLayout_isolation_no = view.findViewById(R.id.LinearLayout_pcr_isolation_no);

        Button button_next_view = view.findViewById(R.id.Button_next_confirm);

        String sharedPrefFile = "t4.sers.activity.positivesharedprefs";
        SharedPreferences mPreferences;

        if(getActivity() != null) {
            mPreferences = getActivity().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

            // 快篩
            TextView_rapid_date.setText(mPreferences.getString(ET_rapid_date, "error"));
            Uri selectedImageUri_rapid = Uri.parse(mPreferences.getString(IV_rapid_certification, ""));
            Bitmap selectedImageBitmap_rapid = null;
            try {
                selectedImageBitmap_rapid = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri_rapid);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView_rapid_certification.setImageBitmap(selectedImageBitmap_rapid);
            ImageView_rapid_certification.setTag(selectedImageUri_rapid.toString());

            // PCR
            int isPcrPositive = mPreferences.getInt(is_PCR_positive, -1);
            int isPcrNegative = mPreferences.getInt(is_PCR_negative, -1);
            int isPcrUnknown = mPreferences.getInt(is_PCR_unknown, -1);
            int isIsolation = mPreferences.getInt(is_PCR_isolation, -1);

            if(isPcrPositive == 1) {
                LinearLayout_PCR_positive.setVisibility(View.VISIBLE);
                LinearLayout_PCR_negative.setVisibility(View.GONE);
                LinearLayout_PCR_unknown.setVisibility(View.GONE);
                TextView_PCR_date.setText(mPreferences.getString(ET_positive_date, "error"));

                Uri selectedImageUri_PCR = Uri.parse(mPreferences.getString(IV_pcr_certification, ""));
                Bitmap selectedImageBitmap_PCR = null;
                try {
                    selectedImageBitmap_PCR = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri_PCR);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ImageView_PCR_certification.setImageBitmap(selectedImageBitmap_PCR);
                ImageView_PCR_certification.setTag(selectedImageUri_PCR.toString());

                if(isIsolation == 1) {
                    LinearLayout_isolation_yes.setVisibility(View.VISIBLE);
                    LinearLayout_isolation_no.setVisibility(View.GONE);
                    TextView_PCR_isolation_startDate.setText(mPreferences.getString(ET_isolation_date_start, "error"));
                    TextView_PCR_isolation_endDate.setText(mPreferences.getString(ET_isolation_date_end, "error"));
                }
                else {
                    LinearLayout_isolation_yes.setVisibility(View.GONE);
                    LinearLayout_isolation_no.setVisibility(View.VISIBLE);
                }
            }
            else if (isPcrNegative == 1) {
                LinearLayout_PCR_positive.setVisibility(View.GONE);
                LinearLayout_PCR_negative.setVisibility(View.VISIBLE);
                LinearLayout_PCR_unknown.setVisibility(View.GONE);
            }
            else if (isPcrUnknown == 1) {
                LinearLayout_PCR_positive.setVisibility(View.GONE);
                LinearLayout_PCR_negative.setVisibility(View.GONE);
                LinearLayout_PCR_unknown.setVisibility(View.VISIBLE);
            }

            button_next_view.setOnClickListener(view1 -> {
                if(getActivity() != null) {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.report_fragment_container, FinishReportFragment.newInstance()).commit();

                    StepView stepView = getActivity().findViewById(R.id.step_view);
                    stepView.setTag(Math.max((int) stepView.getTag(), 4));
                    stepView.go(4, true);
                }
            });
        }

        return view;
    }
}