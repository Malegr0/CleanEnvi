package com.example.cleanenvi;

import android.app.Activity;
import android.app.AsyncNotedAppOp;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.cleanenvi.helpers.ResponseManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class SplashScreenActivity extends Activity {

    ImageView logoImage;
    public static JSONArray allMarkers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        logoImage = findViewById(R.id.logo_image);
        logoImage.setImageResource(R.mipmap.ic_main_round);

        //Abfrage der Datenbank
        new APICall().execute();
    }

    private class APICall extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                allMarkers = ResponseManager.getAllMarkers();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }
    }
}
