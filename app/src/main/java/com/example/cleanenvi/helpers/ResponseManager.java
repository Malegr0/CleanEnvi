package com.example.cleanenvi.helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ResponseManager {

    public static String[] getProductData(String ean) throws IOException, JSONException {
        //shorten response to fit JSONObject
        String responseStr = URLManager.getProduct(ean);
        if(responseStr != null) {
            responseStr = responseStr.replaceAll("[\\[\\]]", "");
            JSONObject jObj = new JSONObject(responseStr);
            //put jObj values into response array
            String[] response = new String[6];
            response[0] = jObj.getString("ean");
            response[1] = jObj.getString("name");
            response[2] = jObj.getString("imageurl");
            if(!jObj.getString("packaging").equals("")) {
                response[3] = jObj.getString("packaging");
            } else {
                response[3] = null;
            }
            response[4] = jObj.getString("brand");
            response[5] = jObj.getString("recnumber");
            return response;
        } else {
            return null;
        }
    }

    public static String getRecIDData(String packaging) throws IOException, JSONException {
        String responseStr = URLManager.getRecID(packaging).replaceAll("[\\[\\]]", "");
        JSONObject jObj = new JSONObject(responseStr);
        return jObj.getString("recid");
    }

}
