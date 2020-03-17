package com.oak.bookyourshelf.model;


import javax.persistence.Entity;

@Entity

public class ElectronicBookReaderCase extends BookCase {

    private String readerBrand;
    private String readerModel;

    // GETTER & SETTER

    public void setReaderBrand(String readerBrand) {
        this.readerBrand = readerBrand;
    }

    public void setReaderModel(String readerModel) {
        this.readerModel = readerModel;
    }

    public String getReaderBrand() {
        return readerBrand;
    }

    public String getReaderModel() {
        return readerModel;
    }
}
