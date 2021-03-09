package com.example.cleanenvi.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLManager {

    private static final String SERVER_ADDRESS = "https://b15f6255307e.ngrok.io";
    private static final String PRODUCTS_ADDRESS = SERVER_ADDRESS + "/products/";
    private static final String PACKAGES_ADDRESS = SERVER_ADDRESS + "/packages/";
    private static final String NEWSFEED_ADDRESS = SERVER_ADDRESS + "/newsfeed/";
    private static final String MARKERS_ADDRESS = SERVER_ADDRESS + "/markers/";

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
        } else if(conn.getResponseCode() == 204) {
            response = null;
        }
        conn.disconnect();
        return response;
    }

    public static String getProduct(String ean) throws IOException {
        if(checkForInteger(ean)) {
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
            } else if(conn.getResponseCode() == 404) {
                response = null;
            }
            conn.disconnect();
            return response;
        } else {
            return null;
        }
    }

    public static String getNewsfeed(int id) throws IOException {
        URL url = new URL(NEWSFEED_ADDRESS + id);
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
        } else if(conn.getResponseCode() == 404) {
            response = null;
        }
        conn.disconnect();
        return response;
    }

    public static String getNewsfeedRows() throws IOException {
        URL url = new URL(NEWSFEED_ADDRESS);
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
        } else if(conn.getResponseCode() == 204) {
            response = null;
        }
        conn.disconnect();
        return response;
    }

    public static String getAllMarkers() throws IOException {
        URL url = new URL(MARKERS_ADDRESS);
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
        } else if(conn.getResponseCode() == 204) {
            response = null;
        }
        conn.disconnect();
        return response;
    }

    private static boolean checkForInteger(String ean) {
        if(ean.isEmpty()) return false;
        for(int i = 0; i < ean.length(); i++) {
            if(i == 0 && ean.charAt(i) == '-') {
                if(ean.length() == 1) return false;
                else continue;
            }
            if(Character.digit(ean.charAt(i),10) < 0) return false;
        }
        return true;
    }
}
