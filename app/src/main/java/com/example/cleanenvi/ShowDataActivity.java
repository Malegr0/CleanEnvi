package com.example.cleanenvi;

import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowDataActivity extends AppCompatActivity {
    private static final String TAG = "ShowDataActivity";
    DBHelper mDBHelper;
    TextView dataTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_ausgabe);
        mDBHelper = new DBHelper(this);

        showData();
    }

    //Zeigt alle Daten die in der Datenbank stehen an
    private void showData(){
        dataTxt = findViewById(R.id.dataTxt);
        Log.d(TAG, "showData: Displaying data in the TextView.");
        Cursor data = mDBHelper.getAllData();

        StringBuilder buffer = new StringBuilder();
        while(data.moveToNext()){
            buffer.append("Name: ").append(data.getString(0)).append("\n");
            buffer.append("ReID: ").append(data.getString(1)).append("\n");
        }
        dataTxt.setText(buffer.toString());
    }
}
