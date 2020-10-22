package com.example.cleanenvi;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;




// JSON Einführung https://www.codeflow.site/de/article/java-org-json
public class Openfoodfacts {

    public static void main(String EAN) {
        //String EAN;

        //EAN = "4250350530047";
        //4008404001018

        //URL für Abfrage erstellen
        String query_url = makeURL(EAN);

        //URL an API senden und JSON empfangen
        String JSONResponsevollstaendig = getJSON(query_url);

        //JSON alle " gegen % tauschen
        String ResponseMSt = replaceResponse(JSONResponsevollstaendig);
        System.out.println(ResponseMSt);

        // JSON aufteilen in einzelne Strings mit den gewünschten Daten
        // Wenn EAN nicht gefunden wurde, wird eine entsprechende Meldung gegeben. Dafür wird als erstes der "Status" in der JSON gesucht und ausgwertet.
        String ProductAvailable = checkResponse(ResponseMSt);
        System.out.println("Produkt vorhanden:_" + ProductAvailable + "_");
        if (ProductAvailable.equals("0")) {
            System.out.println("Das Produkt ist noch nicht vorhanden, oder die EAN ist falsch.");
        }

        else {
            String Verpackungen = splitResponse(ResponseMSt, "packaging");
            String EANCode = splitResponse(ResponseMSt, "code");
            String Produktname = splitResponse(ResponseMSt, "product_name");
            String Marke = splitResponse(ResponseMSt, "brands");
            String[] Verpackung = splitInArray(Verpackungen);


            //System.out.println(Verpackungen);
            //System.out.println(EANCode);
            //System.out.println(Produktname);
            //System.out.println(Marke);
            //System.out.println("Verpackungen im Array ausgeben:");


            for (int i = 0; i < (Verpackung.length); i++) {

                System.out.println(Verpackung[i]);

            }
        }
    }


    public static String replaceResponse (String response) {
        char c = '%';
        String ResponseMSt = response.replace('"', c);
        System.out.println(ResponseMSt);
        return ResponseMSt;
    }

    public static String makeURL(String EAN) {
        String url, openfoodfactsurl;
        openfoodfactsurl = "https://world.openfoodfacts.org/api/v0/product/";
        url = openfoodfactsurl.concat(EAN);
        url = url.concat(".json");

        System.out.println("query_url= " + url);
        return url;
    }

    public static String getJSON(String query_url) {
        String JSONResponse = " ";

        try {
            URL obj = new URL(query_url);
            HttpURLConnection connget = (HttpURLConnection) obj.openConnection();

            int responseCode = connget.getResponseCode();
            System.out.println("Sending Get Request to URL: " + query_url);
            System.out.println("Response Code: " + responseCode);
            if (responseCode != 200) {
                System.out.println("Es gab einen Fehler bei der Verbindung, Fehlercode:" + responseCode);

            } else {
                BufferedReader in;
                in = new BufferedReader(
                        new InputStreamReader(connget.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                //System.out.println(response.toString());
                JSONResponse = response.toString();
                //System.out.println(JSONResponse);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return JSONResponse;
    }

    public static String checkResponse(String ResponseMSt) {

        //Such String erstellen
        String searchString = "status%:";

        System.out.println("SearchString: " + searchString);
        System.out.println(ResponseMSt);
        String Verpackung = "#";


        //Vorne abschneiden

        for (int i = 0; i < (ResponseMSt.indexOf(searchString)); i++) {
            Verpackung = ResponseMSt.substring(ResponseMSt.indexOf(searchString) + searchString.length());
        }
        //System.out.println(Verpackung);
        //hinten abschneiden

        String[] arrOfStr = Verpackung.split(",");
        Verpackung = arrOfStr[0];
        //System.out.println(Verpackung);
        return Verpackung;
    }

    public static String splitResponse(String ResponseMSt, String suchString) {

        //Such String erstellen
        String searchString = "%";
        searchString = searchString.concat(suchString);
        searchString = searchString.concat("%:%");
        String Verpackung = "#";


        //Vorne abschneiden

        for (int i = 0; i < (ResponseMSt.indexOf(searchString)); i++) {
            Verpackung = ResponseMSt.substring(ResponseMSt.indexOf(searchString) + searchString.length());
        }
        //System.out.println(Verpackung);
        //hinten abschneiden

        String[] arrOfStr = Verpackung.split("%");
        Verpackung = arrOfStr[0];
        //System.out.println(Verpackung);
        return Verpackung;
    }

    public static String[] splitInArray(String verpackung_string) {
        String[] verpackung_array;


        verpackung_array = verpackung_string.split(",");

        return verpackung_array;
    }

}
