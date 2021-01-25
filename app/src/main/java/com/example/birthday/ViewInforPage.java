package com.example.birthday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ViewInforPage extends AppCompatActivity {

    AlertDialog dialog;
    AlertDialog.Builder builder;
    private Button bt;
    private EditText etName;
    private EditText etNickname;
    private EditText etBirthday;
    private EditText etAddition;
    DatabaseHelper db;

    //TODO: Display data in editText
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_infor_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseHelper(this);
        bt = findViewById(R.id.btDelete);
        etName = findViewById(R.id.etName);
        etNickname = findViewById(R.id.etNickname);
        etBirthday = findViewById(R.id.etDate);
        etAddition = findViewById(R.id.etMultiLine);
        setText();

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
                        /*Bundle b = new Bundle();
                        b.putInt("id", 1);
                        Intent intent = new Intent(ViewInforPage.this, MainActivity.class);
                        intent.putExtras(b);
                        startActivity(intent);*/
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.create();
                dialog.show();
                //TODO: Delete will delete data from DB
            }
        });
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
                startActivity(intent);
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void setText(){
        etName.setText(getIntent().getExtras().getString("name"));
        etNickname.setText(getIntent().getExtras().getString("nickname"));
        etBirthday.setText(getIntent().getExtras().getString("date"));
        etAddition.setText(getIntent().getExtras().getString("addition"));

    }
}