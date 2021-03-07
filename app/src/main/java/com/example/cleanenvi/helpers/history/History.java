package com.example.cleanenvi.helpers.history;

public class History  {

    private String ean, name, imageUrl;

    public History(String ean, String name, String imageUrl) {
        this.ean = ean;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getEan() {
        return ean;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
