package t4.sers.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.shuhart.stepview.StepView;

import java.util.ArrayList;

import t4.sers.R;
import t4.sers.fragment.HelloReportFragment;

public class ReportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().hide();

        StepView stepView = findViewById(R.id.step_view);
        ArrayList<String> list = new ArrayList<>();

        list.add("歡迎");
        list.add("快篩");
        list.add("PCR");
        list.add("確認");

        stepView.getState().stepsNumber(4).steps(list).commit();

        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.replace(R.id.report_fragment_container, HelloReportFragment.newInstance("bla", "bla")).commit();
    }
}
