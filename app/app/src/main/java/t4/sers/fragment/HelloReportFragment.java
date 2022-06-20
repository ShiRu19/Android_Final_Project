package t4.sers.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import com.shuhart.stepview.StepView;

import java.util.Calendar;
import java.util.Date;

import t4.sers.R;
import t4.sers.activity.DatePickerVerification;
import t4.sers.activity.StudentNotificationActivity_PCR;
import t4.sers.activity.StudentNotificationActivity_rapid;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelloReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelloReportFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HelloReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelloReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelloReportFragment newInstance(String param1, String param2) {
        HelloReportFragment fragment = new HelloReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hello_report, container, false);

        Button button = view.findViewById(R.id.button3);

        button.setOnClickListener(view1 -> {

            if(getActivity() != null){
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.report_fragment_container, RapidTestReportFragment.newInstance()).commit();
                StepView stepView = getActivity().findViewById(R.id.step_view);
                stepView.go(1, true);

                SharedPreferences.Editor mPreferences = getActivity().getSharedPreferences("t4.sers.activity.positivesharedprefs", MODE_PRIVATE).edit();
                mPreferences.clear();
                mPreferences.apply();
            }
        });

        return view;
    }

}