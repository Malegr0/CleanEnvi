package com.example.cleanenvi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductShowActivity extends AppCompatActivity {
    String EANmanuell, EANcamera;
    TextView resultTxt, result_packaging, result_disposal, result_brand, result_EAN, result_product;
    ImageView proimageView, imageViewRest, imageViewGlas, imageViewWert, imageViewPapier;
    DBHelper mDBHelper;
    ProgressDialog nDialog, mDialog;
    String reID;
    String Entsorgung = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_show);
        this.setTitle("Produktsuche");
        mDBHelper = new DBHelper(this);
        EANmanuell = ProductSearchActivity.EAN;
        EANcamera = CameraMainActivity.EANcamera;
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_show);

        //Ladebildschirm für den Nutzer
        nDialog = new ProgressDialog(ProductShowActivity.this);
        nDialog.setMessage("Bitte warten (Ladezeit abhängig von Internetverbindung)..");
        nDialog.setTitle("Anfrage senden");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();


        // Methodenaufruf für die API-Anfrage
        new OpenFoodFacts().execute();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        ProductShowActivity.this.startActivity(new Intent(ProductShowActivity.this, MainActivity.class));
                        break;
                    case R.id.action_search:
                        ProductShowActivity.this.startActivity(new Intent(ProductShowActivity.this, ProductSearchActivity.class));
                        break;
                    case R.id.action_camera:
                        ProductShowActivity.this.startActivity(new Intent(ProductShowActivity.this, CameraMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.action_hofkarte:
                        ProductShowActivity.this.startActivity(new Intent(ProductShowActivity.this, MapActivity.class));
                        break;
                }
                return true;
            }
        });
    }

    //Hintergrund-Thread für den API-Aufruf
    private class OpenFoodFacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (EANmanuell == null) {
                api(EANcamera);
            } else {
                api(EANmanuell);
            }
            return null;
        }
    }

    //Gesamtmethode für die API-Anfrage (besteht aus einzelnen kleineren Funktionen)
    public void api(String EAN) {
        proimageView = findViewById(R.id.productImageView);
        imageViewRest = findViewById(R.id.imageViewRest);
        imageViewGlas = findViewById(R.id.imageViewGlas);
        imageViewWert = findViewById(R.id.imageViewWert);
        imageViewPapier = findViewById(R.id.imageViewPapier);
        resultTxt = findViewById(R.id.showResulttxt);
        result_packaging = findViewById(R.id.result_packaging);
        result_brand = findViewById(R.id.result_brand);
        result_disposal = findViewById(R.id.result_disposal);
        result_EAN = findViewById(R.id.result_EAN);
        result_product = findViewById(R.id.result_product);


        //URL für Abfrage erstellen
        String query_url = makeURL(EAN);

        //URL an API senden und JSON-Datei empfangen
        String JSONResponsevollstaendig = getJSON(query_url);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDialog = new ProgressDialog(ProductShowActivity.this);
                mDialog.setMessage("Bitte warten (Ladezeit variiert nach Produkt)..");
                mDialog.setTitle("Daten verarbeiten");
                mDialog.setIndeterminate(false);
                mDialog.setCancelable(true);
                mDialog.show();
            }
        });

        String ResponseMSt = replaceResponse(JSONResponsevollstaendig);
        System.out.println("JSON: " + ResponseMSt);

        // Überprüfung ob Produkt vorhanden ist
        String ProductAvailable = checkResponse(ResponseMSt);
        System.out.println("Produkt vorhanden:_" + ProductAvailable + "_");
        if (ProductAvailable.equals("0")) {
            runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    resultTxt.setText("Das Produkt ist noch nicht vorhanden, oder die EAN ist falsch eingegeben/eingescannt worden.");
                    nDialog.dismiss();
                    mDialog.dismiss();
                }
            });
        } else {
            //Wenn das Produkt vorhanden ist, werden wichtige Daten aus der JSON-Datei herausgefiltert
            final String Verpackungen = splitResponse(ResponseMSt, "packaging");
            final String Packung = Verpackungen.toUpperCase();
            final String EANCode = splitResponse(ResponseMSt, "code");
            final String Produktname = splitResponse(ResponseMSt, "product_name");
            final String Marke = splitResponse(ResponseMSt, "brands");
            final String Bild = splitResponse(ResponseMSt, "image_url");
            final String[] Verpackung = splitInArray(Packung);

            runOnUiThread(new Runnable() {
                @SuppressLint({"SetTextI18n", "ResourceType"})
                @Override
                public void run() {
                    //Zeigt dem User ein Bild des Produkts an
                    Picasso.get().load(Bild).into(proimageView);
                    proimageView.setVisibility(View.VISIBLE);

                    // ImageView mit "grauer Tonne" starten
                    imageViewRest.setImageResource(R.drawable.ic_graue_tonne);
                    imageViewGlas.setImageResource(R.drawable.ic_graue_tonne);
                    imageViewWert.setImageResource(R.drawable.ic_graue_tonne);
                    imageViewPapier.setImageResource(R.drawable.ic_graue_tonne);

                    boolean pfand=false;
                    for (String s : Verpackung) {


                        Cursor data = mDBHelper.getData(s);
                        StringBuilder buffer = new StringBuilder();
                        while (data.moveToNext()) {
                            buffer.append(data.getString(1));
                        }
                        reID = buffer.toString();

                        if (reID.equals("5")){
                            pfand =true;
                        }

                        //Unterscheidung der Entsorgung abhängig von Recyclingnummer
                        switch (reID) {
                            case "1":
                                Entsorgung = Entsorgung + s + "= Wertstofftonne oder Gelber Sack" + "\n";
                                imageViewWert.setImageResource(R.mipmap.ic_wert_2_foreground);
                                resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                break;
                            case "2":
                                Entsorgung = Entsorgung + s + "= Schwarze Tonne" + "\n";
                                imageViewRest.setImageResource(R.mipmap.ic_rest_2_foreground);
                                resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                break;
                            case "3":
                                Entsorgung = Entsorgung + s + "= Blaue Tonne" + "\n";
                                imageViewPapier.setImageResource(R.mipmap.ic_papier_2_foreground);
                                resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                break;
                            case "4":
                                Entsorgung = Entsorgung + s + "= Glascontainer" + "\n";
                                if (!pfand){
                                    imageViewGlas.setImageResource(R.mipmap.ic_glas_2_foreground);
                                }

                                resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                break;
                            case "5":
                                Entsorgung = Entsorgung + s + "= Pfandannahmestellen im Handel" + "\n";
                                imageViewGlas.setImageResource(R.mipmap.ic_pfand_foreground);
                                imageViewWert.setImageResource(R.drawable.ic_graue_tonne);
                                break;
                            case "6":
                                Entsorgung = Entsorgung + s + "= nicht genau zuordenbar" + "\n";
                                resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                break;
                            default:
                                if (Packung.equals("KEINE ANGABEN VERFÜGBAR")) {
                                    Entsorgung = "KEINE ANGABEN VERFÜGBAR" + "\n";
                                } else {
                                    break;
                                }
                        }

                        reID = "";
                    }

                    nDialog.dismiss();
                    mDialog.dismiss();
                    result_packaging.setText(Packung);
                    result_disposal.setText(Entsorgung);
                    result_brand.setText(Marke);
                    result_product.setText(Produktname);
                    result_EAN.setText(EANCode);


                    EANmanuell = null;
                    EANcamera = null;
                    ProductSearchActivity.EAN = null;
                    CameraMainActivity.EANcamera = null;
                }
            });
        }
    }

    //Gänsefüßchen ersetzen für bessere Weiterverarbeitung der Strings
    public static String replaceResponse(String response) {
        char c = '%';
        String ResponseMSt = response.replace('"', c);
        return ResponseMSt;
    }

    //URL für API-Anfrage zusammensetzen
    public static String makeURL(String EAN) {
        String url, openfoodfactsurl;
        openfoodfactsurl = "https://world.openfoodfacts.org/api/v0/product/";
        url = openfoodfactsurl.concat(EAN);
        url = url.concat(".json");
        System.out.println("api_url= " + url);
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
                in = new BufferedReader(new InputStreamReader(connget.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
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

    //Ergebnis der API-Anfrage auf Vorhandensein des Produkts überprüfen
    public static String checkResponse(String ResponseMSt) {
        //Such String erstellen
        String searchString = "status%:";
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

    //Herausfiltern von gewünschten Daten aus der JSON-Datei
    public static String splitResponse(String ResponseMSt, String suchString) {
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
        if (Verpackung.contentEquals("#")) {
            return "Keine Angaben verfügbar";
        } else {
            return Verpackung;
        }
    }

    //Verpackung als Array speichern
    public static String[] splitInArray(String verpackung_string) {
        String[] verpackung_array;
        verpackung_array = verpackung_string.split(",");

        for (int i = 0; i < (verpackung_array.length); i++) {
            verpackung_array[i] = verpackung_array[i].trim();
        }
        return verpackung_array;
    }
}
