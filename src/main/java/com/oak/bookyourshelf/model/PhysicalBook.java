package com.oak.bookyourshelf.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class PhysicalBook extends Book {

    public enum MediaType {
        HARDCOVER,
        SOFTCOVER,
        POCKET_SIZE,
        TABLET_SIZE,
        MONTHLY;
    }

    public enum PaperType {
        COATED,
        UN_COATED,
        AMERICAN_BRISTOL,
        TRACING,
        CANVAS,
        CELLOPHANE,
        CRAFT,
        MATTE_COATED_PLASTER;
    }

    public enum BindingType {
        SADDLE_STITCHING,
        CASE_BINDING,
        CASE_WRAPPED,
        PLASTIC_COIL,
        PERFECT_BINDING;
    }

    private int pageNum;
    private int editionNum;
    private float height;
    private float width;
    private float depth;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Enumerated(EnumType.STRING)
    private PaperType paperType;

    @Enumerated(EnumType.STRING)
    private BindingType bindingType;

    // GETTER & SETTER

    public String getProductTypeName() {
        return "Book";
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

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

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public PaperType getPaperType() {
        return paperType;
    }

    public void setPaperType(PaperType paperType) {
        this.paperType = paperType;
    }

    public BindingType getBindingType() {
        return bindingType;
    }

    public void setBindingType(BindingType bindingType) {
        this.bindingType = bindingType;
    }
}
