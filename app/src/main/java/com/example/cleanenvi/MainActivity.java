package com.example.cleanenvi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{
    //Newsfeed Declarations:
    TextView newsTitle, newsMainText;
    String[] newsArrayCenter, newsArrayLeft, newsArrayRight;
    int newsRowNumber, currentCenterNewsIndex;
    Newsfeed newsfeedInstance;

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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_main);
        newsTitle =  findViewById(R.id.newsTitle);
        newsMainText =  findViewById(R.id.newsMainText);

        //Button der manuellen Produktsuche
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, ProductSearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
                MainActivity.this.startActivity(new Intent(MainActivity.this, MapActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        //Verhindern des Layoutflackerns
        bottomNavigationView.getMenu().getItem(0).setEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                } else if(id == R.id.action_search) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, ProductSearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                } else if(id == R.id.action_camera) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, CameraMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                } else if (id == R.id.action_hofkarte) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, MapActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                }
                return true;
            }
        });

        //Newsfeed Initialization:
        new APICall().execute();

        //Swipe gestures
        //initialise gestureDetector
        this.gestureDetector=new GestureDetector(MainActivity.this,this);
    }

    private class APICall extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            newsfeedSetup();
            return null;
        }
    }

    private void newsfeedSetup() {
        newsfeedInstance = new Newsfeed();
        newsRowNumber = newsfeedInstance.getNewsCount();
        currentCenterNewsIndex = new Random().nextInt(newsRowNumber) + 1; //first NewsIndex is random.
        newsArrayCenter = newsfeedInstance.getSingleNews(currentCenterNewsIndex);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newsTitle.setText(newsArrayCenter[0]);
                newsMainText.setText(newsArrayCenter[1]);
            }
        });
        if (currentCenterNewsIndex == 1){
            newsArrayLeft = newsfeedInstance.getSingleNews(newsRowNumber);
            newsArrayRight = newsfeedInstance.getSingleNews(2);
        }
        else if (currentCenterNewsIndex == newsRowNumber){
            newsArrayLeft = newsfeedInstance.getSingleNews(newsRowNumber - 1);
            newsArrayRight = newsfeedInstance.getSingleNews(1);
        }
        else {
            newsArrayLeft = newsfeedInstance.getSingleNews(currentCenterNewsIndex - 1);
            newsArrayRight = newsfeedInstance.getSingleNews(currentCenterNewsIndex + 1);
        }
    }

    private void newsfeedUpdate(Newsfeed newsfeedInstance, int swipeDirection){
        if (swipeDirection == -1){ // left swipe
            newsArrayLeft = newsArrayCenter;
            newsArrayCenter = newsArrayRight; //DB version.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    newsTitle.setText(newsArrayCenter[0]);
                    newsMainText.setText(newsArrayCenter[1]);
                }
            });
            if (currentCenterNewsIndex == newsRowNumber){
                currentCenterNewsIndex = 1;
            } else {
                currentCenterNewsIndex += 1;
            }
            if (currentCenterNewsIndex == newsRowNumber) {
                newsArrayRight = newsfeedInstance.getSingleNews(1);
            } else {
                newsArrayRight = newsfeedInstance.getSingleNews(currentCenterNewsIndex + 1);
            }
        }
        else if (swipeDirection == 1) { // right swipe
            newsArrayRight = newsArrayCenter;
            newsArrayCenter = newsArrayLeft;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    newsTitle.setText(newsArrayCenter[0]);
                    newsMainText.setText(newsArrayCenter[1]);
                }
            });
            if (currentCenterNewsIndex == 1) {
                currentCenterNewsIndex = newsRowNumber;
            } else {
                currentCenterNewsIndex -= 1;
            }
            if (currentCenterNewsIndex == 1){
                newsArrayLeft = newsfeedInstance.getSingleNews(newsRowNumber);
            } else {
                newsArrayLeft = newsfeedInstance.getSingleNews(currentCenterNewsIndex - 1);
            }
        }
    }

    private class APIUpdate extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            int swipeDirection = integers[0];
            newsfeedUpdate(newsfeedInstance, swipeDirection);
            return null;
        }
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

                    if(x2>x1){
                        new APIUpdate().execute(1);
                    }
                    else{
                        new APIUpdate().execute(-1);
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
