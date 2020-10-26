package com.example.cleanenvi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
    String EANmanuell, EANcamera;
    Button backBtn, backCameraBtn;
    TextView resultTxt;
    ImageView proimageView;
    DBHelper mDBHelper;
    ProgressDialog nDialog, mDialog;
    String reID;
    String Entsorgung ="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_show);
        this.setTitle("Manuelle Produktsuche per Nummer");
        mDBHelper = new DBHelper(this);
        EANmanuell = ProductSearchActivity.EAN;
        EANcamera = CameraMainActivity.EANcamera;
        backBtn = findViewById(R.id.back);
        backCameraBtn = findViewById(R.id.backCamera);

        nDialog = new ProgressDialog(ProductShowActivity.this);
        nDialog.setMessage("Bitte warten (Ladezeit abhängig von Internetverbindung)..");
        nDialog.setTitle("Anfrage senden");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();

        // Methodenaufruf für API-Anfrage
        new OpenFoodFacts().execute();

        //setzt User beim Betätigen des Zurück-Buttons wieder zur Eingabe
        backBtn.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                ProductShowActivity.this.startActivity(new Intent(ProductShowActivity.this, ProductSearchActivity.class));
            }
        });

        //setzt User beim Betätigen des ZurückCamera-Buttons wieder zur Kamera
        backCameraBtn.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                ProductShowActivity.this.startActivity(new Intent(ProductShowActivity.this, CameraMainActivity.class));
            }
        });
    }

    //Hintergrund-Thread für API-Aufruf
    private class OpenFoodFacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if(EANmanuell == null) {
                api(EANcamera);
            } else {
                api(EANmanuell);
            }
            return null;
        }
    }

    //Methode für API-Anfrage
    public void api(String EAN) {
        proimageView = findViewById(R.id.productImageView);
        resultTxt = findViewById(R.id.showResulttxt);
        backBtn = findViewById(R.id.back);

        //URL für Abfrage erstellen
        String query_url = makeURL(EAN);

        //URL an API senden und JSON empfangen
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

        //JSON alle " gegen % tauschen
        String ResponseMSt = replaceResponse(JSONResponsevollstaendig);
        System.out.println(ResponseMSt);

        // JSON aufteilen in einzelne Strings mit den gewünschten Daten
        // Wenn EAN nicht gefunden wurde, wird eine entsprechende Meldung gegeben. Dafür wird als erstes der "Status" in der JSON gesucht und ausgewertet.
        String ProductAvailable = checkResponse(ResponseMSt);
        System.out.println("Produkt vorhanden:_" + ProductAvailable + "_");
        if(ProductAvailable.equals("0")) {
            runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    resultTxt.setText("Das Produkt ist noch nicht vorhanden, oder die EAN ist falsch.");
                    nDialog.dismiss();
                    mDialog.dismiss();
                }
            });
        } else {
            final String Verpackungen = splitResponse(ResponseMSt, "packaging");
            final String Packung = Verpackungen.toUpperCase();
            final String EANCode = splitResponse(ResponseMSt, "code");
            final String Produktname = splitResponse(ResponseMSt, "product_name");
            final String Marke = splitResponse(ResponseMSt, "brands");
            final String Bild = splitResponse(ResponseMSt, "image_url");
            final String[] Verpackung = splitInArray(Packung);

            runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    //zeigt dem User Bild des Produkts  an
                    Picasso.get().load(Bild).into(proimageView);
                    proimageView.setVisibility(View.VISIBLE);

                    for (String s : Verpackung) {
                        Cursor data = mDBHelper.getData(s);
                        StringBuilder buffer = new StringBuilder();
                        while (data.moveToNext()) {
                            buffer.append(data.getString(1));
                        }
                        reID = buffer.toString();

                        switch (reID) {
                            case "1":
                                Entsorgung = Entsorgung + s + "= Wertstofftonne oder Gelber Sack" + "\n";
                                break;
                            case "2":
                                Entsorgung = Entsorgung + s + "= Schwarze Tonne" + "\n";
                                break;
                            case "3":
                                Entsorgung = Entsorgung + s + "= Blaue Tonne" + "\n";
                                break;
                            case "4":
                                Entsorgung = Entsorgung + s + "= Glascontainer" + "\n";
                                break;
                            case "5":
                                Entsorgung = Entsorgung + s + "= Pfandannahmestellen im Handel" + "\n";
                                break;
                            case "6":
                                Entsorgung = Entsorgung + s + "= keinem Material zuordbar" + "\n";
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
                    resultTxt.setText("Bei PET bitte vorher nach Pfand gucken!" + "\n\n" + "Verpackungen: " + Packung + "\n\n" + "Entsorgung: " + "\n" + Entsorgung + "\n" + "EANCode: " + EANCode + "\n\n" + "Produkt: " + Produktname + "\n\n" + "Marke: " + Marke);
                    EANmanuell = null;
                    EANcamera = null;
                    ProductSearchActivity.EAN = null;
                    CameraMainActivity.EANcamera = null;
                }
            });

            //Verpackung im Array anzeigen lassen
            for (String s : Verpackung) {
                System.out.println(s);
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

            if(responseCode != 200) {
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
        if(Verpackung.contentEquals("#")) {
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
