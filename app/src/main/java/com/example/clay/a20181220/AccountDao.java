package com.example.clay.a20181220;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.clay.a20181220.Account;

public class AccountDao
{
    private MyHelper helper;

    public AccountDao(Context context)
    {
        helper = new MyHelper(context); // build dao, build helper object
    }

    // insert
    public void insert(Account account)
    {
        SQLiteDatabase db = helper.getWritableDatabase(); // load database
        ContentValues values = new ContentValues(); // put in the insert data by ContentValues object
        values.put("datetime", account.getDatetime());
        values.put("info", account.getInfo());
        values.put("value", account.getValue());

        long id = db.insert("account", null, values); // insert data
        account.setId(id);
        db.close();
    }

    // delete
    public int delete(long id)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete("account", "_id=?", new String[]{id+""}); // delete the list data
        db.close();

        return count;
    }

    // refresh
    public int update(Account account)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("datetime", account.getDatetime());
        values.put("info", account.getInfo());
        values.put("value", account.getValue());

        int count = db.update("account", values, "_id=?", new String[]{ account.getId()+""}); // refresh
        db.close();
        return count;
    }

    // query all data
    public List<Account> queryAll()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query("account", null, null, null, null, null, "value DESC");
        List<Account> list = new ArrayList<Account>();

        while(c.moveToNext())
        {
            // index by list number
            long id = c.getLong(c.getColumnIndex("_id"));
            String datetime = c.getString(1);
            String info = c.getString(2);
            int value = c.getInt(3);

            list.add(new Account(id, datetime, info, value));
        }

        c.close();
        db.close();

        return list;
    }
}