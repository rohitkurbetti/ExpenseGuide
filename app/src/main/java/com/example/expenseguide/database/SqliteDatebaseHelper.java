package com.example.expenseguide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.expenseguide.MainActivity;

public class SqliteDatebaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expenses.db";
    private static final String TABLE_NAME = "expenses_table";
    private static final String COL_ID = "ID";
    private static final String COL_CATEGORY = "CATEGORY";
    private static final String COL_PARTICULARS = "PARTICULARS";
    private static final String COL_COST_INCURRED = "COST_INCURRED";
    private static final String COL_SUFFICIENT_COST = "SUFFICIENT_COST";

    private static final String COL_COST_DIFFERENCE = "COST_DIFFERENCE";
    private static final String COL_DATE_TIME = "DATE_TIME";

    private static final String COL_DATE = "DATE";

    public SqliteDatebaseHelper(MainActivity context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CATEGORY + " TEXT, " +
                COL_PARTICULARS + " TEXT, " +
                COL_COST_INCURRED + " REAL, " +
                COL_SUFFICIENT_COST + " REAL, " +
                COL_COST_DIFFERENCE + " REAL, " +
                COL_DATE_TIME + " TEXT," +
                COL_DATE + " DATE)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String category, String particulars, double costIncurred, double sufficientCost, double costDifference, String dateTime, String dateStr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CATEGORY, category);
        contentValues.put(COL_PARTICULARS, particulars);
        contentValues.put(COL_COST_INCURRED, costIncurred);
        contentValues.put(COL_SUFFICIENT_COST, sufficientCost);
        contentValues.put(COL_COST_DIFFERENCE, costDifference);
        contentValues.put(COL_DATE_TIME, dateTime);
        contentValues.put(COL_DATE, dateStr);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }


    public Cursor fetchReportsDateRange(String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = {startDate, endDate};
        Cursor cursor =  db.rawQuery("select * from '"+TABLE_NAME+"' " +
                "where "+COL_DATE+" >= '"+startDate+"' and "+COL_DATE+" <= '"+endDate+"' ", null);

        return cursor;
    }

    public Cursor getAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from '"+TABLE_NAME+"' ", null);
    }
}
