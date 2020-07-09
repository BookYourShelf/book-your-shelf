package com.oak.bookyourshelf.model;

import javax.persistence.Entity;

@Entity
public class ElectronicBookReaderCase extends BookCase {

    private String readerBrand;
    private String readerModel;
    private float caseDepth;

    // GETTER & SETTER

    public String getProductTypeName() {
        return "E-Book Reader Case";
    }

    public float getCaseDepth() {
        return caseDepth;
    }

    public void setCaseDepth(float caseDepth) {
        this.caseDepth = caseDepth;
    }

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
