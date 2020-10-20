package com.example.cleanenvi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DataMainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    DBHelper mDBHelper;
    Button btnAdd, btnViewData, btnEdit;
    EditText nameEdit, idEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_main);

        nameEdit = findViewById(R.id.column1Edit);
        idEdit = findViewById(R.id.column2Edit);
        btnAdd = findViewById(R.id.addBtn);
        btnViewData = findViewById(R.id.viewBtn);
        btnEdit = findViewById(R.id.editBtn);
        mDBHelper = new DBHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String newEntry = editText.getText().toString();
                if (nameEdit.length() != 0) {
                    AddData(nameEdit.getText().toString(), idEdit.getText().toString());
                    nameEdit.setText("");
                    idEdit.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }
            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataMainActivity.this.startActivity(new Intent((Context)DataMainActivity.this, ShowDataActivity.class));
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataMainActivity.this.startActivity(new Intent((Context)DataMainActivity.this, EditDataActivity.class));
            }
        });
    }

    public void AddData(String Name, String ID){
        boolean insertData = mDBHelper.addData(Name, ID);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    //Nachrichten für den Nutzer
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
