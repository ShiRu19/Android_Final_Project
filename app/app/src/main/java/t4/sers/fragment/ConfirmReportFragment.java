package t4.sers.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import t4.sers.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmReportFragment extends Fragment {

    private static final String ET_rapid_date = "EditTextRapidTestPositiveDate";
    private static final String IV_rapid_certification = "ImageView rapid certification";

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

        EditText EditText_rapid_date = view.findViewById(R.id.EditText_rapidAntigenTest_date);

        String sharedPrefFile = "t4.sers.activity.positivesharedprefs";
        SharedPreferences mPreferences;

        if(getActivity() != null) {
            mPreferences = getActivity().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

            String s = mPreferences.getString(ET_rapid_date, "error");
            EditText_rapid_date.setText(s);
        }

        return view;
    }
}