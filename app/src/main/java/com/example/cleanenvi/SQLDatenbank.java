package com.example.cleanenvi;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SQLDatenbank extends AppCompatActivity {


    Button btnload, btnadd, btnfind, btndelete, btnupdate;
    TextView Ausgabe1;

    EditText studentid, studentname;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.database_test);
        this.setTitle((CharSequence) "Datenbank bearbeiten");

        //speichert Ergebnis der Eingabe aus der anderen Activity

        Ausgabe1 = findViewById(R.id.Ausgabe1);
        studentid = findViewById(R.id.AttributMaterialNummer);
        studentname = findViewById(R.id.AttributProduktName);
        btnadd = findViewById(R.id.buttonAdd) ;
        btndelete = findViewById(R.id.buttonDel);
        btnfind = findViewById(R.id.buttonFind);
        btnload = findViewById(R.id.buttonLoadData);
        btnupdate = findViewById(R.id.buttonUpdate);

        // Methodenaufruf für API-Anfrage
        //new ProductShowActivity.OpenFoodFacts().execute();

        //Events
        btnadd.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                SQLDatenbank.this.startActivity(new Intent((Context)SQLDatenbank.this, MyDBHandler.class));
            }});
        btnupdate.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                SQLDatenbank.this.startActivity(new Intent((Context)SQLDatenbank.this, MyDBHandler.class));
            }});
        btnload.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                SQLDatenbank.this.startActivity(new Intent((Context)SQLDatenbank.this, MyDBHandler.class));
            }});
        btnfind.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                SQLDatenbank.this.startActivity(new Intent((Context)SQLDatenbank.this, MyDBHandler.class));
            }});
        btndelete.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                SQLDatenbank.this.startActivity(new Intent((Context)SQLDatenbank.this, MyDBHandler.class));
            }});
    }


    public void loadStudents(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Ausgabe1.setText(dbHandler.loadHandler());
        studentid.setText("");
        studentname.setText("");
    }

    //ADD Button
    public void addStudent(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        int id = Integer.parseInt(studentid.getText().toString());
        String name = studentname.getText().toString();
        Student student = new Student(id, name);
        dbHandler.addHandler((student));
        studentid.setText("");
        studentname.setText("");
    }

    // FIND Button
    public void findStudent(View view) {

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Student student = dbHandler.findHandler(studentname.getText().toString());
        if (student != null) {
            Ausgabe1.setText(String.valueOf(student.getID()) + " " + student.getStudentName() + System.getProperty("line.separator"));
            studentid.setText("");
            studentname.setText("");
        } else {
            Ausgabe1.setText("No Match found");
            studentid.setText("");
            studentname.setText("");
        }
    }

    // DELETE Button
    public void removeStudent (View view) {

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        boolean result = dbHandler.deleteHandler(Integer.parseInt(studentid.getText().toString()));
        if (result) {
            studentid.setText("");
            studentname.setText("");
            Ausgabe1.setText("Record Deleted");
        } else {
            Ausgabe1.setText("No Match Found");
        }
    }

    // UPDATE Button
    public void updateStudent (View view) {

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        boolean result = dbHandler.updateHandler(Integer.parseInt(studentid.getText().toString()), studentname.getText().toString());
        if (result) {
            studentid.setText("");
            studentname.setText("");
            Ausgabe1.setText("Record Updated");
        }else {
            Ausgabe1.setText("No Match Found");
        }
    }

}
