package com.example.venkatmugesh.hallprocv20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RegisterDB extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "Register_Table";

    private static final String COL1 = "ID";

    private static final String COL2 = "secondyear";
    private static final String COL3 = "thirdyear";
    private static final String COL4 = "finalyear";

    public RegisterDB(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +"(" + COL1  +" INTEGER PRIMARY KEY AUTOINCREMENT, " +

                COL2 +" INTEGER, " + COL3 + " INTEGER, " + COL4 + "INTEGER)";


        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public boolean addData(double firYear , double secYear , double finYear ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2, firYear);
        contentValues.put(COL3, secYear);
        contentValues.put(COL4 , finYear);
        long result = db.insert(TABLE_NAME, null, contentValues);


        if (result == -1) {

            return false;

        } else {

            return true;

        }

    }

    public Cursor getData(){

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME;

        Cursor data = db.rawQuery(query, null);

        return data;

    }
}
