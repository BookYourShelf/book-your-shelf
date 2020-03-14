package com.oak.bookyourshelf.model;

public class PhysicalBookCase extends BookCase  {

    private float caseWidth;
    private float caseHeight;

    // GETTER & SETTER

    public void setCaseWidth(float caseWidth) {
        this.caseWidth = caseWidth;
    }

    public void setCaseHeight(float caseHeight) {
        this.caseHeight = caseHeight;
    }

    public float getCaseWidth() {
        return caseWidth;
    }

    public float getCaseHeight() {
        return caseHeight;
    }
}
