package t4.sers.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import t4.sers.R;
import t4.sers.adapter.CourseTableAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String courseDataJson;
    private String mParam2;

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
    // TODO: Rename and change types and number of parameters
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
            courseDataJson = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        RecyclerView courseTable = view.findViewById(R.id.course_recycleview);
        JsonArray jsonArray = JsonParser.parseString(courseDataJson).getAsJsonArray();

        List<String> courseCodeList = new ArrayList<>();
        List<String> courseNameList = new ArrayList<>();
        List<String> courseTeacherList = new ArrayList<>();

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
            courseCodeList.add(courseCode);
            courseNameList.add(courseName);
            courseTeacherList.add(courseTeacher.toString());
        }

        courseTable.setAdapter(new CourseTableAdapter(getContext(), courseCodeList, courseNameList, courseTeacherList));
        courseTable.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}