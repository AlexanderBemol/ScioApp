package com.nordokod.scio.old.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAux extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "Bita.db";
    public final static int DATABASE_VERSION = 1;

    public DBAux(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + StarsHistory.TABLE_NAME + " (" +
                StarsHistory.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StarsHistory.GUIDE + " TEXT NOT NULL ," +
                StarsHistory.QUESTION + " INTEGER ," +
                StarsHistory.STARS + " INTEGER ," +
                StarsHistory.TIMESTAMP + " TEXT NOT NULL ," +
                StarsHistory.SYNC + " INTEGER ," +
                StarsHistory.MODE + " INTEGER ," +
                "UNIQUE(" + StarsHistory.ID + "))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
