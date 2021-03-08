package com.example.cleanenvi.helpers.history;

import android.content.Context;

import java.io.IOException;

public class HistoryManager {

    private static History[] histories;

    public static void init() {
        histories = new History[5];
        histories[0] = new History("", "", "");
        histories[1] = new History("", "", "");
        histories[2] = new History("", "", "");
        histories[3] = new History("", "", "");
        histories[4] = new History("", "", "");
    }

    public static void addNewHistory(History history) {
        boolean productAlreadyInHistory = false;
        for(History h: histories) {
            if(h.getEan().equals(history.getEan())) {
                productAlreadyInHistory = true;
                break;
            }
        }
        if(!productAlreadyInHistory) {
            histories[4] = histories[3];
            histories[3] = histories[2];
            histories[2] = histories[1];
            histories[1] = histories[0];
            histories[0] = history;
        }
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
