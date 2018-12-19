package com.example.clay.a20181220;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.clay.a20181220.MainActivity;

public class MyHelper extends SQLiteOpenHelper
{
    public MyHelper(Context context)
    {
        super(context, "product.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d("MyHelper","OnCreate");
        db.execSQL("create table account(_id integer primary key autoincrement, datetime varchar(20), info varchar(40), value integer)"); // id primary key, datetime, information, value
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d("MyHelper","OnUpgrade");
    }
}
