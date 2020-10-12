package com.example.cleanenvi;


import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.cleanenvi.Openfoodfacts;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public final class ProductSearchActivity extends AppCompatActivity {

    EditText productSearchEdit;
    TextView TextEAN;
    String EAN;
    Button searchBtn;
    TextView resultTxt;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_search);
        this.setTitle((CharSequence)"Manuelle Produktsuche per Nummer");


        productSearchEdit =  findViewById(R.id.productSearchEdit);
        TextEAN = findViewById(R.id.txtEAN);

        searchBtn = findViewById(R.id.searchbtn);

        watcher(productSearchEdit, searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                //TextEAN.setText(EAN);
                setContentView(R.layout.product_show);
                new OpenFoodFacts().execute();
            }
        });
    }

    void watcher(final EditText productSearchEdit, final Button searchBtn)
    {
        //final TextView txt = (TextView) findViewById(R.id.txtCounter);
        productSearchEdit.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                //txt.setText(productSearchEdit.length() + " / 160"); //This is my textwatcher to update character left in my EditText
                if(productSearchEdit.length() == 0)
                    searchBtn.setEnabled(false); //Button disabled
                else
                    searchBtn.setEnabled(true);  //Button enabled
                EAN = productSearchEdit.getText().toString();

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
        });
        if(productSearchEdit.length() == 0) searchBtn.setEnabled(false);//Am Anfang wird Button disabled
    }

    private class OpenFoodFacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            main(EAN);
            return null;
        }
    }
    public void main(String EAN) {
        //String EAN;

        //EAN = "4250350530047";
        //4008404001018
        resultTxt = findViewById(R.id.showResulttxt);

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
            final String Verpackungen = splitResponse(ResponseMSt, "packaging");
            final String EANCode = splitResponse(ResponseMSt, "code");
            final String Produktname = splitResponse(ResponseMSt, "product_name");
            final String Marke = splitResponse(ResponseMSt, "brands");
            final String[] Verpackung = splitInArray(Verpackungen);


            //System.out.println(Verpackungen);
            //System.out.println(EANCode);
            //System.out.println(Produktname);
            //System.out.println(Marke);
            //System.out.println("Verpackungen im Array ausgeben:");
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    resultTxt.setText("Verpackungen: " + Verpackungen + "\n\n" + "EANCode: " + EANCode + "\n\n" + "Produkt: " + Produktname + "\n\n" + "Marke: " + Marke);
                    // Stuff that updates the UI

                }
            });

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
