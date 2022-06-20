package t4.sers.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.shuhart.stepview.StepView;
import java.util.ArrayList;
import t4.sers.R;
import t4.sers.fragment.ConfirmReportFragment;
import t4.sers.fragment.HelloReportFragment;
import t4.sers.fragment.PCRReportFragment;
import t4.sers.fragment.RapidTestReportFragment;
import t4.sers.receiver.UploadReceiver;

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
        list.add("完成");

        stepView.setOnStepClickListener(step -> {
            StepView sv = findViewById(R.id.step_view);
            int tag = (int) sv.getTag();
            Fragment[] fragmentList = {HelloReportFragment.newInstance("", ""), RapidTestReportFragment.newInstance(), PCRReportFragment.newInstance(), ConfirmReportFragment.newInstance()};
            if(tag == list.size() - 1){
                return;
            }
            if(step == 0){
                return;
            }
            if(step <= tag){
                sv.go(step, true);
                FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.report_fragment_container, fragmentList[step]).commit();
            }
        });

        stepView.getState().stepsNumber(4).steps(list).commit();
        stepView.setTag(1);

        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.replace(R.id.report_fragment_container, HelloReportFragment.newInstance("bla", "bla")).commit();
    }
}
