package com.example.cleanenvi;


public class SQLDatenbank {

    


}



    public void addProdukt (int entnum, String Material) {
    DBHandler dbHandler = new DBHandler(this, null, null, 1);
    /*int entnum = 6;
    String Material = "Box";*/
    entnum = 6;
    Material = "Box";
    Produkt produkt = new Produkt(Material, entnum);
    dbHandler.addHandler(Produkt);
    entnum.setText("6");

}