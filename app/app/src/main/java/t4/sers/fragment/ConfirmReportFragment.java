package t4.sers.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

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


        LinearLayout LinearLayout_PCR_positive = view.findViewById(R.id.linearLayout_pcr_positive);
        LinearLayout LinearLayout_PCR_negative = view.findViewById(R.id.linearLayout_pcr_negative);
        LinearLayout LinearLayout_PCR_unknown = view.findViewById(R.id.linearLayout_pcr_unknown);
        LinearLayout LinearLayout_isolation_yes = view.findViewById(R.id.LinearLayout_pcr_isolation_yes);
        LinearLayout LinearLayout_isolation_no = view.findViewById(R.id.LinearLayout_pcr_isolation_no);

        String sharedPrefFile = "t4.sers.activity.positivesharedprefs";
        SharedPreferences mPreferences;

        if(getActivity() != null) {
            mPreferences = getActivity().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

            // 快篩
            TextView_rapid_date.setText(mPreferences.getString(ET_rapid_date, "error"));

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
        }

        return view;
    }
}