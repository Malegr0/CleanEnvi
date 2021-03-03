package com.example.cleanenvi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//TODO: add newsfeed handling
//TODO: design changes by Christopher need to be added
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        Button buttonSearch = findViewById(R.id.button_search);
        Button buttonCameraSearch = findViewById(R.id.button_camera_search);
        Button buttonMap = findViewById(R.id.button_map);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_main);

        //Button der manuellen Produktsuche
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, ProductSearchActivity.class));
            }
        });

        //Button der Kamerasuche
        buttonCameraSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, CameraMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        //Button der Recyclinghofkarte
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });


        //Verhindern des Layoutflackerns
        bottomNavigationView.getMenu().getItem(0).setEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class));
                } else if(id == R.id.action_search) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, ProductSearchActivity.class));
                } else if(id == R.id.action_camera) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, CameraMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                } else if (id == R.id.action_hofkarte) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, MapActivity.class));
                }
                return true;
            }
        });
    }
}
