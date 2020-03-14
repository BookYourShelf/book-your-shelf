package com.oak.bookyourshelf.model;

import java.sql.Date;
import java.util.ArrayList;

public abstract class Product {

    private int productId;
    private int stock;
    private int salesNum;
    private int[] totalStarNum = {0, 0, 0, 0, 0};
    private float price;
    private float saleRate;
    private boolean onSale;
    private Date uploadDate;
    private String productName;
    private String shortDesc;
    private String longDesc;
    private String barcode;
    private ArrayList<Integer> buyerUserIds;
    private ArrayList<Review> reviews;
    private ArrayList<byte[]> images;

    public byte[] getCoverImage() {     // cover image of the product is the first image in images
        return images.get(0);
    }

    public float getScoreOutOf5() {        // calculate score of the product
        float total = 0;

        for (int i = 0; i < 5; i++) {
            total += (i+1) * totalStarNum[i];
        }

        return total/5;
    }

    public int getTotalReviewNum() {
        return reviews.size();
    }

    public void increaseSalesNum() {
        salesNum++;
    }

    // GETTER & SETTER

    public int getProductId() {
        return productId;
    }

    public int getStock() {
        return stock;
    }

    public int getSalesNum() {
        return salesNum;
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

    public boolean isOnSale() {
        return onSale;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public String getProductName() {
        return productName;
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

    public ArrayList<Integer> getBuyerUserIds() {
        return buyerUserIds;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public ArrayList<byte[]> getImages() {
        return images;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
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

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public void setBuyerUserIds(ArrayList<Integer> buyerUserIds) {
        this.buyerUserIds = buyerUserIds;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public void setImages(ArrayList<byte[]> images) {
        this.images = images;
    }
}
