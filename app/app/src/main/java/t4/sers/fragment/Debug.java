package t4.sers.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import t4.sers.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Debug#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Debug extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Debug() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Debug.
     */
    // TODO: Rename and change types and number of parameters
    public static Debug newInstance(String param1, String param2) {
        Debug fragment = new Debug();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_debug, container, false);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
            if(result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                if(data != null && data.getData() != null){
                    Uri selectedImageUri = data.getData();
                    Bitmap selectedImageBitmap = null;
                    try {
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageView imageView = getActivity().findViewById(R.id.imageView4);
                    imageView.setImageBitmap(selectedImageBitmap);
                }
            }
        });

        Button pickPictureButton = view.findViewById(R.id.button2);
        pickPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                activityResultLauncher.launch(intent);
            }
        });

        return view;
    }

}