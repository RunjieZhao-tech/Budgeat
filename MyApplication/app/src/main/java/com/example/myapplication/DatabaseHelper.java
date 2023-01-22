package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static SQLiteOpenHelper mInstance;
    public static synchronized SQLiteOpenHelper getmInstance(Context context){
        if(mInstance == null){
            mInstance = new DatabaseHelper(context,"runjieDB.db",null,1);
        }
        return mInstance;
    }

    public DatabaseHelper(Context context,String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //input data
        String sql = "create table persons(_id integer, title text, sub_title text, ppl INT, price double,image text,meal_type text,CHECK(meal_type='Breakfast' OR meal_type='Dinner' OR meal_type='Lunch' OR meal_type='Snack'))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}
}
