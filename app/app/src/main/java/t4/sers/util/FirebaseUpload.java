package t4.sers.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FirebaseUpload {

    public static void uploadImage(Activity activity, String imageName, Uri uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference(imageName);
        reference.putFile(uri).addOnProgressListener(snapshot -> {
            Log.d("FirebaseStorage", "Uploading");
            Intent broadcast = new Intent("FirebaseUpload").putExtra("status", "pending");
            activity.sendBroadcast(broadcast);
        }).addOnSuccessListener(taskSnapshot -> {
            Log.d("FirebaseStorage", "Upload Success");
            Intent broadcast = new Intent("FirebaseUpload").putExtra("status", "OK");
            activity.sendBroadcast(broadcast);
        }).addOnFailureListener(e -> {
            Log.d("FirebaseStorage", "Upload Failed");
            Intent broadcast = new Intent("FirebaseUpload").putExtra("status", "Failed");
            activity.sendBroadcast(broadcast);
        });
    }

}
