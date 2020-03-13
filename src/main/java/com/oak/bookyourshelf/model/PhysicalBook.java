package com.oak.bookyourshelf.model;

public class PhysicalBook extends Book {

    private int pageNum;
    private int editionNum;
    private float height;
    private float width;
    enum MediaType { HARDCOVER, SOFTCOVER, POCKET_SIZE, TABLET_SIZE; }
    enum PaperType { COATED, UN_COATED, AMERICAN_BRISTOL, TRACING, CANVAS, CELLOPHANE, CRAFT, MATTE_COATED_PLASTER; }
    enum BindingType { SADDLE_STITCHING , CASE_BINDING, CASE_WRAPPED, PLASTIC_COIL, PERFECT_BINDING; }
    
    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getEditionNum() {
        return editionNum;
    }

    public void setEditionNum(int editionNum) {
        this.editionNum = editionNum;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

}
