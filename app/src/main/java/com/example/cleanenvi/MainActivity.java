package com.example.cleanenvi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
//TODO: bottomNavigationBar change from switch to if-else in all classes
public class MainActivity extends AppCompatActivity {

    String tName1,tName2,tName3,tName4,tName5,tName6, tNameGes, tID;

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

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;
                    case R.id.action_search:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, ProductSearchActivity.class));
                        break;
                    case R.id.action_camera:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, CameraMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.action_hofkarte:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, MapActivity.class));
                        break;
                }
                return true;
            }
        });
    }
}
