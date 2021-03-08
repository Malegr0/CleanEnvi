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
import android.widget.Toast;

import com.example.cleanenvi.helpers.history.HistoryManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//TODO: design changes by Christopher need to be added
public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    //Newsfeed Declarations:
    TextView newsTitle, newsMainText;
    //String[] newsArray;
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
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_main);
        newsTitle =  findViewById(R.id.newsTitle);
        newsMainText =  findViewById(R.id.newsMainText);

        //TODO: move init method to splash screen
        HistoryManager.init();

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

    void newsfeedSetup() {
        newsfeedInstance = new Newsfeed();
        newsRowNumber = newsfeedInstance.getNewsCount();
        //newsArray = newsfeedInstance.chooseRandomNewsFromArray(); //array version.
        currentCenterNewsIndex = (int) (System.currentTimeMillis() % newsRowNumber); //first NewsIndex is random.
        //newsfeedUpdate(newsfeedInstance, randomNewsIndex);
        newsArrayCenter = newsfeedInstance.getSingleNews(currentCenterNewsIndex); //DB version.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newsTitle.setText("Irgendein Text2");
                //newsMainText.setText(newsArrayCenter[1]);
            }
        });
        if (currentCenterNewsIndex == 0){
            newsArrayLeft = newsfeedInstance.getSingleNews(newsRowNumber);
            newsArrayRight = newsfeedInstance.getSingleNews(1);
        }
        else if (currentCenterNewsIndex == newsRowNumber - 1){
            newsArrayLeft = newsfeedInstance.getSingleNews(0);
            newsArrayRight = newsfeedInstance.getSingleNews(newsRowNumber - 2);
        }
        else {
            newsArrayLeft = newsfeedInstance.getSingleNews(currentCenterNewsIndex - 1);
            newsArrayRight = newsfeedInstance.getSingleNews(currentCenterNewsIndex + 1);
        }
    }

    void newsfeedUpdate(Newsfeed newsfeedInstance, int swipeDirection){
        if (swipeDirection == -1){ // left swipe
            newsArrayRight = newsArrayCenter;
            newsArrayCenter = newsArrayLeft; //DB version.
            newsTitle.setText(newsArrayCenter[0]);
            newsMainText.setText(newsArrayCenter[1]);
            currentCenterNewsIndex -= 1;
            if (currentCenterNewsIndex == 0){
                newsArrayLeft = newsfeedInstance.getSingleNews(newsRowNumber);
            }
            else {
                newsArrayLeft = newsfeedInstance.getSingleNews(currentCenterNewsIndex - 1);
            }
        }
        else if (swipeDirection == 1) { // right swipe
            newsArrayLeft = newsArrayCenter;
            newsArrayCenter = newsArrayRight; //DB version.
            newsTitle.setText(newsArrayCenter[0]);
            newsMainText.setText(newsArrayCenter[1]);
            currentCenterNewsIndex += 1;
            if (currentCenterNewsIndex == newsRowNumber - 1) {
                newsArrayRight = newsfeedInstance.getSingleNews((0));
            } else {
                newsArrayRight = newsfeedInstance.getSingleNews(currentCenterNewsIndex + 1);
            }
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

                    //detect left to right swipe
                    if(x2>x1){
                        //TODO display already loaded newsfeed array index-1
                        //TODO load newsfeed array index-2 from database (on index+1 or shift all before)
                        //This will be Bens job :DD leftIndex, currentIndex and rightIndex, currentIndex is the

                        //toast to see if it works
                        Toast.makeText(this,"Right is swiped",Toast.LENGTH_SHORT).show();
                        newsfeedUpdate(newsfeedInstance, 1);
                    }
                    else{
                        //detects right to left swipe
                        //TODO display already loaded newsfeed array index+1
                        //TODO load newsfeed array index+2 from database (on index-1 or shift all before)
                        //This will be Bens job :DD

                        //toast to see if it works
                        Toast.makeText(this,"Left is swiped",Toast.LENGTH_SHORT).show();
                        newsfeedUpdate(newsfeedInstance, -1);
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
