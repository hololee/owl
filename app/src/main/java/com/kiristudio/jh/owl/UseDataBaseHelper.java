package com.kiristudio.jh.owl;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lee on 2015-08-01.
 */
public class UseDataBaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase Mdb;

    public String TABLE_NAME = "usage";

    public UseDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public UseDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // id, 요일 ,날짜, 사용시간
        db.execSQL("create table if not exists " + TABLE_NAME + " (_id integer PRIMARY KEY autoincrement, Day integer, Date string, UsedTime long)");


        db.execSQL("insert into " + TABLE_NAME + "(Day, Date, UsedTime) values (1, '일', 10000)");
        db.execSQL("insert into " + TABLE_NAME + "(Day, Date, UsedTime) values (2, '월', 10000)");
        db.execSQL("insert into " + TABLE_NAME + "(Day, Date, UsedTime) values (3, '화', 10000)");
        db.execSQL("insert into " + TABLE_NAME + "(Day, Date, UsedTime) values (4, '수', 10000)");
        db.execSQL("insert into " + TABLE_NAME + "(Day, Date, UsedTime) values (5, '목', 10000)");
        db.execSQL("insert into " + TABLE_NAME + "(Day, Date, UsedTime) values (6, '금', 10000)");
        db.execSQL("insert into " + TABLE_NAME + "(Day, Date, UsedTime) values (7, '토', 10000)");




        // db.execSQL("update " +TABLE_NAME + " set Day = '0', Date = '8/3' , UsedTime = '10000' where Day = 'Mon'");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
