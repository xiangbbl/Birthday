package com.example.birthday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String dbName = "MainDB";
    private static final String Table_Name = "Birthday";
    private static final String Name = "name";
    private static final String Nickname = "nickname";
    private static final String Birthday = "birthday";
    private static final String Add_info = "add_info";


    public DatabaseHelper(@Nullable Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table if not exists " + Table_Name +
                " (name varchar(64), " +
                "nickname varchar(64), " +
                "birthday varchar(64), " +
                "add_info varchar(255))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + Table_Name);
        onCreate(db);
    }

    public boolean addData(String mName, String mNickname, String mDate, String mInfo) {
        //System.out.println(mName + mNickname + mDate + mInfo + "!!!!!!!!!!!!!!!!!!!!!!!");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Name, mName);
        contentValues.put(Nickname, mNickname);
        contentValues.put(Birthday, mDate);
        contentValues.put(Add_info, mInfo);
        long ins = db.insert(Table_Name, null, contentValues);
        db.close();
        if (ins == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor loadData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select name, birthday from Birthday order by name ASC";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor loadRecData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select name, birthday from Birthday order by birthday ASC";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor retData(String name, String birth){
        String qName = "\"" + name + "\"";
        String qBirth = "\"" + birth + "\"";
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from Birthday where name = " + qName + " and birthday = " + qBirth;
        cursor = db.rawQuery(query, null);
        return cursor;
    }
    public void delete(String name, String birth, String Nickname){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Birthday, "name = ? and birthday = ? and nickname = ?", new String[]{name, birth, Nickname});
        db.close();
    }
    public void upDate(){

    }
}



