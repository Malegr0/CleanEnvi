package com.example.cleanenvi;

import androidx.appcompat.app.AppCompatActivity;

public class Student extends AppCompatActivity {
    //fields
    private String studentName;
    private int studentID;

    //constructors
public Student(){}
public Student(int id, String studentname) {




        this.studentID = id;
        this.studentName = studentname;
    }

    //properties
    public void setID(int id) {
        this.studentID = id;
    }

    public int getID() {
        return this.studentID;
    }

    public void setStudentName(String studentname) {
        this.studentName = studentname;
    }

    public String getStudentName() {
        return this.studentName;
    }

}
