package com.example.cleanenvi;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//TODO: Diese Activity kann komplett entfernt werden
public class EditDataActivity extends AppCompatActivity {
    private static final String TAG = "EditDataActivity";
    Button btnSave, btnDelete;
    EditText changeEdit;
    DBHelper mDBHelper;
    String selectedName;
    String delName;
    int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_edit);
        btnSave = findViewById(R.id.saveBtn);
        btnDelete = findViewById(R.id.delBtn);
        changeEdit = findViewById(R.id.changeEdit);
        mDBHelper = new DBHelper(this);

        changeEdit.setText(selectedName);

        //Button für das Ändern von Werten, muss irgendwann angepasst werden falls noch benötigt
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String change = changeEdit.getText().toString();
                if(!change.equals("")) {
                    mDBHelper.updateName(change,selectedID,selectedName);
                } else {
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

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
