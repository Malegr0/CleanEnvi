package com.example.cleanenvi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
//TODO: design changes by Christopher need to be added
public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    //Newsfeed Declarations:
    TextView newsTitle, newsMainText;
    String[] newsArray;

    //Swipe gesture members
    private float x1,x2;
    private static int minDistance=150;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        Button buttonSearch = findViewById(R.id.button_search);
        Button buttonCameraSearch = findViewById(R.id.button_camera_search);
        Button buttonMap = findViewById(R.id.button_map);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_main);
        newsTitle =  findViewById(R.id.newsTitle);
        newsMainText =  findViewById(R.id.newsMainText);

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

        //Newsfeed Initialization:
        newsfeedSetup();

        //Swipe gestures
        //initialise gestureDetector
        this.gestureDetector=new GestureDetector(MainActivity.this,this);
    }


    void newsfeedSetup() {
        Newsfeed NewsfeedInstance = new Newsfeed();
        newsArray = NewsfeedInstance.chooseRandomNews();
        newsTitle.setText(newsArray[0]);
        newsMainText.setText(newsArray[1]);
    }

    //override on touch event vor swipe gestures
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        switch (event.getAction()){

            //starting to swipe time gesture
            case MotionEvent.ACTION_DOWN:
                x1=event.getX();
                break;

                //ending time swipe gesture
            case MotionEvent.ACTION_UP:
                x2=event.getX();

                //value for horizontal swipe
                float valueX=x2-x1;

                if(Math.abs(valueX)>minDistance){

                    //detect left to right swipe
                    if(x2>x1){
                        //display already loaded newsfeed array index-1
                        //load newsfeed array index-2 from database (on index+1 or shift all before)

                        //toast to see if it works
                        Toast.makeText(this,"Right is swiped",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //detects right to left swipe
                        //display already loaded newsfeed array index+1
                        //load newsfeed array index+2 from database (on index-1 or shift all before)

                        //toast to see if it works
                        Toast.makeText(this,"Left is swiped",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    //swipe gestures interface implementation
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
