package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.util.Base64;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    private byte[] image;

    // FUNCTIONS

    public String encodeImage() {
        return Base64.getEncoder().encodeToString(this.getImage());
    }

    // GETTER & SETTER

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
