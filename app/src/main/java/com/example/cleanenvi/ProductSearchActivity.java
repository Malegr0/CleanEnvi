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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cleanenvi.helpers.history.History;
import com.example.cleanenvi.helpers.history.HistoryManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public final class ProductSearchActivity extends AppCompatActivity {
    EditText productSearchEdit;
    public static String EAN;
    ImageButton searchBtn;
    ImageView hisPro1, hisPro2, hisPro3, hisPro4, hisPro5;
    TextView hisProText1, hisProText2, hisProText3, hisProText4, hisProText5;
    Button hisProBut1, hisProBut2, hisProBut3, hisProBut4, hisProBut5;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_search);
        this.setTitle("Produktsuche");
        productSearchEdit =  findViewById(R.id.productSearchEdit);
        searchBtn = findViewById(R.id.searchbtn);
        hisPro1 = findViewById(R.id.historyImage1);
        hisProText1 = findViewById(R.id.historyText1);
        hisProBut1 = findViewById(R.id.historyTextButton1);
        hisPro2 = findViewById(R.id.historyImage2);
        hisProText2 = findViewById(R.id.historyText2);
        hisProBut2 = findViewById(R.id.historyTextButton2);
        hisPro3 = findViewById(R.id.historyImage3);
        hisProText3 = findViewById(R.id.historyText3);
        hisProBut3 = findViewById(R.id.historyTextButton3);
        hisPro4 = findViewById(R.id.historyImage4);
        hisProText4 = findViewById(R.id.historyText4);
        hisProBut4 = findViewById(R.id.historyTextButton4);
        hisPro5 = findViewById(R.id.historyImage5);
        hisProText5 = findViewById(R.id.historyText5);
        hisProBut5 = findViewById(R.id.historyTextButton5);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_search);

        watcher(productSearchEdit, searchBtn);

        //History loading
        try {
            HistoryManager.loadHistories(this);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        loadHistoriesIntoLayoutFields(HistoryManager.getHistories());

        initButtonListener();

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

    private void initButtonListener() {
        if(!HistoryManager.getHistories()[0].getEan().equals("")) {
            hisProBut1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EAN = HistoryManager.getHistories()[0].getEan();
                    ProductSearchActivity.this.startActivity(new Intent(ProductSearchActivity.this,
                            com.example.cleanenvi.productmanager.ProductShowActivity.class));
                }
            });
            if(!HistoryManager.getHistories()[1].getEan().equals("")) {
                hisProBut2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EAN = HistoryManager.getHistories()[1].getEan();
                        ProductSearchActivity.this.startActivity(new Intent(ProductSearchActivity.this,
                                com.example.cleanenvi.productmanager.ProductShowActivity.class));
                    }
                });
                if(!HistoryManager.getHistories()[2].getEan().equals("")) {
                    hisProBut3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EAN = HistoryManager.getHistories()[2].getEan();
                            ProductSearchActivity.this.startActivity(new Intent(ProductSearchActivity.this,
                                    com.example.cleanenvi.productmanager.ProductShowActivity.class));
                        }
                    });
                    if(!HistoryManager.getHistories()[3].getEan().equals("")) {
                        hisProBut4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EAN = HistoryManager.getHistories()[3].getEan();
                                ProductSearchActivity.this.startActivity(new Intent(ProductSearchActivity.this,
                                        com.example.cleanenvi.productmanager.ProductShowActivity.class));
                            }
                        });
                        if(!HistoryManager.getHistories()[4].getEan().equals("")) {
                            hisProBut5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EAN = HistoryManager.getHistories()[4].getEan();
                                    ProductSearchActivity.this.startActivity(new Intent(ProductSearchActivity.this,
                                            com.example.cleanenvi.productmanager.ProductShowActivity.class));
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private void loadHistoriesIntoLayoutFields(History[] histories) {
        if (!histories[0].getEan().equals("")) {
            hisProText1.setText(histories[0].getName());
            Picasso.get().load(histories[0].getImageUrl()).into(hisPro1);
            if (!histories[1].getEan().equals("")) {
                hisProText2.setText(histories[1].getName());
                Picasso.get().load(histories[1].getImageUrl()).into(hisPro2);
                if (!histories[2].getEan().equals("")) {
                    hisProText3.setText(histories[2].getName());
                    Picasso.get().load(histories[2].getImageUrl()).into(hisPro3);
                    if (!histories[3].getEan().equals("")) {
                        hisProText4.setText(histories[3].getName());
                        Picasso.get().load(histories[3].getImageUrl()).into(hisPro4);
                        if (!histories[4].getEan().equals("")) {
                            hisProText5.setText(histories[4].getName());
                            Picasso.get().load(histories[4].getImageUrl()).into(hisPro5);
                        }
                    }
                }
            }
        }
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
