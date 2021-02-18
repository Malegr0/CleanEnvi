package com.example.cleanenvi.productmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cleanenvi.CameraMainActivity;
import com.example.cleanenvi.MainActivity;
import com.example.cleanenvi.MapActivity;
import com.example.cleanenvi.ProductSearchActivity;
import com.example.cleanenvi.R;
import com.example.cleanenvi.helpers.ResponseManager;
import com.example.cleanenvi.helpers.URLManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;

public class ProductShowActivity extends AppCompatActivity {

    String ean;
    TextView resultTxt;
    ImageView productImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_show);
        this.setTitle("Produktsuche");
        //set ean
        if(ProductSearchActivity.EAN != null) {
            ean = ProductSearchActivity.EAN;
        } else if(CameraMainActivity.EANcamera != null) {
            ean = CameraMainActivity.EANcamera;
        }
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_show);

        //make api call
        new APICall().execute();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        com.example.cleanenvi.productmanager.ProductShowActivity.this
                                .startActivity(new Intent(com.example.cleanenvi.productmanager.ProductShowActivity.this, MainActivity.class));
                        break;
                    case R.id.action_search:
                        com.example.cleanenvi.productmanager.ProductShowActivity.this
                                .startActivity(new Intent(com.example.cleanenvi.productmanager.ProductShowActivity.this, ProductSearchActivity.class));
                        break;
                    case R.id.action_camera:
                        com.example.cleanenvi.productmanager.ProductShowActivity.this
                                .startActivity(new Intent(com.example.cleanenvi.productmanager.ProductShowActivity.this, CameraMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.action_hofkarte:
                        com.example.cleanenvi.productmanager.ProductShowActivity.this
                                .startActivity(new Intent(com.example.cleanenvi.productmanager.ProductShowActivity.this, MapActivity.class));
                        break;
                }
                return true;
            }
        });

    }

    //background thread for api call
    private class APICall extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            productImageView = findViewById(R.id.productImageView);
            resultTxt = findViewById(R.id.showResulttxt);
            String[] productData = null;
            try {
                productData = ResponseManager.getProductData(ean);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            //TODO: add check if product is available/if something is in response
            if(true) {
                final String[] finalProductData = productData;
                final String[] packages = productData[3].split(",");
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        //TODO: test performance without picasso
                        //render picture
                        Picasso.get().load(finalProductData[2]).into(productImageView);
                        productImageView.setVisibility(View.VISIBLE);
                        String recOutput = "";

                        for(String s: packages) {
                            String reID = "";
                            try {
                                reID = URLManager.getRecID(s.toUpperCase());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            switch (reID) {
                                case "1":
                                    recOutput = recOutput + s + "= Wertstofftonne oder Gelber Sack" + "\n";
                                    break;
                                case "2":
                                    recOutput = recOutput + s + "= Schwarze Tonne" + "\n";
                                    break;
                                case "3":
                                    recOutput = recOutput + s + "= Blaue Tonne" + "\n";
                                    break;
                                case "4":
                                    recOutput = recOutput + s + "= Glascontainer" + "\n";
                                    break;
                                case "5":
                                    recOutput = recOutput + s + "= Pfandannahmestellen im Handel" + "\n";
                                    break;
                                case "6":
                                    recOutput = recOutput + s + "= nicht genau zuordenbar" + "\n";
                                    break;
                                default:
                                    /*if (Packung.equals("KEINE ANGABEN VERFÜGBAR")) {
                                        recOutput = "KEINE ANGABEN VERFÜGBAR" + "\n";
                                    } else {
                                        break;
                                    }*/
                                    break;
                            }
                        }

                        resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!" + "\n\n"
                                + "Verpackungen: " + finalProductData[3] + "\n\n"
                                + "Entsorgung: " + "\n" + recOutput + "\n"
                                + "EAN-Code: " + finalProductData[0] + "\n\n"
                                + "Produkt: " + finalProductData[1] + "\n\n"
                                + "Marke: " + finalProductData[4]);
                        ProductSearchActivity.EAN = null;
                        CameraMainActivity.EANcamera = null;
                        ean = null;
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        resultTxt.setText("Das Produkt ist noch nicht vorhanden, oder die EAN ist falsch eingegeben/eingescannt worden.");
                        //nDialog.dismiss();
                        //mDialog.dismiss();
                    }
                });
            }
            return null;
        }
    }
}
