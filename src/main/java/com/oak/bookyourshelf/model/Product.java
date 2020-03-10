package com.oak.bookyourshelf.model;

import java.awt.image.BufferedImage;
import java.sql.Date;
import java.util.ArrayList;

public abstract class Product {

    private int id;
    private int totalReviewNum;
    private int stock;
    private int[] totalStarNum;
    private float price;
    private float saleRate;
    private float scoreOutOf5;
    private boolean onSale;
    private Date uploadDate;
    private String shortDesc;
    private String longDesc;
    private String barcode;
    private BufferedImage coverImg;
    private ArrayList<Review> reviews;
    private ArrayList<BufferedImage> images;

    public int getId() {
        return id;
    }

    public int getTotalReviewNum() {
        return totalReviewNum;
    }

    public int getStock() {
        return stock;
    }

    public int[] getTotalStarNum() {
        return totalStarNum;
    }

    public float getPrice() {
        return price;
    }

    public float getSaleRate() {
        return saleRate;
    }

    public float getScoreOutOf5() {
        return scoreOutOf5;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public String getBarcode() {
        return barcode;
    }

    public BufferedImage getCoverImg() {
        return coverImg;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public ArrayList<BufferedImage> getImages() {
        return images;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTotalReviewNum(int totalReviewNum) {
        this.totalReviewNum = totalReviewNum;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setTotalStarNum(int[] totalStarNum) {
        this.totalStarNum = totalStarNum;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setSaleRate(float saleRate) {
        this.saleRate = saleRate;
    }

    public void setScoreOutOf5(float scoreOutOf5) {
        this.scoreOutOf5 = scoreOutOf5;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setCoverImg(BufferedImage coverImg) {
        this.coverImg = coverImg;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public void setImages(ArrayList<BufferedImage> images) {
        this.images = images;
    }
}
