package com.oak.bookyourshelf.model;

public  abstract class BookCase extends Product  {

    private String color;
    private String brand;
    private String caseModel;




    public void setColor(String color) {
        this.color = color;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCaseModel(String caseModel) {
        this.caseModel = caseModel;
    }

    public String getColor() {
        return color;
    }

    public String getBrand() {
        return brand;
    }

    public String getCaseModel() {
        return caseModel;
    }

}

