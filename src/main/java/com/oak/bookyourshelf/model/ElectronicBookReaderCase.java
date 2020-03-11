package com.oak.bookyourshelf.model;

public class ElectronicBookReaderCase extends BookCase {

    private String readerBrand;
    private String readerModel;

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
