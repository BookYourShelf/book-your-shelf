package com.oak.bookyourshelf.model;

public class ElectronicBookReader extends Product {
    private String electronicBookColor;
    private String electronicBookBrand;
    private String electronicBookModel;
    private String technicalSpecifications;
    private float weight;
    private float width;
    private float height;
    private float screenWidth;
    private float screenHeight;

    public String getElectronicBookColor() {
        return electronicBookColor;
    }

    public void setElectronicBookColor(String electronicBookColor) {
        this.electronicBookColor = electronicBookColor;
    }

    public String getElectronicBookBrand() {
        return electronicBookBrand;
    }

    public void setElectronicBookBrand(String electronicBookBrand) {
        this.electronicBookBrand = electronicBookBrand;
    }

    public String getElectronicBookModel() {
        return electronicBookModel;
    }

    public void setElectronicBookModel(String electronicBookModel) {
        this.electronicBookModel = electronicBookModel;
    }

    public String getTechnicalSpecifications() {
        return technicalSpecifications;
    }

    public void setTechnicalSpecifications(String technicalSpecifications) {
        this.technicalSpecifications = technicalSpecifications;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(float screenHeight) {
        this.screenHeight = screenHeight;
    }
}
