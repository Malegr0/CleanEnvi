package com.example.cleanenvi;

import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

    private void showData(){
        dataTxt = findViewById(R.id.dataTxt);
        Log.d(TAG, "showData: Displaying data in the TextView.");

        //get the data and append to a buffer
        Cursor data = mDBHelper.getData();

        StringBuffer buffer = new StringBuffer();
        while(data.moveToNext()){
            buffer.append("Name: " + data.getString(0) + "\n");
            buffer.append("ReID: " + data.getString(1) + "\n");
        }
        dataTxt.setText(buffer.toString());
    }

    //Nachrichten für den Nutzer
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
