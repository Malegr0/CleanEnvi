package com.example.cleanenvi;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Point;
import com.mapbox.search.CategorySearchEngine;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.SearchCallback;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchRequestTask;
import com.mapbox.search.location.DefaultLocationProvider;
import com.mapbox.search.location.LocationProvider;
import com.mapbox.search.result.SearchResult;

import java.util.List;

public class SearchActivity extends AppCompatActivity{

    private CategorySearchEngine categorySearchEngine;
    private SearchRequestTask searchRequestTask;

    private final SearchCallback searchCallback = new SearchCallback() {

        @Override
        public void onResults(@NonNull List<? extends SearchResult> results) {
            if (results.isEmpty()) {
                Log.i("SearchApiExample", "No category search results");
            } else {
                Log.i("SearchApiExample", "Category search results: " + results);
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            Log.i("SearchApiExample", "Search error", e);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxSearchSdk.initialize(this, getString(R.string.mapbox_access_token), (LocationProvider) (new DefaultLocationProvider(this)));
        categorySearchEngine = MapboxSearchSdk.createCategorySearchEngine();

        final SearchOptions options = new SearchOptions.Builder()
                .limit(1)
                .build();
        searchRequestTask = categorySearchEngine.search("cafe", options, searchCallback);
    }

    @Override
    public void onDestroy() {
        searchRequestTask.cancel();
        super.onDestroy();
    }

}
