package t4.sers.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import t4.sers.R;
import t4.sers.adapter.SchoolConfirmTableAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SchoolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchoolFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_1ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SwipeRefreshLayout swipeRefreshLayout;

    final List<String> indexList = new ArrayList<>();
    final List<String> uuidList = new ArrayList<>();
    final List<String> studentClassList = new ArrayList<>();
    final List<String> dateList = new ArrayList<>();

    int today = 0;

    public SchoolFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment school.
     */
    // TODO: Rename and change types and number of parameters
    public static SchoolFragment newInstance(String param1, String param2) {
        SchoolFragment fragment = new SchoolFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_school, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.laySwipe);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            reloadData();
            swipeRefreshLayout.setRefreshing(false);
        });
        reloadData();
        return view;
    }

    public void reloadData(){
        indexList.clear();
        uuidList.clear();
        studentClassList.clear();
        dateList.clear();
        today = 0;
        Handler handler = new Handler();
        Runnable runnable = () -> {
            try {
                String confirmCaseURL = getString(R.string.school_confirm_case_URL);
                Connection.Response response = Jsoup.connect(confirmCaseURL).ignoreContentType(true).method(Connection.Method.GET).execute();
                String body = response.body();
                JsonObject responseData = JsonParser.parseString(body).getAsJsonObject();
                JsonArray dataArray = responseData.get("data").getAsJsonArray();
                System.out.println(dataArray.size());
                for(int i = 0; i < dataArray.size(); i++){
                    JsonObject dataObject = dataArray.get(i).getAsJsonObject();
                    String ID = dataObject.get("ID").getAsString();
                    String studentClass = dataObject.get("studentClass").getAsString();
                    long confirmDate = dataObject.get("confirmDate").getAsLong();
                    Date date = new Date(confirmDate);
                    Date current = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    indexList.add(String.valueOf(i+1));
                    uuidList.add(ID);
                    studentClassList.add(studentClass);
                    dateList.add(sdf.format(date));
                    if(sdf.format(date).equals(sdf.format(current))) {
                        today += 1;
                    }
                }
                if(getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        RecyclerView confirmDataRecyclerView = getActivity().findViewById(R.id.confirm_recycler_view);
                        confirmDataRecyclerView.setAdapter(new SchoolConfirmTableAdapter(getActivity(), indexList, uuidList, studentClassList, dateList));
                        confirmDataRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        TextView todayConfirmCount = getActivity().findViewById(R.id.textView11);
                        TextView totalConfirmCount = getActivity().findViewById(R.id.textView12);
                        todayConfirmCount.setText(String.valueOf(today));
                        totalConfirmCount.setText(String.valueOf(indexList.size()));
                    });
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        };
        mExecutor.execute(runnable);
    }
}