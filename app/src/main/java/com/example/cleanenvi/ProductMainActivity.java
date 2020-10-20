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

    DBHelper mDBHelper ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_main);
        this.setTitle((CharSequence)"Produktsuche");

        Button searchproductbtn = findViewById(id.productSearchbtn);
        Button databasebtn = findViewById(id.databaseBtn);

        //Initalisierung der Datenbankeinträge
        mDBHelper.addData("En-green-dot","6");
        mDBHelper.addData("Plastique","1");
        mDBHelper.addData("Carton","3");
        mDBHelper.addData("Sachet","1");
        mDBHelper.addData("Frais","6");
        mDBHelper.addData("Verre","4");
        mDBHelper.addData("Bouteille","6");
        mDBHelper.addData("Barquette","6");
        mDBHelper.addData("Boite","6");
        mDBHelper.addData("Kunststoff","1");
        mDBHelper.addData("Bocal","4");
        mDBHelper.addData("Metal","1");
        mDBHelper.addData("Plastic","1");
        mDBHelper.addData("Conserve","6");
        mDBHelper.addData("Pot","6");
        mDBHelper.addData("Surgele","6");
        mDBHelper.addData("Glas","4");
        mDBHelper.addData("Plastico","1");
        mDBHelper.addData("Sachet-plastique","1");
        mDBHelper.addData("Papier","3");
        mDBHelper.addData("Karton","3");
        mDBHelper.addData("Sous-atmosphere-protectrice","6");
        mDBHelper.addData("Aluminium","1");
        mDBHelper.addData("Stuck","6");
        mDBHelper.addData("Brique","6");
        mDBHelper.addData("Flasche","6");
        mDBHelper.addData("Sous-vide","6");
        mDBHelper.addData("Dose","1");
        mDBHelper.addData("Film","6");
        mDBHelper.addData("Etui","6");
        mDBHelper.addData("Couvercle","1");
        mDBHelper.addData("Plastik","1");
        mDBHelper.addData("Fr-film-en-plastique","1");
        mDBHelper.addData("Tute","1");
        mDBHelper.addData("Bottle","6");
        mDBHelper.addData("Box","6");
        mDBHelper.addData("Tetra-pak","1");
        mDBHelper.addData("Opercule","6");
        mDBHelper.addData("Flacon","6");
        mDBHelper.addData("05-pp","1");
        mDBHelper.addData("Film-plastique","1");
        mDBHelper.addData("Produkt","6");
        mDBHelper.addData("Bolsa-de-plastico","1");
        mDBHelper.addData("Glass","4");
        mDBHelper.addData("Bag","1");
        mDBHelper.addData("Plastica","1");
        mDBHelper.addData("Bolsa","1");
        mDBHelper.addData("Paquet","6");
        mDBHelper.addData("Barquette-plastique","1");
        mDBHelper.addData("Pet","1");
        mDBHelper.addData("Fr-point-vert","6");
        mDBHelper.addData("Pp","1");
        mDBHelper.addData("Pot-en-verre","4");
        mDBHelper.addData("Canette","1");
        mDBHelper.addData("Lata","1");
        mDBHelper.addData("♷","1");
        mDBHelper.addData("Becher","1");
        mDBHelper.addData("Bouteille-plastique","1");
        mDBHelper.addData("Fr-barquette","6");
        mDBHelper.addData("Beutel","1");
        mDBHelper.addData("Boite-carton","3");
        mDBHelper.addData("Caja","6");
        mDBHelper.addData("Pape","3");
        mDBHelper.addData("Fr-pensez-au-tri","6");
        mDBHelper.addData("Bouteille-en-verre","4");
        mDBHelper.addData("Pots","6");
        mDBHelper.addData("Bouchon","6");
        mDBHelper.addData("Refrigerado","6");
        mDBHelper.addData("Surgeles","6");
        mDBHelper.addData("Packung-en","6");
        mDBHelper.addData("Can","1");
        mDBHelper.addData("Cardboard","3");
        mDBHelper.addData("Canned","1");
        mDBHelper.addData("Bouteille-verre","4");
        mDBHelper.addData("Atmosphere-protectrice","6");
        mDBHelper.addData("Fr-etui-en-carton","6");
        mDBHelper.addData("21-pap","3");
        mDBHelper.addData("Etui-carton-a-recycler","6");
        mDBHelper.addData("Tube","1");
        mDBHelper.addData("Sachets","6");
        mDBHelper.addData("Vidrio","4");
        mDBHelper.addData("Bocal-en-verre","4");
        mDBHelper.addData("Bocal-verre","4");
        mDBHelper.addData("Pot-plastique","1");
        mDBHelper.addData("Pap","3");
        mDBHelper.addData("Doypack","6");
        mDBHelper.addData("Caja-de-carton","3");
        mDBHelper.addData("Fr-plastique","1");
        mDBHelper.addData("Product","6");
        mDBHelper.addData("Mehrwegpfand","5");
        mDBHelper.addData("Jar","4");
        mDBHelper.addData("Plastic-bag","1");
        mDBHelper.addData("Bote-de-vidrio","4");
        mDBHelper.addData("Botella","6");
        mDBHelper.addData("Pappe","3");
        mDBHelper.addData("Point-vert","6");
        mDBHelper.addData("Bois","2");
        mDBHelper.addData("Capsule","6");
        mDBHelper.addData("Tablette","6");
        mDBHelper.addData("Konserve","6");
        mDBHelper.addData("Tetrapak","1");
        mDBHelper.addData("Glasflasche","4");
        mDBHelper.addData("Alu","1");
        mDBHelper.addData("Tetra-brik","1");
        mDBHelper.addData("Fr-sachet-en-plastique","1");
        mDBHelper.addData("Envasado-en-atmosfera-protectora","6");
        mDBHelper.addData("Folie","1");
        mDBHelper.addData("Carta","3");
        mDBHelper.addData("Boite-en-carton","3");
        mDBHelper.addData("Boite-plastique","1");
        mDBHelper.addData("07-o","1");
        mDBHelper.addData("Fr-carton","1");
        mDBHelper.addData("En-bottle","6");
        mDBHelper.addData("Pack","6");
        mDBHelper.addData("Couvercle-metal","1");
        mDBHelper.addData("01-pet","1");
        mDBHelper.addData("Plastic-bottle","1");
        mDBHelper.addData("Gourde","1");
        mDBHelper.addData("Film-plastique-a-jeter","1");
        mDBHelper.addData("Packet","6");
        mDBHelper.addData("Green-dot","6");
        mDBHelper.addData("Tetrapack","1");
        mDBHelper.addData("Botella-de-plastico","1");
        mDBHelper.addData("Fr-triman","6");
        mDBHelper.addData("Fr-bocal-en-verre","1");
        mDBHelper.addData("Fresh","6");
        mDBHelper.addData("Frozen","6");
        mDBHelper.addData("Etui-carton","3");
        mDBHelper.addData("Film-en-plastique","1");
        mDBHelper.addData("40-fe","1");
        mDBHelper.addData("5-pp","1");
        mDBHelper.addData("Pot-en-plastique","1");
        mDBHelper.addData("En-conserva","6");
        mDBHelper.addData("Ultracongelado","6");
        mDBHelper.addData("Tarrina","6");
        mDBHelper.addData("Producto","6");
        mDBHelper.addData("Tarro","4");
        mDBHelper.addData("♳","1");
        mDBHelper.addData("Sachet-plastique-a-jeter","1");
        mDBHelper.addData("Blister","6");
        mDBHelper.addData("Emballage","6");
        mDBHelper.addData("Decongele","6");
        mDBHelper.addData("Fr-barquette-en-plastique","1");
        mDBHelper.addData("Fe","1");
        mDBHelper.addData("Refrigere","6");
        mDBHelper.addData("Filet","1");
        mDBHelper.addData("Glass-bottle","4");
        mDBHelper.addData("Cartone","3");
        mDBHelper.addData("Plastic-film","1");
        mDBHelper.addData("Pot-verre","4");
        mDBHelper.addData("Fr-frais","6");
        mDBHelper.addData("Barquette-et-film-plastique-a-jeter","1");
        mDBHelper.addData("Bidon","6");
        mDBHelper.addData("Aluminio","1");
        mDBHelper.addData("Barquette-en-plastique","1");
        mDBHelper.addData("Papel","3");
        mDBHelper.addData("Einwegpfand","5");
        mDBHelper.addData("Squeezer","1");
        mDBHelper.addData("Gl","4");
        mDBHelper.addData("Pensez-au-tri","6");
        mDBHelper.addData("Glass-jar","4");
        mDBHelper.addData("Vetro","4");
        mDBHelper.addData("Fr-etui-carton-a-recycler","3");
        mDBHelper.addData("Sac","6");
        mDBHelper.addData("Bac","6");
        mDBHelper.addData("Packed","6");
        mDBHelper.addData("Couvercle-ouverture-facile","6");
        mDBHelper.addData("Tarrina-de-plastico","1");
        mDBHelper.addData("Brick","6");
        mDBHelper.addData("70-gl","4");
        mDBHelper.addData("Dose-n","1");
        mDBHelper.addData("Congelado","6");
        mDBHelper.addData("Fr-sachet","6");
        mDBHelper.addData("Schale","6");
        mDBHelper.addData("Sec","6");
        mDBHelper.addData("Fr-sachet-plastique-a-jeter","1");
        mDBHelper.addData("Triman","6");
        mDBHelper.addData("Fr-couvercle-en-metal","1");
        mDBHelper.addData("41-alu","1");
        mDBHelper.addData("Fr-capsule-en-metal","1");
        mDBHelper.addData("Boite-metal","1");
        mDBHelper.addData("Flacon-verre","4");
        mDBHelper.addData("Sac-plastique","1");
        mDBHelper.addData("Bokaal","6");
        mDBHelper.addData("Fr-bouteille-en-plastique","1");
        mDBHelper.addData("Bouchon-plastique","1");
        mDBHelper.addData("Plast","1");
        mDBHelper.addData("Boite-de-conserve","1");
        mDBHelper.addData("Acier","1");
        mDBHelper.addData("Feuille","3");
        mDBHelper.addData("Tray","6");
        mDBHelper.addData("Bote","6");
        mDBHelper.addData("Glas-glaser","4");
        mDBHelper.addData("Pasteurizado","6");
        mDBHelper.addData("Sachet-papier","4");
        mDBHelper.addData("Bouteille-en-plastique","1");
        mDBHelper.addData("Plastikowe","1");
        mDBHelper.addData("Fr-bouteille-en-verre","4");
        mDBHelper.addData("Tetra-brik-aseptic","1");
        mDBHelper.addData("A-jeter","6");
        mDBHelper.addData("Es-botella","6");
        mDBHelper.addData("Conditionne-sous-atmosphere-protectrice","6");
        mDBHelper.addData("Cup","1");
        mDBHelper.addData("Foil","6");
        mDBHelper.addData("A-recycler","6");
        mDBHelper.addData("Fles","6");
        mDBHelper.addData("Al-vacio","6");
        mDBHelper.addData("Hdpe","1");
        mDBHelper.addData("Elopak","6");
        mDBHelper.addData("Sachets-individuels","6");
        mDBHelper.addData("Plastic-tray","1");
        mDBHelper.addData("Flasche-n","6");
        mDBHelper.addData("84-c-pap","1");
        mDBHelper.addData("1-pet","1");
        mDBHelper.addData("Emballage-plastique","1");
        mDBHelper.addData("Sachet-individuel","6");
        mDBHelper.addData("Botella-de-vidrio","4");
        mDBHelper.addData("Cristal","4");

        searchproductbtn.setOnClickListener(new OnClickListener() {
            public final void onClick(View it) {
                ProductMainActivity.this.startActivity(new Intent((Context)ProductMainActivity.this, ProductSearchActivity.class));
            }
        });

        databasebtn.setOnClickListener(new OnClickListener() {
            public final void onClick(View it) {
                ProductMainActivity.this.startActivity(new Intent((Context)ProductMainActivity.this, DataMainActivity.class));
            }
        });
    }
}
