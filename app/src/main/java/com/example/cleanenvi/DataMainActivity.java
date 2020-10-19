package com.example.cleanenvi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DataMainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    DBHelper mDatabaseHelper;
    Button btnAdd, btnViewData, btnEdit;
    EditText editText, editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_main);

        editText = findViewById(R.id.column1Edit);
        editText2 = findViewById(R.id.column2Edit);
        btnAdd = findViewById(R.id.addBtn);
        btnViewData = findViewById(R.id.viewBtn);
        btnEdit = findViewById(R.id.editBtn);
        mDatabaseHelper = new DBHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String newEntry = editText.getText().toString();
                if (editText.length() != 0) {
                    AddData(editText.getText().toString(), editText2.getText().toString());
                    editText.setText("");
                    editText2.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }
            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataMainActivity.this, ShowDataActivity.class);
                startActivity(intent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataMainActivity.this.startActivity(new Intent((Context)DataMainActivity.this, EditDataActivity.class));
            }
        });
    }

    public void AddData(String newEntry, String newEntry2){
        boolean insertData = mDatabaseHelper.addData(newEntry, newEntry2);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    /**
     * customizable toast
     * @param message
     */

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
