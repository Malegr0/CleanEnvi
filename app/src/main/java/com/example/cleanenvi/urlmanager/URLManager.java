package com.example.cleanenvi.urlmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class URLManager {

    private static final String  SERVER_ADDRESS = "malegro.ddns.net:8080";
    private static final String  PRODUCTS_ADDRESS = SERVER_ADDRESS + "/products/";
    private static final String  PACKAGES_ADDRESS = SERVER_ADDRESS + "/packages/";
    //private static final String  NEWSFEED_ADDRESS = SERVER_ADDRESS + "/newsfeed/";

    public static int getRecID(String packaging) throws IOException {
        //Setting up the connection
        URL url = new URL(PACKAGES_ADDRESS + packaging);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestMethod("GET");

        //Check response code
        if(http.getResponseCode() == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String responseLine;
            StringBuilder response = new StringBuilder();
            while((responseLine = br.readLine()) != null) {
                response.append(responseLine);
            }
            br.close();
            System.out.println(response);
            return 1;
        } else if(http.getResponseCode() == 204) {
            //Package is not in the database
            return -1;
        } else {
            //Something went wrong
            return -2;
        }
    }

    public static void getProduct() {

    }

}
