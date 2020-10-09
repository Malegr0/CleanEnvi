package com.example.cleanenvi;


import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.cleanenvi.Openfoodfacts;


import androidx.appcompat.app.AppCompatActivity;


public final class ProductSearchActivity extends AppCompatActivity {

    EditText productSearchEdit;
    TextView TextEAN;
    String EAN;
    Button searchBtn;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_search);
        this.setTitle((CharSequence)"Manuelle Produktsuche per Nummer");

        productSearchEdit =  findViewById(R.id.productSearchEdit);
        TextEAN = findViewById(R.id.txtEAN);


        searchBtn = findViewById(R.id.searchbtn);

        watcher(productSearchEdit, searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                TextEAN.setText(EAN);
                new OpenFoodFacts().execute();
            }
        });
    }

    void watcher(final EditText productSearchEdit,final Button searchBtn)
    {
        //final TextView txt = (TextView) findViewById(R.id.txtCounter);
        productSearchEdit.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                //txt.setText(productSearchEdit.length() + " / 160"); //This is my textwatcher to update character left in my EditText
                if(productSearchEdit.length() == 0)
                    searchBtn.setEnabled(false); //Button disabled
                else
                    searchBtn.setEnabled(true);  //Button enabled
                EAN = productSearchEdit.getText().toString();

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
        });
        if(productSearchEdit.length() == 0) searchBtn.setEnabled(false);//Am Anfang wird Button disabled
    }

    private class OpenFoodFacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Openfoodfacts.main(EAN);
            return null;
        }
    }
}
