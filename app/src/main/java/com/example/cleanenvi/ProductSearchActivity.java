package com.example.cleanenvi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public final class ProductSearchActivity extends AppCompatActivity {
    EditText productSearchEdit;
    public static String EAN;
    Button searchBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_search);
        this.setTitle("Produktsuche");
        productSearchEdit =  findViewById(R.id.productSearchEdit);
        searchBtn = findViewById(R.id.searchbtn);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_search);

        watcher(productSearchEdit, searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                ProductSearchActivity.this.startActivity(new Intent(ProductSearchActivity.this, com.example.cleanenvi.productmanager.ProductShowActivity.class));
            }
        });


        //Varianten zum Verhindern des Layoutflackerns
        bottomNavigationView.getMenu().getItem(1).setEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    ProductSearchActivity.this.startActivity(new Intent(ProductSearchActivity.this, MainActivity.class));
                } else if(id == R.id.action_search) {
                    ProductSearchActivity.this.startActivity(new Intent(ProductSearchActivity.this, ProductSearchActivity.class));
                } else if(id == R.id.action_camera) {
                    ProductSearchActivity.this.startActivity(new Intent(ProductSearchActivity.this, CameraMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                } else if (id == R.id.action_hofkarte) {
                    ProductSearchActivity.this.startActivity(new Intent(ProductSearchActivity.this, MapActivity.class));
                }
                return true;
            }
        });
    }

    //Aktiviert Button bei Texteingabe und speichert Eingabe für spätere Verarbeitung
    void watcher(final EditText productSearchEdit, final Button searchBtn) {
        productSearchEdit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                searchBtn.setEnabled(productSearchEdit.length() != 0);
                EAN = productSearchEdit.getText().toString();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){ }
            public void onTextChanged(CharSequence s, int start, int before, int count){ }
        });

        //Am Anfang wird Button ausgeschaltet
        if(productSearchEdit.length() == 0) searchBtn.setEnabled(false);
    }
}
