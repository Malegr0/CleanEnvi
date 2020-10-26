package com.example.cleanenvi;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;



public final class ProductSearchActivity extends AppCompatActivity {

    EditText productSearchEdit;
    public static String EAN;
    Button searchBtn;
    DBHelper mDBHelper;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_search);
        this.setTitle((CharSequence)"Manuelle Produktsuche per Nummer");
        mDBHelper = new DBHelper(this);

        productSearchEdit =  findViewById(R.id.productSearchEdit);
        searchBtn = findViewById(R.id.searchbtn);

        //Aktiviert Button bei Eingabe und speichert Eingabe für später
        watcher(productSearchEdit, searchBtn);

        // ruft nächste Activity (für Verarbeitung der Anfrage) auf
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                ProductSearchActivity.this.startActivity(new Intent((Context)ProductSearchActivity.this, ProductShowActivity.class));
            }
        });

    }


    void watcher(final EditText productSearchEdit, final Button searchBtn)
    {
        productSearchEdit.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                if(productSearchEdit.length() == 0)

                    // Button ausgeschaltet wenn kein Zeichen im Edit-Feld
                    searchBtn.setEnabled(false);
                else

                    // Button eingeschaltet
                    searchBtn.setEnabled(true);

                // Variable zum Speichern der Eingabe
                EAN = productSearchEdit.getText().toString();

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
        });

        //Am Anfang wird Button ausgeschaltet
        if(productSearchEdit.length() == 0) searchBtn.setEnabled(false);
    }
}
