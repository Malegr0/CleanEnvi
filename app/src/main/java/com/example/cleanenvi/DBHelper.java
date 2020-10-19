package com.example.cleanenvi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "Verpackungen";
    private static final String COL1 = "name";
    private static final String COL2 = "ReID";

    EditText editable_item;


    public DBHelper(Context context){
        super(context, TABLE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (name TEXT PRIMARY KEY, " + COL2 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String Name, String ReID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues dbValues = new ContentValues();
        dbValues.put(COL1, Name);
        dbValues.put(COL2, ReID);

        Log.d(TAG, "addData: Adding " + Name + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, dbValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Daten von der Datenbank abfragen
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //ID vom angegebenen Namen erhalten (nicht wirklich für Projekt wichtig)(noch vom Tutorial aber drinlassen falls irgendwann wichtig)
    public Cursor getItemID(String Name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + Name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //Ändert Namen in der Datenbank
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    //Löscht Eintrag der Tabelle abhängig vom Namen
    public void deleteName(String Name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + Name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + Name + " from database.");
        db.execSQL(query);
    }
}
