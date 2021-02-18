package com.example.cleanenvi.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLManager {

    private static final String  SERVER_ADDRESS = "http://malegro.ddns.net:8080";
    private static final String  PRODUCTS_ADDRESS = SERVER_ADDRESS + "/products/";
    private static final String  PACKAGES_ADDRESS = SERVER_ADDRESS + "/packages/";
    //private static final String  NEWSFEED_ADDRESS = SERVER_ADDRESS + "/newsfeed/";

    public static String getRecID(String packaging) throws IOException {
        URL url = new URL(PACKAGES_ADDRESS + packaging);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        String response = "";
        if(conn.getResponseCode() == 200) {
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while((inputLine = bf.readLine()) != null) {
                response = response + inputLine;
            }
            bf.close();
        }
        conn.disconnect();
        return response;
    }

    public static String getProduct(String ean) throws IOException {
        URL url = new URL(PRODUCTS_ADDRESS + ean);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        String response = "";
        if(conn.getResponseCode() == 200) {
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while((inputLine = bf.readLine()) != null) {
                response = response + inputLine;
            }
            bf.close();
        }
        conn.disconnect();
        return response;
    }

}
