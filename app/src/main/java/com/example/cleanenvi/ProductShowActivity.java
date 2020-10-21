package com.example.cleanenvi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
    TextView testingTxt;
    ImageView imageView;
    DBHelper mDBHelper;

    String reID;
    String Entsorgung ="";


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_show);
        this.setTitle((CharSequence)"Manuelle Produktsuche per Nummer");
        mDBHelper = new DBHelper(this);


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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resultTxt.setText("Das Produkt ist noch nicht vorhanden, oder die EAN ist falsch.");
                }
            });

        }

        else {
            final String Verpackungen = splitResponse(ResponseMSt, "packaging");
            final String Packung = Verpackungen.toUpperCase();
            final String EANCode = splitResponse(ResponseMSt, "code");
            final String Produktname = splitResponse(ResponseMSt, "product_name");
            final String Marke = splitResponse(ResponseMSt, "brands");
            final String Bild = splitResponse(ResponseMSt, "image_url");
            final String[] Verpackung = splitInArray(Packung);



            /*System.out.println("String vom Bild: " + Bild);
            System.out.println(EANCode);
            System.out.println(Produktname);
            System.out.println(Marke);
            System.out.println("Verpackungen im Array ausgeben:")*/

            //Änderungen die im MainThread ausgeführt werden müssen
                //foo(testName, testID);
                //System.out.println("Dies ist ein Testlauf: " + testName.length + "Dies ein andere: " + testID.length);

            runOnUiThread(new Runnable() {

                @SuppressLint("SetTextI18n")
                @Override
                public void run() {

                    //zeigt dem User Bild des Produkts  an
                    Picasso.get().load(Bild).into(imageView);
                    //resize(500,500).
                    imageView.setVisibility(View.VISIBLE);

                    for (int i = 0; i < (Verpackung.length); i++) {
                        Cursor data = mDBHelper.getData(Verpackung[i]);
                        StringBuilder buffer = new StringBuilder();
                        while (data.moveToNext()) {
                            buffer.append(data.getString(1));
                        }
                        reID = buffer.toString();

                        switch (reID) {
                            case "1":
                                Entsorgung = Entsorgung + Verpackung[i] + "= Wertstofftonne oder Gelber Sack" + "\n";
                                //System.out.println(Entsorgung);
                                break;
                            case "2":
                                Entsorgung = Entsorgung + Verpackung[i] + "= Schwarze Tonne" + "\n";
                                //System.out.println(Entsorgung);
                                break;
                            case "3":
                                Entsorgung = Entsorgung + Verpackung[i] + "= Blaue Tonne" + "\n";
                                //System.out.println(Entsorgung);
                                break;
                            case "4":
                                Entsorgung = Entsorgung + Verpackung[i] + "= Glascontainer" + "\n";
                                //System.out.println(Entsorgung);
                                break;
                            case "5":
                                Entsorgung = Entsorgung + Verpackung[i] + "= Pfandannahmestellen im Handel" + "\n";
                                //System.out.println(Entsorgung);
                                break;
                            case "6":
                                Entsorgung = Entsorgung + Verpackung[i] + "= keinem Material zuordbar" + "\n";
                                //System.out.println(Entsorgung);
                                break;
                            default:
                                break;
                        }
                        reID ="";
                    }

                    resultTxt.setText("Verpackungen: " + Packung + "\n\n" + "Entsorgung: " + "\n" + Entsorgung + "\n" + "EANCode: " + EANCode + "\n\n" + "Produkt: " + Produktname + "\n\n" + "Marke: " + Marke);
                }
            });



            //Verpackung im Array anzeigen lassen
            /*for (int i = 0; i < (Verpackung.length); i++) {

                System.out.println(Verpackung[i]);

            }*/
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

        for (int i = 0; i < (verpackung_array.length); i++) {
            verpackung_array[i] = verpackung_array[i].trim();
            //String test = verpackung_array[1];
            //test= test.trim();
            //System.out.println(test);
            //System.out.println(verpackung_array[i]);
            //System.out.println("This is a test");
            }

        return verpackung_array;
    }


}
