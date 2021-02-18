package com.example.cleanenvi.productmanager;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cleanenvi.CameraMainActivity;
import com.example.cleanenvi.ProductSearchActivity;
import com.example.cleanenvi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductShowActivity extends AppCompatActivity {

    String ean;

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



    }

    //background thread for api call
    private class APICall extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }
}
