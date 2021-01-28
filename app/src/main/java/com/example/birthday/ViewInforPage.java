package com.example.birthday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class ViewInforPage extends AppCompatActivity {

    AlertDialog dialog;
    AlertDialog.Builder builder;
    private Button bt;
    private EditText etName;
    private EditText etNickname;
    private EditText etBirthday;
    private EditText etAddition;
    private int id;
    private String name = "";
    private String nickname = "";
    private String birthday = "";
    private String addition = "";
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_infor_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        bt = findViewById(R.id.btDelete);
        etName = findViewById(R.id.etName);
        etName.setKeyListener(null);
        etNickname = findViewById(R.id.etNickname);
        etNickname.setKeyListener(null);
        etBirthday = findViewById(R.id.etDate);
        etBirthday.setKeyListener(null);
        etAddition = findViewById(R.id.etMultiLine);
        etAddition.setKeyListener(null);

        //setText();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(ViewInforPage.this);
                builder.setTitle("Delete person");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            db.delete(getIntent().getExtras().getString("name"), getIntent().getExtras().getString("date"),
                                    getIntent().getExtras().getString("nickname"));
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Something Wrong opening the Database", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplicationContext(), "Person Deleted Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        setText();
        //setRetText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.edit:
                Intent intent = new Intent(ViewInforPage.this, EditInfoPage.class);
                /*intent.putExtra("name", getIntent().getExtras().getString("name"));
                intent.putExtra("nickname", getIntent().getExtras().getString("nickname"));
                intent.putExtra("date", getIntent().getExtras().getString("date"));
                intent.putExtra("addition", getIntent().getExtras().getString("addition"));*/
                intent.putExtra("name", etName.getText().toString());
                intent.putExtra("nickname", etNickname.getText().toString());
                intent.putExtra("date", etBirthday.getText().toString());
                intent.putExtra("addition", etAddition.getText().toString());
                intent.putExtra("id", id);
                //startActivity(intent);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Log.d(TAG, "Edit Menu");
                return true;

            case android.R.id.home:
                /*Intent intent1 = new Intent(ViewInforPage.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putInt("id", id);
                intent1.putExtras(b);
                startActivity(intent1);
                Log.d(TAG, "home Menu");*/
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;

            default:
                Log.d(TAG, "Default Menu");
                return super.onOptionsItemSelected(item);
        }
    }
    public void setText(){
        if(!name.isEmpty()) {
            etName.setText(name);
            etNickname.setText(nickname);
            etBirthday.setText(birthday);
            etAddition.setText(addition);
        }
        else{
            etName.setText(getIntent().getExtras().getString("name"));
            etNickname.setText(getIntent().getExtras().getString("nickname"));
            etBirthday.setText(getIntent().getExtras().getString("date"));
            etAddition.setText(getIntent().getExtras().getString("addition"));
        }
        id = getIntent().getExtras().getInt("id");
    }
    public void setRetText(){
        etName.setText(getIntent().getExtras().getString("name1"));
        etNickname.setText(getIntent().getExtras().getString("nickname1"));
        etBirthday.setText(getIntent().getExtras().getString("date1"));
        etAddition.setText(getIntent().getExtras().getString("addition1"));
        id = getIntent().getExtras().getInt("id");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                name = data.getStringExtra("name1");
                nickname = data.getStringExtra("nickname1");
                birthday = data.getStringExtra("date1");
                addition = data.getStringExtra("addition1");
            }
        }
    }
}