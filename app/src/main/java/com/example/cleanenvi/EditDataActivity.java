package com.example.cleanenvi;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    Button btnSave, btnDelete;
    EditText changeEdit;

    DBHelper mDBHelper;

    private String selectedName;
    String delName;
    private int selectedID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_edit);

        btnSave = findViewById(R.id.saveBtn);
        btnDelete = findViewById(R.id.delBtn);
        changeEdit = findViewById(R.id.changeEdit);
        mDBHelper = new DBHelper(this);

        //get the intent extra from the ListDataActivity
        //Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        //selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //now get the name we passed as an extra
        //selectedName = receivedIntent.getStringExtra("name");

        //set the text to show the current selected name
        changeEdit.setText(selectedName);

        //Testing own idea

        //Button für das Ändern von Werten, nochmals überprüfen, funktioniert nicht mehr
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String change = changeEdit.getText().toString();
                if(!change.equals("")){
                    mDBHelper.updateName(change,selectedID,selectedName);
                } else{
                    toastMessage("You must enter a name");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delName = changeEdit.getText().toString();
                Log.d(TAG, "Delete: query: " + delName);
                mDBHelper.deleteName(delName);
                changeEdit.setText("");
                toastMessage("removed from database");
            }
        });
    }

    //Nachrichten für den Nutzer
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
