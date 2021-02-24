package com.example.cleanenvi.productmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

//TODO: delete TextView "Infos zum Produkt"
public class ProductShowActivity extends AppCompatActivity {

    String ean;
    TextView resultTxt, result_packaging, result_disposal, result_brand, result_EAN, result_product;
    ImageView productImageView, imageViewRest, imageViewGlas, imageViewWert, imageViewPapier;
    ProgressBar processingBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_show);
        this.setTitle("Produktsuche");
        //set ean
        if(ProductSearchActivity.EAN != null) {
            ean = ProductSearchActivity.EAN;
        } else if(CameraMainActivity.EAN_CAMERA != null) {
            ean = CameraMainActivity.EAN_CAMERA;
        }
        processingBar = findViewById(R.id.processingBar);
        processingBar.setVisibility(View.VISIBLE);
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

                        String recOutput = "";
                        boolean pfand=false;

                        for(int i = 0; i < finalPackages.length; i++) {
                            String reID = packaging.get(i);
                            if (reID.equals("5")){
                                pfand =true;
                            }
                            switch (reID) {
                                case "1":
                                    recOutput = recOutput + finalPackages[i] + "= Wertstofftonne oder Gelber Sack" + "\n";
                                    imageViewWert.setImageResource(R.mipmap.ic_wert_2_foreground);
                                    resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                    break;
                                case "2":
                                    recOutput = recOutput + finalPackages[i] + "= Schwarze Tonne" + "\n";
                                    imageViewRest.setImageResource(R.mipmap.ic_rest_2_foreground);
                                    resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                    break;
                                case "3":
                                    recOutput = recOutput + finalPackages[i] + "= Blaue Tonne" + "\n";
                                    imageViewPapier.setImageResource(R.mipmap.ic_papier_2_foreground);
                                    resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                    break;
                                case "4":
                                    recOutput = recOutput + finalPackages[i] + "= Glascontainer" + "\n";
                                    if (!pfand){
                                        imageViewGlas.setImageResource(R.mipmap.ic_glas_2_foreground);
                                    }
                                    resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                    break;
                                case "5":
                                    recOutput = recOutput + finalPackages[i] + "= Pfandannahmestellen im Handel" + "\n";
                                    imageViewGlas.setImageResource(R.mipmap.ic_pfand_foreground);
                                    imageViewWert.setImageResource(R.drawable.ic_graue_tonne);
                                    break;
                                case "6":
                                    recOutput = recOutput + finalPackages[i] + "= nicht genau zuordenbar" + "\n";
                                    resultTxt.setText("Bei Flaschen bitte vorher nach Pfand gucken!");
                                    break;
                                default:
                                    recOutput = "Keine Angaben verfügbar" + "\n";
                                    break;
                            }
                        }
                        processingBar.setVisibility(View.INVISIBLE);
                        result_packaging.setText(finalProductData[3]);
                        result_disposal.setText(recOutput);
                        result_brand.setText(finalProductData[4]);
                        result_product.setText(finalProductData[1]);
                        result_EAN.setText(finalProductData[0]);
                        ProductSearchActivity.EAN = null;
                        CameraMainActivity.EAN_CAMERA = null;
                        ean = null;
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
