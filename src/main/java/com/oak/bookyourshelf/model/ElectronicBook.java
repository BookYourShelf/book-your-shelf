package com.oak.bookyourshelf.model;

public class ElectronicBook extends Book {

    private int pageNum;
    private int fileSize;
    enum FileFormat { EPUB , PDF; }

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


}
