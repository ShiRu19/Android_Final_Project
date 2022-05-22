package t4.sers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;

public class LobbyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        System.out.println(intent.getStringExtra("idToken"));

        Toast toast = Toast.makeText(this, "Hello, " + intent.getStringExtra("displayName") + "!", Toast.LENGTH_LONG);
        toast.show();

    }

}
