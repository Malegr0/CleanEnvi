package com.example.cleanenvi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        Button buttonSearch = findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button 1 Clicked");
            }
        });

        Button buttonCameraSearch = findViewById(R.id.button_camera_search);
        buttonCameraSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
            }
        });

        Button buttonMap = findViewById(R.id.button_map);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
            }
        });
    }
}
