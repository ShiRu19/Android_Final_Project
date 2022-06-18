package t4.sers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import t4.sers.R;

public class OtherFootprintsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_footprints);
        Button addfootprintbtn =(Button) findViewById(R.id.addfootprint_btn);
        addfootprintbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass( OtherFootprintsActivity.this, AddFootprintsActivity.class);
                startActivity(intent);
            }
        });
    }




}
