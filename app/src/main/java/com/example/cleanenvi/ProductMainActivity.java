package com.example.cleanenvi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cleanenvi.R.id;


public final class ProductMainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_main);
        this.setTitle((CharSequence)"Produktsuche");
        Button searchproductbtn = findViewById(id.productSearchbtn);
        Button databasebtn = findViewById(id.DatabaseBtn);

        searchproductbtn.setOnClickListener(new OnClickListener() {
            public final void onClick(View it) {
                ProductMainActivity.this.startActivity(new Intent((Context)ProductMainActivity.this, ProductSearchActivity.class));
            }
        });
        /*databasebtn.setOnClickListener(new OnClickListener() {
            public final void onClick(View it) {
                ProductMainActivity.this.startActivity(new Intent((Context)ProductMainActivity.this, SQLDatenbank.class));
            }
        });*/
    }
}
