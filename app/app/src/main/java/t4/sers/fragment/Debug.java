package t4.sers.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import t4.sers.R;
import t4.sers.util.FirebaseUpload;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Debug#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Debug extends Fragment {


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
                    imageView.setTag(selectedImageUri.toString());
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

        Button uploadButton = view.findViewById(R.id.button4);
        uploadButton.setOnClickListener(view1 -> {
            ImageView imageView = getActivity().findViewById(R.id.imageView4);
            Uri uri = Uri.parse((String) imageView.getTag());
            FirebaseUpload.uploadImage(getActivity(),"1234", uri);
        });

        return view;
    }

}