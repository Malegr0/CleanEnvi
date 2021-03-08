package com.example.cleanenvi.productmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.cleanenvi.helpers.history.History;
import com.example.cleanenvi.helpers.history.HistoryManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


//TODO: delete TextView "Infos zum Produkt"
public class ProductShowActivity extends AppCompatActivity {

    String ean;
    TextView resultTxt, result_packaging, result_disposal, result_brand, result_EAN, result_product;
    TextView textViewProdukt, textViewBrand, textViewEan, textViewPackage, textViewEntsorgung;
    ImageView productImageView, imageViewRest, imageViewGlas, imageViewWert, imageViewPapier;
    ProgressBar processingBar;
    TextView noDis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_show);
        this.setTitle("Produktsuche");
        //set ean
        if (ProductSearchActivity.EAN != null) {
            ean = ProductSearchActivity.EAN;
        } else if (CameraMainActivity.EAN_CAMERA != null) {
            ean = CameraMainActivity.EAN_CAMERA;
        }
        processingBar = findViewById(R.id.processingBar);
        processingBar.setVisibility(View.VISIBLE);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_show);

        /*noDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*String url = "http://www.gobloggerslive.com";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*//*
            }
        });*/

        //make api call
        new APICall().execute();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    ProductShowActivity.this.startActivity(new Intent(ProductShowActivity.this, MainActivity.class));
                } else if (id == R.id.action_search) {
                    ProductShowActivity.this.startActivity(new Intent(ProductShowActivity.this, ProductSearchActivity.class));
                } else if (id == R.id.action_camera) {
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
            noDis = findViewById(R.id.btnnodisposal);
            textViewBrand = findViewById(R.id.textViewBrand);
            textViewProdukt = findViewById(R.id.textViewProdukt);
            textViewEan = findViewById(R.id.textViewEan);
            textViewEntsorgung = findViewById(R.id.textViewEntsorgung);
            textViewPackage = findViewById(R.id.textViewPackage);

            noDis = findViewById(R.id.btnnodisposal);
            noDis.setMovementMethod(LinkMovementMethod.getInstance());

            String[] productData = null;
            try {
                productData = ResponseManager.getProductData(ean);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            //TODO: add check for packages which arent in database
            if (productData != null) {
                String[] packages;
                ArrayList<String> recID = new ArrayList<>();
                if (productData[3] != null) {
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
                final String[] finalProductData = productData;
                final ArrayList<String> packaging = recID;
                final String[] finalPackages = packages;
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        //render picture
                        Picasso.get().load(finalProductData[2]).into(productImageView);
                        productImageView.setVisibility(View.VISIBLE);

                        // ImageView mit "grauer Tonne" starten
                        imageViewRest.setImageResource(R.drawable.ic_graue_tonne);
                        imageViewGlas.setImageResource(R.drawable.ic_graue_tonne);
                        imageViewWert.setImageResource(R.drawable.ic_graue_tonne);
                        imageViewPapier.setImageResource(R.drawable.ic_graue_tonne);

                        String recOutput1 = "Wertstofftonne oder Gelber Sack: \n", recOutput2 = "Restmüll: \n",
                                recOutput3 = "Blaue Tonne: \n", recOutput4 = "Glascontainer: \n", recOutput5 = "Pfandannahmestellen im Handel: \n", recOutput6 = "nicht genau zuordenbar: \n";
                        String recOutput = "";
                        boolean pfand = false;
                        boolean noDisposal = true;

                        for (int i = 0; i < finalPackages.length; i++) {
                            String reID = packaging.get(i);
                            if (reID.equals("5")) {
                                pfand = true;
                            }
                            if (reID.equals("1") || reID.equals("2") || reID.equals("3") || reID.equals("4") || reID.equals("5") ) {
                                noDisposal = false;
                            }
                            switch (reID) {
                                case "1":
                                    recOutput1 = recOutput1 + finalPackages[i] + ", ";
                                    imageViewWert.setImageResource(R.mipmap.ic_wert_2_foreground);
                                    resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                    break;
                                case "2":
                                    recOutput2 = recOutput2 + finalPackages[i] + ", ";
                                    imageViewRest.setImageResource(R.mipmap.ic_rest_2_foreground);
                                    resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                    break;
                                case "3":
                                    recOutput3 = recOutput3 + finalPackages[i]  + ", ";
                                    imageViewPapier.setImageResource(R.mipmap.ic_papier_2_foreground);
                                    resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                    break;
                                case "4":
                                    recOutput4 = recOutput4 + finalPackages[i]  + ", ";
                                    if (!pfand) {
                                        imageViewGlas.setImageResource(R.mipmap.ic_glas_2_foreground);
                                    }
                                    resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                    break;
                                case "5":
                                    recOutput5 = recOutput5 + finalPackages[i]  + ", ";
                                    imageViewGlas.setImageResource(R.mipmap.ic_pfand_foreground);
                                    imageViewWert.setImageResource(R.drawable.ic_graue_tonne);
                                    break;
                                case "6":
                                    recOutput6 = recOutput6 + finalPackages[i]  + ", ";
                                    resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                    break;
                                default:
                                    recOutput = "Keine Angaben verfügbar" + "\n";
                                    break;
                            }

                        }

                        if (!recOutput1.equals("Wertstofftonne oder Gelber Sack: \n")){
                            recOutput1=recOutput1.substring(0, recOutput1.length()-2);
                            recOutput = recOutput + recOutput1  + "\n";
                        }
                        if (!recOutput2.equals("Restmüll: \n")){
                            recOutput2= recOutput2.substring(0, recOutput2.length()-2);
                            recOutput = recOutput + recOutput2  + "\n";
                        }
                        if (!recOutput3.equals("Blaue Tonne: \n")){
                            recOutput3= recOutput3.substring(0, recOutput3.length()-2);
                            recOutput = recOutput + recOutput3  + "\n";
                        }
                        if (!recOutput4.equals("Glascontainer: \n")){
                            recOutput4= recOutput4.substring(0, recOutput4.length()-2);
                            recOutput = recOutput + recOutput4  + "\n";
                        }
                        if (!recOutput5.equals("Pfandannahmestellen im Handel: \n")){
                            recOutput5= recOutput5.substring(0, recOutput5.length()-2);
                            recOutput = recOutput + recOutput5  + "\n";
                        }
                        if (!recOutput6.equals("nicht genau zuordenbar: \n")){
                            recOutput = recOutput + recOutput6  + "\n";
                        }


                        if (noDisposal) {
                            noDis.setVisibility(View.VISIBLE);
                        }

                        processingBar.setVisibility(View.INVISIBLE);
                        result_packaging.setText(finalProductData[3]);
                        textViewPackage.setVisibility(View.VISIBLE);
                        result_disposal.setText(recOutput);
                        textViewEntsorgung.setVisibility(View.VISIBLE);
                        result_brand.setText(finalProductData[4]);
                        textViewBrand.setVisibility(View.VISIBLE);
                        result_product.setText(finalProductData[1]);
                        textViewProdukt.setVisibility(View.VISIBLE);
                        result_EAN.setText(finalProductData[0]);
                        textViewEan.setVisibility(View.VISIBLE);
                        ProductSearchActivity.EAN = null;
                        CameraMainActivity.EAN_CAMERA = null;
                        ean = null;
                        HistoryManager.addNewHistory(new History(finalProductData[0], finalProductData[1], finalProductData[2]));
                        try {
                            HistoryManager.saveHistories(getApplication().getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                processingBar.setVisibility(View.INVISIBLE);
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        resultTxt.setText("Das Produkt ist noch nicht vorhanden, oder die EAN ist falsch eingegeben/eingescannt worden.");
                    }
                });
            }
            return null;
        }
    }



}
