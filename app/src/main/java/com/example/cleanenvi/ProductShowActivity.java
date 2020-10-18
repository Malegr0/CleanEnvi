package com.example.cleanenvi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductShowActivity extends AppCompatActivity {

    String EAN;
    Button backBtn;
    TextView resultTxt;
    ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_show);
        this.setTitle((CharSequence)"Manuelle Produktsuche per Nummer");

        //speichert Ergebnis der Eingabe aus der anderen Activity
        EAN = ProductSearchActivity.EAN;
        backBtn = findViewById(R.id.back);

        // Methodenaufruf für API-Anfrage
        new OpenFoodFacts().execute();

        //setzt User beim Betätigen des Zurück-Buttons wieder zur Eingabe
        backBtn.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                ProductShowActivity.this.startActivity(new Intent((Context)ProductShowActivity.this, ProductSearchActivity.class));
            }
        });

    }

    //Hintergrund-Thread für API-Aufruf
    private class OpenFoodFacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            main(EAN);
            return null;
        }

    }

    //Methode für API-Anfrage
    public void main(String EAN) {

        imageView = findViewById(R.id.productImageView);
        resultTxt = findViewById(R.id.showResulttxt);
        backBtn = findViewById(R.id.back);

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
            final String Bild = splitResponse(ResponseMSt, "image_url");
            final String[] Verpackung = splitInArray(Verpackungen);


            /*System.out.println("String vom Bild: " + Bild);
            System.out.println(EANCode);
            System.out.println(Produktname);
            System.out.println(Marke);
            System.out.println("Verpackungen im Array ausgeben:")*/

            //Änderungen die im MainThread ausgeführt werden müssen
            runOnUiThread(new Runnable() {

                @SuppressLint("SetTextI18n")
                @Override
                public void run() {

                    //zeigt dem User Bild des Produkts  an
                    Picasso.get().load(Bild).resize(300,300).into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                    resultTxt.setText("Verpackungen: " + Verpackungen + "\n\n" + "EANCode: " + EANCode + "\n\n" + "Produkt: " + Produktname + "\n\n" + "Marke: " + Marke);
                }
            });

            //Verpackung im Array anzeigen lassen
            for (int i = 0; i < (Verpackung.length); i++) {

                System.out.println(Verpackung[i]);

            }
        }
    }

    //Gänsefüßchen ersetzen für bessere Weiterverarbeitung
    public static String replaceResponse (String response) {
        char c = '%';
        String ResponseMSt = response.replace('"', c);
        System.out.println(ResponseMSt);
        return ResponseMSt;
    }

    //URL für Anfrage zusammensetzen
    public static String makeURL(String EAN) {
        String url, openfoodfactsurl;
        openfoodfactsurl = "https://world.openfoodfacts.org/api/v0/product/";
        url = openfoodfactsurl.concat(EAN);
        url = url.concat(".json");

        System.out.println("query_url= " + url);
        return url;
    }

    //URL an API senden und Ergebnis erhalten
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
                JSONResponse = response.toString();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return JSONResponse;
    }

    //Ergebnis der Anfrage überprüfen
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
        //hinten abschneiden

        String[] arrOfStr = Verpackung.split(",");
        Verpackung = arrOfStr[0];
        return Verpackung;
    }

    //besondere Ergebnisse mit Such-Strings erhalten können
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
        //hinten abschneiden

        String[] arrOfStr = Verpackung.split("%");
        Verpackung = arrOfStr[0];
        return Verpackung;
    }

    //Verpackung als Array speichern
    public static String[] splitInArray(String verpackung_string) {
        String[] verpackung_array;


        verpackung_array = verpackung_string.split(",");

        return verpackung_array;
    }

}
