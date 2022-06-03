package t4.sers.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.nio.file.Path;
import java.util.Locale;

import t4.sers.R;
import t4.sers.activity.LobbyActivity;
import t4.sers.activity.MainActivity;
import t4.sers.adapter.SettingFragmentRecyclerViewAdapter;
import t4.sers.placeholder.PlaceholderContent;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment implements SettingFragmentRecyclerViewAdapter.OnItemClickHandler{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARGS1 = "userName";
    private static final String ARGS2 = "photoPath";
    private static final String ARGS3 = "userEmail";

    private String userName;
    private String photoPath;
    private String userEmail;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SettingFragment.
     */

    public static SettingFragment newInstance(String param1, String param2, String param3) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARGS1, param1);
        args.putString(ARGS2, param2);
        args.putString(ARGS3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARGS1);
            photoPath = getArguments().getString(ARGS2);
            userEmail = getArguments().getString(ARGS3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting2, container, false);

        TextView name = view.findViewById(R.id.setting_name_textview);
        name.setText(userName);

        TextView email = view.findViewById(R.id.setting_email_textview);
        email.setText(userEmail);

        ImageView imageView = view.findViewById(R.id.setting_iv);
        if(photoPath != null && !photoPath.equals("")) {
            Bitmap myBitmap = BitmapFactory.decodeFile(new File(photoPath).getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }else{
            imageView.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_baseline_person_24));
            imageView.setColorFilter(AppCompatResources.getColorStateList(view.getContext(), R.color.green_500).getDefaultColor());
        }

        Context context = view.getContext();

        RecyclerView recyclerView = view.findViewById(R.id.setting_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));

        PlaceholderContent.setItems(context.getResources());
        recyclerView.setAdapter(new SettingFragmentRecyclerViewAdapter(context, PlaceholderContent.ITEMS, this));

        recyclerView.setOnClickListener(view1 -> {
            int position = recyclerView.getChildLayoutPosition(view1);
            Toast.makeText(view1.getContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
            if(position == 0){

            }
        });

        return view;
    }

    @Override
    public void onItemClick(String text) {
        if(text.equals(getString(R.string.setting_option_logout))){
            if(getActivity() != null) {
                getActivity().finish();
            }
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}