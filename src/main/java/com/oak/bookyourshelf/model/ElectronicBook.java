package com.oak.bookyourshelf.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class ElectronicBook extends Book {

    public enum FileFormat {
        EPUB,
        PDF;
    }

    private int pageNum;
    private int fileSize;

    @Enumerated(EnumType.STRING)
    private FileFormat fileFormat;

    // GETTER & SETTER

    public String getProductTypeName() {
        return "E-Book";
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
    }
}
