package t4.sers.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import t4.sers.R;
import t4.sers.adapter.CourseTableAdapter;
import t4.sers.receiver.WarningReceiver;
import t4.sers.util.ConfirmCourse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class PersonalFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String courseDataJson;
    private String mParam2;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<String> courseCodeList;
    private List<String> courseNameList;
    private List<String> courseTeacherList;

    ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    public PersonalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalFragment.
     */
    public static PersonalFragment newInstance(String param1, String param2) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            SharedPreferences loginPreferenceEditor = getActivity().getSharedPreferences("loginPref", MODE_PRIVATE);
            courseDataJson = loginPreferenceEditor.getString("courseDataJson", "{}");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        RecyclerView courseTable = view.findViewById(R.id.course_recycleview);
        JsonArray jsonArray = JsonParser.parseString(courseDataJson).getAsJsonArray();

        courseCodeList = new ArrayList<>();
        courseNameList = new ArrayList<>();
        courseTeacherList = new ArrayList<>();

        for(int i = 0; i < jsonArray.size(); i++){
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String courseCode = jsonObject.get("courseID").getAsString();
            String courseName = jsonObject.get("courseName").getAsString();
            JsonArray courseTeacherArray = jsonObject.get("courseTeacher").getAsJsonArray();
            StringBuilder courseTeacher = new StringBuilder();
            for(int j = 0; j < courseTeacherArray.size(); j++){
                courseTeacher.append(courseTeacherArray.get(j).getAsString());
                if(j != courseTeacherArray.size() - 1){
                    courseTeacher.append("\n");
                }
            }
            WarningReceiver.courseCode.add(courseCode);
            courseCodeList.add(courseCode);
            courseNameList.add(courseName);
            courseTeacherList.add(courseTeacher.toString());
        }

        courseTable.setAdapter(new CourseTableAdapter(getContext(), courseCodeList, courseNameList, courseTeacherList));
        courseTable.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.laySwipe1);
        swipeRefreshLayout.setOnRefreshListener(this::reloadData);

        reloadData();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void reloadData(){
        Runnable runnable = () -> {
            try {
                List<ConfirmCourse> list = new ArrayList<>();
                String confirmCaseURL = getString(R.string.confirm_course_URL);
                Connection.Response response = Jsoup.connect(confirmCaseURL).ignoreContentType(true).method(Connection.Method.GET).execute();
                String body = response.body();
                JsonObject responseData = JsonParser.parseString(body).getAsJsonObject();
                JsonObject dateObject = responseData.get("data").getAsJsonObject();
                for(String key : dateObject.keySet()){
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.TAIWAN);
                    JsonObject dateIsolateObject = dateObject.get(key).getAsJsonObject();
                    for(String code : dateIsolateObject.keySet()){
                        Date date = dateFormat.parse(key);
                        DateTime dateTime = new DateTime(date);
                        JsonObject courseObject = dateIsolateObject.get(code).getAsJsonObject();
                        int courseWeekDay = courseObject.get("courseIsolateDate").getAsInt();
                        dateTime = dateTime.minusDays(Math.abs(dateTime.getDayOfWeek() - courseWeekDay));
                        DateTime isolateFinish = dateTime.plusDays(2);
                        list.add(new ConfirmCourse(code, "", "", dateTime.withZone(DateTimeZone.forID("Asia/Taipei")), isolateFinish.withZone(DateTimeZone.forID("Asia/Taipei"))));
                    }
                }
                if(getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("warningLiftTime", MODE_PRIVATE);
                        for(ConfirmCourse confirmCourse : list){
                            String code = confirmCourse.getCode();
                            DateTime dateTime = confirmCourse.getRiskDurationStart();
                            DateTime isolateTime = confirmCourse.getRiskDurationEnd();
                            DateTime liftTime = new DateTime(sharedPreferences.getLong("liftTime", 0L));
                            if(courseCodeList.contains(code) && isolateTime.isAfter(liftTime)){
                                TextView title = getActivity().findViewById(R.id.textView3);
                                TextView subTitle = getActivity().findViewById(R.id.textView4);
                                title.setText("與確診者資料比對有接觸");
                                subTitle.setText("請勿驚慌，並遵循北科防疫指引");
                                title.setTextColor(getActivity().getColor(R.color.red_500));
                                subTitle.setTextColor(getActivity().getColor(R.color.red_500));
                                title.setBackground(AppCompatResources.getDrawable(getActivity(), R.drawable.personal_touch_warning_border));
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    });
                }
            }catch (IOException | ParseException e){
                e.printStackTrace();
            }
        };
        mExecutor.execute(runnable);
    }
}