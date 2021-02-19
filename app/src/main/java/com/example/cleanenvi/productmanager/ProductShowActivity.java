package com.example.cleanenvi.productmanager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class ProductShowActivity extends AppCompatActivity {

    //TODO: check for rest in old ProductShowActivity which need to be added

    String ean;
    TextView resultTxt;
    ImageView productImageView;
    ProgressDialog waitingDialog;

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
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    ProductShowActivity.this.startActivity(new Intent(ProductShowActivity.this, MainActivity.class));
                } else if(id == R.id.action_search) {
                    ProductShowActivity.this.startActivity(new Intent(ProductShowActivity.this, ProductSearchActivity.class));
                } else if(id == R.id.action_camera) {
                    ProductShowActivity.this.startActivity(new Intent(ProductShowActivity.this, CameraMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                } else if (id == R.id.action_hofkarte) {
                    ProductShowActivity.this.startActivity(new Intent(ProductShowActivity.this, MapActivity.class));
                }
                return true;
            }
        });

    }

    //background thread for api call
    @SuppressLint("StaticFieldLeak")
    private class APICall extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            productImageView = findViewById(R.id.productImageView);
            resultTxt = findViewById(R.id.showResulttxt);

            //set and show dialog for web call
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    waitingDialog = new ProgressDialog(ProductShowActivity.this);
                    waitingDialog.setMessage("Bitte warten (Ladezeit variiert nach Produkt)..");
                    waitingDialog.setTitle("Daten verarbeiten");
                    waitingDialog.setIndeterminate(false);
                    waitingDialog.setCancelable(true);
                    waitingDialog.show();
                }
            });

            String[] productData = null;
            try {
                productData = ResponseManager.getProductData(ean);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            if(productData != null) {
                String[] packages;
                ArrayList<String> recID = new ArrayList<String>();
                if(productData[3] != null) {
                    packages = productData[3].split(",");
                    for (String s : packages) {
                        try {
                            recID.add(ResponseManager.getRecIDData(s.toUpperCase()));
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    recID.add("0");
                    packages = new String[1];
                    productData[3] = "Keine Angaben verfügbar.";
                }
                //TODO: add check if product is available/if something is in response, with response code
                final String[] finalProductData = productData;
                final ArrayList<String> packaging = recID;
                final String[] finalPackages = packages;
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        //TODO: test performance without picasso
                        //render picture
                        Picasso.get().load(finalProductData[2]).into(productImageView);
                        productImageView.setVisibility(View.VISIBLE);
                        String recOutput = "";

                        for(int i = 0; i < finalPackages.length; i++) {
                            String reID = packaging.get(i);
                            switch (reID) {
                                case "1":
                                    recOutput = recOutput + finalPackages[i] + "= Wertstofftonne oder Gelber Sack" + "\n";
                                    break;
                                case "2":
                                    recOutput = recOutput + finalPackages[i] + "= Schwarze Tonne" + "\n";
                                    break;
                                case "3":
                                    recOutput = recOutput + finalPackages[i] + "= Blaue Tonne" + "\n";
                                    break;
                                case "4":
                                    recOutput = recOutput + finalPackages[i] + "= Glascontainer" + "\n";
                                    break;
                                case "5":
                                    recOutput = recOutput + finalPackages[i] + "= Pfandannahmestellen im Handel" + "\n";
                                    break;
                                case "6":
                                    recOutput = recOutput + finalPackages[i] + "= nicht genau zuordenbar" + "\n";
                                    break;
                                default:
                                    recOutput = "Keine Angaben verfügbar" + "\n";
                                    break;
                            }
                        }
                        waitingDialog.dismiss();
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
                waitingDialog.dismiss();
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
