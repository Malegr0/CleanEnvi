package com.example.cleanenvi;
//https://dzone.com/articles/create-a-database-android-application-in-android-s

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    // information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ProduktDB.db";
    public static final String TABLE_NAME = "Material";
    public static final String COLUMN_ID = "prodname";
    public static final String COLUMN_NAME = "entnum";

   /* private static final String DATABASE_NAME = "ProduktDB.db";
    public static final String TABLE_NAME = "ProduktMat";
    public static final String COLUMN_ID = "prodname";
    public static final String COLUMN_NAME = "entnum";*/

    // initialize the database
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    /*Create a table
    column 1 datatype,
    column 2 dadatype,
    .... */
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE" + TABLE_NAME + "(" + COLUMN_ID + "INTEGER PRIMARYKEY," + COLUMN_NAME + "TEXT )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    // select * from table
    public String loadHandler() {
        String result = " ";
        String query = " Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int result_0 = cursor.getInt(0);
            String result_1 = cursor.getString(1);
            result += String.valueOf(result_0) + " " + result_1 + System.getProperty("line separator");
        }
        cursor.close();
        db.close();
        return result;
    }


    public void addHandler(Produkt produkt) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, produkt.getName());
        values.put(COLUMN_NAME, produkt.getentnum());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public Produkt findHandler(String entnum) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_NAME + " = " + "'" + entnum + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Produkt produkt = new Produkt();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            produkt.setentnum(Integer.parseInt(cursor.getString(0)));
            produkt.setName(cursor.getString(1));
            cursor.close();
        } else {
            produkt = null;
        }
        db.close();
        return produkt;
    }


    public boolean deleteHandler(int ID) {


        boolean result = false;
        String query = " Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "= '" + String.valueOf(ID) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Produkt produkt = new Produkt();
        if (cursor.moveToFirst()) {
            produkt.setentnum(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_NAME, COLUMN_ID + "=?",
                    new String[]{
                            String.valueOf(produkt.getentnum())
                    });

            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }


    /*public boolean updateHandler(int ID, String name) {
        int gt;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID, ID);
        args.put(COLUMN_NAME, name);
                return db.update(TABLE_NAME, args, COLUMN_ID + "=" +ID, null)&gt; 0;
    }*/



}
