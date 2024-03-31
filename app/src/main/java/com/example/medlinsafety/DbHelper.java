package com.example.medlinsafety;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SafetyDb.db";
    public static final String Profile_TABLE_NAME = "userprofile";
    public static final String SETTING_TABLE_NAME = "settings";
    public static final String LOG_TABLE_NAME = "logs";
    private HashMap hp;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table if not exists userprofile (user_id integer primary key, user_name text,phone text,email text, user_address text,id_no text, image blob)");
        db.execSQL("create table if not exists settings (setting_id integer primary key, emergency_no text,phrase text,is_login text)");
        db.execSQL("create table if not exists logs (log_id integer primary key, log_date text,log_no text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS userprofile");
        db.execSQL("DROP TABLE IF EXISTS settings");
        db.execSQL("DROP TABLE IF EXISTS settings");
        onCreate(db);
    }
    public boolean insertUser (String name, String phone, String email, String address,String id_no,byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("user_address", address);
        contentValues.put("id_no", id_no);
        contentValues.put("image", image);
        db.insert("userprofile", null, contentValues);
        return true;
    }
    public boolean updateUser (String name, String phone, String email, String address,String id_no,byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("user_address", address);
        contentValues.put("id_no", id_no);
        contentValues.put("image", image);
        db.update("userprofile", contentValues, "user_id = ? ", new String[] { "1" } );
        return true;
    }
    public boolean curdOperations (String query) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(query);
        return true;
    }

    public Cursor getData(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( query, null );
        return res;
    }

    public int numberOfRows(String table){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table);
        return numRows;
    }
    @SuppressLint("Range")
    public ArrayList<String>[] getAllLog() {
        ArrayList<String>[] array_list = new ArrayList[2];

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from logs", null );
        res.moveToFirst();
        array_list[0]=new ArrayList<String>();
        array_list[1]=new ArrayList<String>();
        while(res.isAfterLast() == false){
            array_list[0].add(res.getString(res.getColumnIndex("log_no")));
            array_list[1].add(res.getString(res.getColumnIndex("log_date")));
            res.moveToNext();
        }
        return array_list;
    }
}
