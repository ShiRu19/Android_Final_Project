package t4.sers.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import t4.sers.fragment.FinishReportFragment;
import t4.sers.receiver.UploadReceiver;

public class FirebaseUpload {

    public static void uploadImage(Activity activity, String imageName, Uri uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference(imageName);
        reference.putFile(uri).addOnProgressListener(snapshot -> {
            Log.d("FirebaseStorage", "Uploading");
            Intent broadcast = new Intent(activity, FinishReportFragment.class).putExtra("status", "pending");
            broadcast.setAction("FirebaseUpbroadcastload");
            activity.sendBroadcast(broadcast);
        }).addOnSuccessListener(taskSnapshot -> {
            Log.d("FirebaseStorage", "Upload Success");
            Intent broadcast = new Intent(activity, FinishReportFragment.class).putExtra("status", "OK");
            broadcast.setAction("FirebaseUpload");
            activity.sendBroadcast(broadcast);
        }).addOnFailureListener(e -> {
            Log.d("FirebaseStorage", "Upload Failed");
            Intent broadcast = new Intent(activity, FinishReportFragment.class).putExtra("status", "Failed");
            broadcast.setAction("FirebaseUpload");
            activity.sendBroadcast(broadcast);
        });
    }

}
