package com.example.cleanenvi;

public class Produkt {
    //fields
    private String prodname;
    private int entnum;

    //constructors


    public Produkt(String id, int entnum) {
        this.prodname = id;
        this.entnum = entnum;
    }

    //properties
    public void setName(String id) {
        this.prodname = id;
    }

    public String getName() {
        return this.prodname;
    }

    public void setentnum(int entnum) {
        this.entnum = entnum;
    }

    public int getentnum() {
        return this.entnum;
    }

}
