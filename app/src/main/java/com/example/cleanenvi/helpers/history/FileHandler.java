package com.example.cleanenvi.helpers.history;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileHandler {

    //saves Date to file, if file does not exist, it creates the file
    public static void saveData(Context context, History[] histories) throws IOException {
        FileOutputStream outputStream = context.openFileOutput("history", Context.MODE_PRIVATE);
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(histories);
        out.close();
    }

    //opens file and reads data
    public static History[] loadData(Context context) throws IOException, ClassNotFoundException {
        FileInputStream inputStream = context.openFileInput("history");
        ObjectInputStream in = new ObjectInputStream(inputStream);
        History[] histories = (History[]) in.readObject();
        in.close();
        return histories;
    }

}
