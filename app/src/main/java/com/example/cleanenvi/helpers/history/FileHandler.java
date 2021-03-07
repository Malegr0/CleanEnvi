package com.example.cleanenvi.helpers.history;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileHandler {

    //saves Date to file, if file does not exist, it creates the file
    public static void saveData(Context context, History[] histories) throws IOException {
        FileOutputStream outputStream = context.openFileOutput("history", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String s = gson.toJson(histories);
        outputStream.write(s.getBytes());
        outputStream.close();
    }

    //opens file and reads data
    public static History[] loadData(Context context) throws IOException, ClassNotFoundException {
        FileInputStream inputStream = context.openFileInput("history");
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();
        Gson gson = new Gson();
        return gson.fromJson(json, History[].class);
    }

}
