package com.example.cleanenvi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    DBHelper mDBHelper ;
    String tName1,tName2,tName3,tName4,tName5,tName6, tNameGes, tID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        Button buttonSearch = findViewById(R.id.button_search);
        Button buttonCameraSearch = findViewById(R.id.button_camera_search);
        Button buttonMap = findViewById(R.id.button_map);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_main);
        mDBHelper = new DBHelper(this);
        SQLiteDatabase db= mDBHelper.getWritableDatabase();

        //Automatisches Erstellen der Einträge der Datenbank (Name und Recyclingnummer)
        database();
        String[] testName = ProductShowActivity.splitInArray(tNameGes);
        String[] testID = ProductShowActivity.splitInArray(tID);

        //Initalisierung der Datenbankeinträge
        mDBHelper.delTable(db);
        add(testName, testID);

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
                switch (item.getItemId()) {
                    case R.id.action_search:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, ProductSearchActivity.class));
                        break;
                    case R.id.action_camera:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, CameraMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.action_hofkarte:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, MapActivity.class));
                        break;
                }
                return true;
            }
        });
    }

    //Hinzufügen aller Datenbankeinträge mithilfe eines Arrays
    void add(String[] testName, String[] testID) {
        for (int i=0; i < (testName.length); i++) {
            mDBHelper.addData(testName[i], testID[i]);
        }
    }

    //Hinterlegte Werte für Einträge der Datenbank (wird gelegentlich aktualisiert da noch nicht alle Verpackungen enthalten)
    void database() {
        tName1 = ("PLASTIQUE,CARTON,SACHET,FRAIS,VERRE,BOUTEILLE,BARQUETTE,BOITE,KUNSTSTOFF,BOCAL,METAL,PLASTIC,CONSERVE,POT,SURGELE,GLAS,PLASTICO,SACHET PLASTIQUE,PAPIER,KARTON,SOUS ATMOSPHERE PROTECTRICE,ALUMINIUM,STUCK,BRIQUE,FLASCHE,SOUS VIDE,DOSE,FILM,ETUI,COUVERCLE,PLASTIK,FR FILM EN PLASTIQUE,TUTE,BOTTLE,BOX,TETRA PAK,OPERCULE,FLACON,05 PP,FILM PLASTIQUE,PRODUKT,BOLSA DE PLASTICO,GLASS,BAG,PLASTICA,BOLSA,PAQUET,BARQUETTE PLASTIQUE,PET,FR POINT VERT,PP,POT EN VERRE,CANETTE,LATA,♷,");
        tName2 = ("BECHER,BOUTEILLE PLASTIQUE,FR BARQUETTE,BEUTEL,BOITE CARTON,CAJA,PAPER,FR PENSEZ AU TRI,BOUTEILLE EN VERRE,POTS,BOUCHON,REFRIGERADO,SURGELES,PACKUNG EN,CAN,CARDBOARD,CANNED,BOUTEILLE VERRE,ATMOSPHERE PROTECTRICE,FR ETUI EN CARTON,21 PAP,ETUI CARTON A RECYCLER,TUBE,SACHETS,VIDRIO,BOCAL EN VERRE,BOCAL VERRE,POT PLASTIQUE,PAP,DOYPACK,CAJA DE CARTON,FR PLASTIQUE,PRODUCT,MEHRWEGPFAND,JAR,EN GREEN DOT,PLASTIC BAG,BOTE DE VIDRIO,BOTELLA,PAPPE,POINT VERT,BOIS,CAPSULE,TABLETTE,");
        tName3 = ("KONSERVE,TETRAPAK,GLASFLASCHE,ALU,TETRA BRIK,FR SACHET EN PLASIQUE,ENVASADO EN ATMOSFERA PROTECTORA,FOLIE,CARTA,BOITE EN CARTON,BOITE PLASTIQUE,07 O,FR CARTON,EN BOTTLE,PACK,COUVERCLE METAL,01 PET,PLASTIC BOTTLE,GOURDE,FILM PLASTIQUE A JETER,PACKET,GREEN DOT,TETRAPACK,BOTELLA DE PLASTICO,FR TRIMAN,FR BOCAL EN VERRE,FRESH,FROZEN,ETUI CARTON,FILM EN PLASTIQUE,40 FE,5 PP,POT EN PLASTIQUE,EN CONSERVA,ULTRACONGELADO,TARRINA,PRODUCTO,TARRO,♳,SACHET PLASTIQUE A JETER,BLISTER,");
        tName4 = ("EMBALLAGE,DECONGELE,FR BARQUETTE EN PLASTIQUE,FE,REFRIGERE,FILET,GLASS BOTTLE,CARTONE,PLASTIC FILM,POT VERRE,FR FRAIS,BARQUETTE ET FILM PLASTIQUE A JETER,BIDON,ALUMINIO,BARQUETTE EN PLASTIQUE,PAPEL,EINWEGPFAND,SQUEEZER,GL,PENSEZ AU TRI,GLASS JAR,VETRO,FR ETUI CARTON A RECYCLER,SAC,BAC,PACKED,COUVERCLE OUVERTURE FACILE,TARRINA DE PLASTICO,BRICK,70 GL,DOSE(N),CONGELADO,FR SACHET,SCHALE,SEC,FR SACHET PLASTIQUE A JETER,TRIMAN,FR COUVERCLE EN METAL,41 ALU,FR CAPSULE EN METAL,");
        tName5 = ("BOITE METAL,FLACON VERRE,SAC PLASTIQUE,BOKAAL,FR BOUTEILLE EN PLASTIQUE,BOUCHON PLASTIQUE,PLAST,BOITE DE CONSERVE,ACIER,FEUILLE,TRAY,BOTE,GLAS GLASER,PASTEURIZADO,SACHET PAPIER,BOUTEILLE EN PLASTIQUE,PLASTIKOWE,FR BOUTEILLE EN VERRE,TETRA BRIK ASEPTIC,A JETER,ES BOTELLA,CONDITIONNE SOUS ATMOSPHERE PROTECTRICE,CUP,FOIL,A RECYCLER,FLES,AL VACIO,HDPE,ELOPAK,SACHETS INDIVIDUELS,PLASTIC TRAY,FLASCHE(N),84 C/PAP,1 PET,EMBALLAGE PLASTIQUE,SACHET INDIVIDUEL,BOTELLA DE VIDRIO,");
        tName6 = ("CRISTAL,SZKŁOS,ПЛАСТИКОВЫЕ,FR OPERCULE EN PLASTIQUE,PLASTIKTUTE,PET FLASCHE,PET-FLASCHE,PLASTIKBECHER,KUNSTSTOFFFOLIE,ПАКЕТ,PLASTIKBEUTEL,METALL,СТЕКЛО,BOITE DE CONSERVE ACIER");
        tNameGes = (tName1 + tName2 + tName3 + tName4 + tName5 + tName6);
        tID = ("1,3,1,6,4,6,6,6,1,4,1,1,6,6,6,4,1,1,3,3,1,1,6,6,6,6,1,6,6,1,1,1,1,6,6,1,6,6,1,1,6,1,4,1,1,1,6,1,1,6,1,4,1,1,1,1,1,6,1,3,6,3,6,4,6,6,6,6,6,1,3,1,4,6,6,3,6,1,6,4,4,4,1,3,6,3,1,6,5,4,6,1,4,6,3,6,2,6,6,6,1,4,1,1,1,6,1,3,3,1,1,1,6,6,1,1,1,1,1,6,6,1,1,6,1,6,6,3,1,1,1,1,6,6,6,6,4,1,1,6,6,6,1,1,6,1,4,3,1,4,6,1,6,1,1,3,5,1,4,6,4,4,3,6,6,6,1,1,6,4,1,6,6,6,6,1,6,1,1,1,1,4,1,6,1,1,1,1,1,3,6,6,4,6,4,1,1,4,1,6,6,6,1,6,6,6,6,1,6,6,1,6,1,1,1,6,4,4,4,1,1,1,1,1,1,1,1,1,1,4,1");
    }
}
