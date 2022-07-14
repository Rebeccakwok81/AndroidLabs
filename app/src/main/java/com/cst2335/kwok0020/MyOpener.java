package com.cst2335.kwok0020;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class MyOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "MessageDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "Messages";
    public final static String COL_ID = "_id";
    public final static String COL_MESSAGE = "Text";
    public static final String COL_SEND_RECEIVE = "SendOrReceive";

    public MyOpener(Context context){
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }


    // should be the creation statement
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table MyData ( _id INTEGER PRIMARY KEY AUTOINCREMENT, Message TEXT, SendOrReceive INTEGER);
        String result = String.format(" %s %s", "FirstString" , "10");

        //                                      //TABLE_NAME               take care of id numbers
        db.execSQL( String.format( "Create table %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s  INTEGER);"
                                       , TABLE_NAME, COL_ID,                       COL_MESSAGE, COL_SEND_RECEIVE ) );
    }

    // delete current table, create a new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "Drop table if exists " + TABLE_NAME ); //deletes the current data
        //create a new table:

        this.onCreate(db); //calls function on line 26
    }

}