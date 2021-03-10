package com.example.birthday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;



import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mNickName;
    private EditText mDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText mDesc;
    String sName = "";
    String sNickName = "";
    String sDate = "";
    String sDesc = "";
    DatabaseHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mDbHelper = new DatabaseHelper(this);
        findViewById(R.id.addActivity).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return true;
            }
        });

        mName = findViewById(R.id.etName);
        mNickName = findViewById(R.id.etNickName);
        mDate = findViewById(R.id.etDate);
        mDesc = findViewById(R.id.etDescription);


        //EditText_setup();
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year;
                int month;
                int day;
                if(sDate.isEmpty()) {
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                }
                else{
                    String sYear = sDate.split("/")[2];
                    String sMonth = sDate.split("/")[0];
                    String sDay = sDate.split("/")[1];
                    year = Integer.parseInt(sYear);//calendar.get(Calendar.YEAR);
                    month = Integer.parseInt(sMonth) - 1;
                    day = Integer.parseInt(sDay);
                }

                DatePickerDialog dialog = new DatePickerDialog(
                        AddActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String Date = month + "/" + dayOfMonth + "/" + year;
                mDate.setText(Date);
                sDate = mDate.getText().toString();
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.cancel:
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;

            case R.id.check:
                sName = mName.getText().toString();
                sNickName = mNickName.getText().toString();
                sDesc = mDesc.getText().toString();
                if(sName.isEmpty()){
                    Toast.makeText(AddActivity.this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                if(sDate.isEmpty()){
                    Toast.makeText(AddActivity.this, "Birthday cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    AddData(sName, sNickName, sDate, sDesc);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddData(String sName, String sNickName, String sDate, String sDesc){

        boolean InsertData = mDbHelper.addData(sName, sNickName, sDate, sDesc);

        if(InsertData){
            startAlarm(sDate, sName);
            Toast.makeText(AddActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
        }

    }

    public void hideKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }

    public void startAlarm(String sDate, String sName){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());

        Intent intent = new Intent(AddActivity.this, Broadcast.class);
        intent.putExtra("name", sName);

        int len = sName.length();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int Year = c.get(Calendar.YEAR);
        int bYear = Integer.parseInt(sDate.split("/")[2]);
        int Month = Integer.parseInt(sDate.split("/")[0]) - 1;
        int Day = Integer.parseInt(sDate.split("/")[1]) - 6;
        int bDay = Integer.parseInt(sDate.split("/")[1]);
        c1.set(bYear, Month, bDay, 00, 00, 0);
        c1.set(Calendar.MILLISECOND, 0);
        int id = (int) c1.getTimeInMillis() + len + sName.charAt(0) + sName.charAt(sName.length()-1);
        System.out.println("id: " + c1.getTime() + " | " + c1.getTimeInMillis());
        intent.putExtra("id", id);
        //System.out.println("c1: " + c1.getTimeInMillis());
        //for(i = Year; i < Year + 111; i++){
        c.set(Year, Month, Day, 07, 00, 0);
            //c.set(i, Month, Day, 07, 00, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, 0);
        //System.out.println("c: " + c.getTimeInMillis());


        if (c.getTimeInMillis() > System.currentTimeMillis()){
            System.out.println("Alarm should go off soon");
        }
        else {
            System.out.println("Alarm is already passed");
            c.set(Year+1, Month, Day, 07, 00, 0);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        //System.out.println("!!!!!!!!!!!!!!!   " + System.currentTimeMillis());
        //}
        //System.out.println("c1: " + System.currentTimeMillis());

    }

}