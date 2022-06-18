package t4.sers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import t4.sers.R;
import t4.sers.adapter.SettingFragmentRecyclerViewAdapter;
import t4.sers.placeholder.PlaceholderContent;
import t4.sers.util.SwipeRemoveHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Debug#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Debug extends Fragment implements SettingFragmentRecyclerViewAdapter.OnItemClickHandler {


    public Debug() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Debug newInstance() {
        Debug fragment = new Debug();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_debug, container, false);

        Context context = view.getContext();

        RecyclerView recyclerView = view.findViewById(R.id.debug_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));

        PlaceholderContent.setItems(context.getResources());
        recyclerView.setAdapter(new SettingFragmentRecyclerViewAdapter(context, PlaceholderContent.ITEMS, this));

        recyclerView.setOnClickListener(view1 -> {
            int position = recyclerView.getChildLayoutPosition(view1);
            Toast.makeText(view1.getContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeRemoveHelper());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onItemClick(String text) {
        /*if(text.equals(getString(R.string.setting_option_logout))) {
            if (getActivity() != null) {
                getActivity().finish();
            }
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }else if(text.equals("解除警示狀態")){
            Intent intent = new Intent(getContext(), WarningLiftActivity.class);
            startActivity(intent);
        }*/
    }
}