package t4.sers.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import t4.sers.R;

public class AddFootprintsActivity extends AppCompatActivity {

    private EditText footprints_date,footprints_time1,footprints_time2,activities,addfootprint_class,addfootprint_studentID,addfootprint_name;
    private Spinner spinner_location;
    private RecyclerView close_contact_recyclerview;
    private CloseContactAdapter adapter;
    private ArrayList<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfootprints);
        footprints_date = findViewById(R.id.pick_date);
        footprints_time1 = findViewById(R.id.pick_time1);
        footprints_time2 = findViewById(R.id.pick_time2);
        activities = findViewById(R.id.activities);

        spinner_location = findViewById(R.id.spinner_location);
        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();
                Toast.makeText(AddFootprintsActivity.this, result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        });
        // ????????????
        close_contact_recyclerview = (RecyclerView) findViewById(R.id.close_contact_recyclerview);
        // ??????RecyclerView???????????????
        close_contact_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        // ????????????
        close_contact_recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }
    public void showDatePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);      //??????????????????????????????
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(view.getContext(), (view1, year1, month1, day1) -> {
            month1 = month1 + 1;

            DatePickerVerification dpv = new DatePickerVerification(year1, month1, day1);
            Date date_picker = dpv.processDatePickerResult(AddFootprintsActivity.this);
            dpv.dateVerification(AddFootprintsActivity.this, date_picker);
            String date_string = dpv.getDateString(0);

                if(date_string == getResources().getString(R.string.notification_dateHint)) {
                    // ???????????????
                    footprints_date.setTextColor(getResources().getColor(R.color.red_500));
                    footprints_date.setText(date_string);   // ????????????
                }
                else{
                    // ????????????
                    footprints_date.setTextColor(getResources().getColor(R.color.black));
                    footprints_date.setText(date_string);   // ????????????

            }
        }, year, month, day).show();
    }
    int h1,h2,m1,m2;
    public void showTimePicker_start(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);      //???????????????????????????
        int min = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(AddFootprintsActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String mTime1 = String.valueOf(hourOfDay) + " : " + String.valueOf(minute);
                if(h1== 0 && m1== 0 && h2==0 && m2==0){
                    h1=hourOfDay;
                    m1=minute;
                    footprints_time1.setText(mTime1);// ????????????
                }
                else if((h1!= 0 || m1!= 0) && h2==0 && m2==0){
                    h1=hourOfDay;
                    m1=minute;
                    footprints_time1.setText(mTime1);// ????????????h1m1
                }
                else if(h2!= 0 || m2!=0){
                    h1=hourOfDay;
                    m1=minute;
                    if(h2> h1 || h1== h2 && m2> m1){
                        footprints_time1.setText(mTime1);//??????h1m1
                    }
                    else{
                        hint();}
                }
                else{
                    hint();}
            }

        }, hour, min, true).show();
    }
    public void showTimePicker_end(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);      //???????????????????????????
        int min = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(AddFootprintsActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String mTime2 = String.valueOf(hourOfDay) + " : " + String.valueOf(minute);
                if(h1== 0 && m1== 0 && h2==0 && m2==0){
                    h2=hourOfDay;
                    m2=minute;
                    footprints_time2.setText(mTime2);//????????????
                }
                else if((h1== 0 || m1== 0) && h2!=0 && m2!=0){
                    h2=hourOfDay;
                    m2=minute;
                    footprints_time2.setText(mTime2);//????????????h2m2
                }
                else if(h1!= 0 || m1!= 0){
                    h2=hourOfDay;
                    m2=minute;
                    if(h2> h1 || h1== h2 && m2> m1){
                        footprints_time2.setText(mTime2);// ????????????
                    }
                    else{
                        hint();}
                }
                else{
                    hint();}
            }
        }, hour, min, true).show();
    }

    public void hint(){
        Toast toast = Toast.makeText(this,"???????????????????????????",Toast.LENGTH_LONG);
        toast.show();
    }

    public void addStudentInfo(View view) {
        String studentClass = ((EditText) findViewById(R.id.addfootprint_class)).getText().toString();
        String studentID = ((EditText) findViewById(R.id.addfootprint_studentID)).getText().toString();
        String studentName = ((EditText) findViewById(R.id.addfootprint_name)).getText().toString();
        mData.add(studentClass+"         "+studentID+"         "+studentName);
        // ???????????????adapter
        adapter = new CloseContactAdapter(mData);
        // ??????adapter???close_contact_recyclerview
        close_contact_recyclerview.setAdapter(adapter);
    }

    public void addfootprint_finish(View view) {

    }
}
