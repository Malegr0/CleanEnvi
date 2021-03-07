package com.example.cleanenvi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

public final class ProductSearchActivity extends AppCompatActivity {
    EditText productSearchEdit;
    public static String EAN;
    ImageButton searchBtn;
    ImageView HisPro1, HisPro2, HisPro3, HisPro4, HisPro5;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_search);
        this.setTitle("Produktsuche");
        productSearchEdit =  findViewById(R.id.productSearchEdit);
        searchBtn = findViewById(R.id.searchbtn);
        HisPro1 = findViewById(R.id.historyImage1);
        HisPro2 = findViewById(R.id.historyImage2);
        HisPro3 = findViewById(R.id.historyImage3);
        HisPro4 = findViewById(R.id.historyImage4);
        HisPro5 = findViewById(R.id.historyImage5);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_search);

        watcher(productSearchEdit, searchBtn);

        //History loading

        // Nutella https://www.testberichte.de/imgs/p_imgs_370/11/11709.jpg
        Picasso.get().load("https://www.testberichte.de/imgs/p_imgs_370/11/11709.jpg").into(HisPro1);
        Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSebbILIsbkl0u52kWQxmBofyJcYhoCWq5_qA&usqp=CAU").into(HisPro2);
        Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ3Md53AHlyPg5COqwMdgQsDjuVX3ScetOucg&usqp=CAU").into(HisPro3);
        Picasso.get().load("https://www.testberichte.de/imgs/p_imgs_370/11/11709.jpg").into(HisPro4);
        Picasso.get().load("https://www.testberichte.de/imgs/p_imgs_370/11/11709.jpg").into(HisPro5);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                ProductSearchActivity.this.startActivity(new Intent(ProductSearchActivity.this, com.example.cleanenvi.productmanager.ProductShowActivity.class));
            }
        });

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
    void watcher(final EditText productSearchEdit, final ImageButton searchBtn) {
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
