package com.oak.bookyourshelf.model;

import javax.persistence.Entity;

@Entity
public class AudioBook extends Book {

    private int lengthInMinutes;
    private String narratedBy;

    // GETTER & SETTER

    public String getProductTypeName() {
        return "Audio Book";
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public void setLengthInMinutes(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }

    public String getNarratedBy() {
        return narratedBy;
    }

    public void setNarratedBy(String narratedBy) {
        this.narratedBy = narratedBy;
    }
}
