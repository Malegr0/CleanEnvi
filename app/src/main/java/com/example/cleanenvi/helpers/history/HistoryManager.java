package com.example.cleanenvi.helpers.history;

import android.content.Context;

import java.io.IOException;

public class HistoryManager {

    private static History[] histories;

    public static void init() {
        histories = new History[5];
    }

    public static void addNewHistory(History history) {
        histories[4] = histories[3];
        histories[3] = histories[2];
        histories[2] = histories[1];
        histories[1] = histories[0];
        histories[0] = history;
    }

    public static History[] getHistories() {
        return histories;
    }

    public static void saveHistories(Context context) throws IOException {
        FileHandler.saveData(context, histories);
    }

    public static void loadHistories(Context context) throws IOException, ClassNotFoundException {
        histories = FileHandler.loadData(context);
    }

}
