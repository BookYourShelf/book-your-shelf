package com.oak.bookyourshelf.model;

import javax.persistence.Entity;

@Entity
public class ElectronicBookReader extends Product {

    private String readerColor;
    private String readerBrand;
    private String readerModel;
    private String technicalSpecifications;
    private float weight;
    private float width;
    private float height;
    private float depth;
    private float screenSize;
    private float batteryLife;
    private float resolution;
    private int ram;
    private boolean glareFreeScreen;
    private boolean waterProof;
    private boolean autoAdjustingLight;
    private boolean buttons;
    private boolean wifi;
    private boolean cellular;
    private boolean backLight;
    private boolean warmBackLight;

    // GETTER & SETTER

    public String getProductTypeName() {
        return "E-Book Reader";
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public String getReaderColor() {
        return readerColor;
    }

    public void setReaderColor(String readerColor) {
        this.readerColor = readerColor;
    }

    public String getReaderBrand() {
        return readerBrand;
    }

    public void setReaderBrand(String readerBrand) {
        this.readerBrand = readerBrand;
    }

    public String getReaderModel() {
        return readerModel;
    }

    public void setReaderModel(String readerModel) {
        this.readerModel = readerModel;
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

    public float getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(float screenSize) {
        this.screenSize = screenSize;
    }

    public float getBatteryLife() {
        return batteryLife;
    }

    public void setBatteryLife(float batteryLife) {
        this.batteryLife = batteryLife;
    }

    public float getResolution() {
        return resolution;
    }

    public void setResolution(float resolution) {
        this.resolution = resolution;
    }

    public boolean isGlareFreeScreen() {
        return glareFreeScreen;
    }

    public void setGlareFreeScreen(boolean glareFreeScreen) {
        this.glareFreeScreen = glareFreeScreen;
    }

    public boolean isWaterProof() {
        return waterProof;
    }

    public void setWaterProof(boolean waterProof) {
        this.waterProof = waterProof;
    }

    public boolean isAutoAdjustingLight() {
        return autoAdjustingLight;
    }

    public void setAutoAdjustingLight(boolean autoAdjustingLight) {
        this.autoAdjustingLight = autoAdjustingLight;
    }

    public boolean isButtons() {
        return buttons;
    }

    public void setButtons(boolean buttons) {
        this.buttons = buttons;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isCellular() {
        return cellular;
    }

    public void setCellular(boolean cellular) {
        this.cellular = cellular;
    }

    public boolean isBackLight() {
        return backLight;
    }

    public void setBackLight(boolean backLight) {
        this.backLight = backLight;
    }

    public boolean isWarmBackLight() {
        return warmBackLight;
    }

    public void setWarmBackLight(boolean warmBackLight) {
        this.warmBackLight = warmBackLight;
    }
}
