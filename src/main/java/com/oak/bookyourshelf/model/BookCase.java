package com.oak.bookyourshelf.model;

public  abstract class BookCase extends Product  {

    private String caseColor;
    private String caseBrand;
    private String caseModel;




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

