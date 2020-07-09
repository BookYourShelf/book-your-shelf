package com.oak.bookyourshelf.model;

import javax.persistence.*;

@Entity
public abstract class BookCase extends Product {

    private String caseColor;
    private String caseBrand;
    private String caseModel;
    private float caseHeight;
    private float caseWidth;

    // GETTER & SETTER

    public float getCaseHeight() {
        return caseHeight;
    }

    public void setCaseHeight(float caseHeight) {
        this.caseHeight = caseHeight;
    }

    public float getCaseWidth() {
        return caseWidth;
    }

    public void setCaseWidth(float caseWidth) {
        this.caseWidth = caseWidth;
    }

    public void setCaseColor(String caseColor) {
        this.caseColor = caseColor;
    }

    public void setCaseBrand(String caseBrand) {
        this.caseBrand = caseBrand;
    }

    public void setCaseModel(String caseModel) {
        this.caseModel = caseModel;
    }

    public String getCaseColor() {
        return caseColor;
    }

    public String getCaseBrand() {
        return caseBrand;
    }

    public String getCaseModel() {
        return caseModel;
    }
}
