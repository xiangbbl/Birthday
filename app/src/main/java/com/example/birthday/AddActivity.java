package com.example.birthday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
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
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

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
                    /*Bundle b = new Bundle();
                    b.putInt("id", 1);
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);*/
                    finish();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddData(String sName, String sNickName, String sDate, String sDesc){

        boolean InsertData = mDbHelper.addData(sName, sNickName, sDate, sDesc);

        if(InsertData){
            Toast.makeText(AddActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
        }

    }
    public void EditText_setup(){
        /*mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName.getText().clear();
                sName = mName.getText().toString();
            }
        });
        mNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNickName.getText().clear();
                sNickName = mNickName.getText().toString();
            }
        });
        mDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDesc.getText().clear();
                sDesc = mDesc.getText().toString();
            }
        });*/
    }
    public void hideKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }


}