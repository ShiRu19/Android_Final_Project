package t4.sers.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import t4.sers.R;
import t4.sers.adapter.CourseConfirmTableAdapter;
import t4.sers.adapter.CourseTableAdapter;
import t4.sers.adapter.SchoolConfirmTableAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Runnable runnable = () -> {
            try {
                String confirmCaseURL = getString(R.string.confirm_course_URL);
                Connection.Response response = Jsoup.connect(confirmCaseURL).ignoreContentType(true).method(Connection.Method.GET).execute();
                String body = response.body();
                JsonObject responseData = JsonParser.parseString(body).getAsJsonObject();
                JsonObject dateObject = responseData.get("data").getAsJsonObject();
                List<String> courseCode = new ArrayList<>();
                List<String> courseName = new ArrayList<>();
                List<String> courseTeacher = new ArrayList<>();
                List<String> courseTime = new ArrayList<>();
                for(String key : dateObject.keySet()){
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.TAIWAN);
                    DateFormat dateFormatString = new SimpleDateFormat("MM/dd(E)", Locale.TAIWAN);
                    JsonObject dateIsolateObject = dateObject.get(key).getAsJsonObject();
                    for(String code : dateIsolateObject.keySet()){
                        Date date = dateFormat.parse(key);
                        DateTime dateTime = new DateTime(date);
                        JsonObject courseObject = dateIsolateObject.get(code).getAsJsonObject();
                        String name = courseObject.get("courseName").getAsString();
                        int courseWeekDay = courseObject.get("courseIsolateDate").getAsInt();
                        JsonArray teacherArray = courseObject.get("courseTeacher").getAsJsonArray();
                        StringBuilder teacherStringBuilder = new StringBuilder();
                        StringBuilder timeStringBuilder = new StringBuilder();
                        for (int i = 0; i < teacherArray.size(); i++){
                            teacherStringBuilder.append(teacherArray.get(i).getAsString()).append("\n");
                        }
                        courseCode.add(code);
                        courseName.add(name);
                        courseTeacher.add(teacherStringBuilder.toString());
                        dateTime = dateTime.minusDays(Math.abs(dateTime.getDayOfWeek() - courseWeekDay));
                        DateTime isolateFinish = dateTime.plusDays(2);
                        assert date != null;
                        courseTime.add(dateFormatString.format(dateTime.toDate()).replace("週", "") + "\n至 " + dateFormatString.format(isolateFinish.toDate()).replace("週", ""));
                    }
                }
                if(getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        RecyclerView confirmDataRecyclerView = getActivity().findViewById(R.id.confirm_course_recycler_view);
                        confirmDataRecyclerView.setAdapter(new CourseConfirmTableAdapter(getActivity(), courseCode, courseName, courseTeacher, courseTime));
                        confirmDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    });
                }
            }catch (IOException | ParseException e){
                e.printStackTrace();
            }
        };
        mExecutor.execute(runnable);
        return inflater.inflate(R.layout.fragment_course, container, false);
    }
}