package com.saransh.smartsupper10.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "smartsupper";
 	private static final String TABLE_REGISTER = "register";
 
    private static final String KEY_Contact = "contact";
    private static final String KEY_Address = "address";

 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_REGISTER + "("
                + KEY_Contact + " INTEGER PRIMARY KEY,"
                + KEY_Address + " TEXT"
                + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    	db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);
        onCreate(db);
    }
 
    public void addUser(String contact, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_Contact, contact);
        values.put(KEY_Address, address);
 
        db.insert(TABLE_REGISTER, null, values);
        db.close();
    }
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_REGISTER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("contact", cursor.getString(0));
            user.put("address", cursor.getString(1));
        }
        Log.d("contact",cursor.getString(0));
        Log.d("address",cursor.getString(1));
        cursor.close();
        db.close();

        return user;
    }
 
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REGISTER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.close();
        db.close();
         
        return rowCount;
    }
    
    public void resetTable_Register(){
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.delete(TABLE_REGISTER, null, null);
        db.close();
    }
    
    public void resetTable_Notices() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	db.delete(TABLE_REGISTER, null, null);
    	db.close();
    }
}
