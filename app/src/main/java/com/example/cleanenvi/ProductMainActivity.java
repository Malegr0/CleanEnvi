package com.example.cleanenvi;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cleanenvi.R.id;


public final class ProductMainActivity extends AppCompatActivity {

    DBHelper mDBHelper ;
    String tName1,tName2,tName3,tName4,tName5,tName6, tNameGes, tID;
    ProductShowActivity mProduct;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.product_main);
        this.setTitle((CharSequence)"Produktsuche");

        Button searchproductbtn = findViewById(id.productSearchbtn);
        Button databasebtn = findViewById(id.databaseBtn);
        Button codeScannerbtn = findViewById(id.codeScannerbtn);
        tName1 = ("PLASTIQUE,CARTON,SACHET,FRAIS,VERRE,BOUTEILLE,BARQUETTE,BOITE,KUNSTSTOFF,BOCAL,METAL,PLASTIC,CONSERVE,POT,SURGELE,GLAS,PLASTICO,SACHET PLASTIQUE,PAPIER,KARTON,SOUS ATMOSPHERE PROTECTRICE,ALUMINIUM,STUCK,BRIQUE,FLASCHE,SOUS VIDE,DOSE,FILM,ETUI,COUVERCLE,PLASTIK,FR FILM EN PLASTIQUE,TUTE,BOTTLE,BOX,TETRA PAK,OPERCULE,FLACON,05 PP,FILM PLASTIQUE,PRODUKT,BOLSA DE PLASTICO,GLASS,BAG,PLASTICA,BOLSA,PAQUET,BARQUETTE PLASTIQUE,PET,FR POINT VERT,PP,POT EN VERRE,CANETTE,LATA,♷,");
        tName2 = ("BECHER,BOUTEILLE PLASTIQUE,FR BARQUETTE,BEUTEL,BOITE CARTON,CAJA,PAPER,FR PENSEZ AU TRI,BOUTEILLE EN VERRE,POTS,BOUCHON,REFRIGERADO,SURGELES,PACKUNG EN,CAN,CARDBOARD,CANNED,BOUTEILLE VERRE,ATMOSPHERE PROTECTRICE,FR ETUI EN CARTON,21 PAP,ETUI CARTON A RECYCLER,TUBE,SACHETS,VIDRIO,BOCAL EN VERRE,BOCAL VERRE,POT PLASTIQUE,PAP,DOYPACK,CAJA DE CARTON,FR PLASTIQUE,PRODUCT,MEHRWEGPFAND,JAR,EN GREEN DOT,PLASTIC BAG,BOTE DE VIDRIO,BOTELLA,PAPPE,POINT VERT,BOIS,CAPSULE,TABLETTE,");
        tName3 = ("KONSERVE,TETRAPAK,GLASFLASCHE,ALU,TETRA BRIK,FR SACHET EN PLASIQUE,ENVASADO EN ATMOSFERA PROTECTORA,FOLIE,CARTA,BOITE EN CARTON,BOITE PLASTIQUE,07 O,FR CARTON,EN BOTTLE,PACK,COUVERCLE METAL,01 PET,PLASTIC BOTTLE,GOURDE,FILM PLASTIQUE A JETER,PACKET,GREEN DOT,TETRAPACK,BOTELLA DE PLASTICO,FR TRIMAN,FR BOCAL EN VERRE,FRESH,FROZEN,ETUI CARTON,FILM EN PLASTIQUE,40 FE,5 PP,POT EN PLASTIQUE,EN CONSERVA,ULTRACONGELADO,TARRINA,PRODUCTO,TARRO,♳,SACHET PLASTIQUE A JETER,BLISTER,");
        tName4 = ("EMBALLAGE,DECONGELE,FR BARQUETTE EN PLASTIQUE,FE,REFRIGERE,FILET,GLASS BOTTLE,CARTONE,PLASTIC FILM,POT VERRE,FR FRAIS,BARQUETTE ET FILM PLASTIQUE A JETER,BIDON,ALUMINIO,BARQUETTE EN PLASTIQUE,PAPEL,EINWEGPFAND,SQUEEZER,GL,PENSEZ AU TRI,GLASS JAR,VETRO,FR ETUI CARTON A RECYCLER,SAC,BAC,PACKED,COUVERCLE OUVERTURE FACILE,TARRINA DE PLASTICO,BRICK,70 GL,DOSE(N),CONGELADO,FR SACHET,SCHALE,SEC,FR SACHET PLASTIQUE A JETER,TRIMAN,FR COUVERCLE EN METAL,41 ALU,FR CAPSULE EN METAL,");
        tName5 = ("BOITE METAL,FLACON VERRE,SAC PLASTIQUE,BOKAAL,FR BOUTEILLE EN PLASTIQUE,BOUCHON PLASTIQUE,PLAST,BOITE DE CONSERVE,ACIER,FEUILLE,TRAY,BOTE,GLAS GLASER,PASTEURIZADO,SACHET PAPIER,BOUTEILLE EN PLASTIQUE,PLASTIKOWE,FR BOUTEILLE EN VERRE,TETRA BRIK ASEPTIC,A JETER,ES BOTELLA,CONDITIONNE SOUS ATMOSPHERE PROTECTRICE,CUP,FOIL,A RECYCLER,FLES,AL VACIO,HDPE,ELOPAK,SACHETS INDIVIDUELS,PLASTIC TRAY,FLASCHE(N),84 C/PAP,1 PET,EMBALLAGE PLASTIQUE,SACHET INDIVIDUEL,BOTELLA DE VIDRIO,");
        tName6 = ("CRISTAL,SZKŁOS,ПЛАСТИКОВЫЕ,FR OPERCULE EN PLASTIQUE,PLASTIKTUTE,PET FLASCHE,PET-FLASCHE,PLASTIKBECHER,KUNSTSTOFFFOLIE,ПАКЕТ,PLASTIKBEUTEL,METALL,СТЕКЛО,BOITE DE CONSERVE ACIER");
        tNameGes = (tName1 + tName2 + tName3 + tName4 + tName5 + tName6);
        tID = ("1,3,1,6,4,6,6,6,1,4,1,1,6,6,6,4,1,1,3,3,1,1,6,6,6,6,1,6,6,1,1,1,1,6,6,1,6,6,1,1,6,1,4,1,1,1,6,1,1,6,1,4,1,1,1,1,1,6,1,3,6,3,6,4,6,6,6,6,6,1,3,1,4,6,6,3,6,1,6,4,4,4,1,3,6,3,1,6,5,4,6,1,4,6,3,6,2,6,6,6,1,4,1,1,1,6,1,3,3,1,1,1,6,6,1,1,1,1,1,6,6,1,1,6,1,6,6,3,1,1,1,1,6,6,6,6,4,1,1,6,6,6,1,1,6,1,4,3,1,4,6,1,6,1,1,3,5,1,4,6,4,4,3,6,6,6,1,1,6,4,1,6,6,6,6,1,6,1,1,1,1,4,1,6,1,1,1,1,1,3,6,6,4,6,4,1,1,4,1,6,6,6,1,6,6,6,6,1,6,6,1,6,1,1,1,6,4,4,4,1,1,1,1,1,1,1,1,1,1,4,1");

        final String[] testName = ProductShowActivity.splitInArray(tNameGes);
        final String[] testID = ProductShowActivity.splitInArray(tID);

        mDBHelper = new DBHelper(this);
        SQLiteDatabase db= mDBHelper.getWritableDatabase();

        //Initalisierung der Datenbankeinträge
        mDBHelper.delTable(db);
        foo(testName, testID);

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

        codeScannerbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductMainActivity.this.startActivity(new Intent((Context)ProductMainActivity.this, CameraMainActivity.class));
            }
        });
    }

    void foo(String[] testName, String[] testID) {
        for (int i=0; i < testName.length; i++) {
            mDBHelper.addData(testName[i], testID[i]);
        }
    }
}
