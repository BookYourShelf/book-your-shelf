package com.oak.bookyourshelf.model;

import javax.persistence.Entity;

@Entity
public class PhysicalBookCase extends BookCase {

    private float maxBookDepth;

    // GETTER & SETTER

    public String getProductTypeName() {
        return "Book Case";
    }

    public float getMaxBookDepth() {
        return maxBookDepth;
    }

    public void setMaxBookDepth(float maxBookDepth) {
        this.maxBookDepth = maxBookDepth;
    }
}
