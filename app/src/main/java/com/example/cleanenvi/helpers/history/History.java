package com.example.cleanenvi.helpers.history;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class History implements Serializable {

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

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.write(ean.getBytes());
        out.write(name.getBytes());
        out.write(imageUrl.getBytes());
        //out.close();
    }

    /*private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    }*/

    private void readObjectNoData() throws ObjectStreamException {

    }

}
