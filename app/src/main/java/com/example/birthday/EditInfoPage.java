package com.example.birthday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
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
import static android.content.ContentValues.TAG;

public class EditInfoPage extends AppCompatActivity {

    //TODO: Display data in editText
    private EditText etName;
    private EditText etNickname;
    private EditText etBirthday;
    private EditText etAddition;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String sDate = "";
    String sName = "";
    String sNickname = "";
    String sAdd = "";
    DatabaseHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info_page);
        mDbHelper = new DatabaseHelper(this);
        findViewById(R.id.EditActivity).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return true;
            }
        });

        etName = findViewById(R.id.etName);
        etNickname = findViewById(R.id.etNickname);
        etBirthday = findViewById(R.id.etDate);
        etAddition = findViewById(R.id.etMultiLine);
        setText();

        etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                String sYear = sDate.split("/")[2];
                String sMonth = sDate.split("/")[0];
                String sDay = sDate.split("/")[1];
                int year = Integer.parseInt(sYear);//calendar.get(Calendar.YEAR);
                int month = Integer.parseInt(sMonth) - 1;
                int day = Integer.parseInt(sDay);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditInfoPage.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
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
                etBirthday.setText(Date);
                //sDate = etBirthday.getText().toString();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                boolean ins = mDbHelper.upDate(etName.getText().toString(), etNickname.getText().toString()
                ,etBirthday.getText().toString(), etAddition.getText().toString(), sName, sNickname, sDate);
                if(ins){
                    Toast.makeText(EditInfoPage.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                }
                //Intent intent = new Intent(EditInfoPage.this, ViewInforPage.class);
                Intent intent = new Intent();
                intent.putExtra("name1", etName.getText().toString());
                intent.putExtra("nickname1", etNickname.getText().toString());
                intent.putExtra("date1", etBirthday.getText().toString());
                intent.putExtra("addition1", etAddition.getText().toString());
                intent.putExtra("id", getIntent().getExtras().getInt("id"));
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //startActivity(intent);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void setText(){
        etName.setText(getIntent().getExtras().getString("name"));
        sName = etName.getText().toString();

        etNickname.setText(getIntent().getExtras().getString("nickname"));
        sNickname = etNickname.getText().toString();

        etBirthday.setText(getIntent().getExtras().getString("date"));
        sDate = etBirthday.getText().toString();

        etAddition.setText(getIntent().getExtras().getString("addition"));
        sAdd = etAddition.getText().toString();
    }
    public void hideKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }
}