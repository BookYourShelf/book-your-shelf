package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int productId;

    private int stock;
    private int salesNum;

    @ElementCollection
    private List<Integer> totalStarNum = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));

    private float price;
    private float saleRate;
    private boolean onSale;
    private Date uploadDate;
    private String productName;
    private String shortDesc;
    private String longDesc;
    private String barcode;

    @ElementCollection
    private List<Integer> buyerUserIds;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Review> reviews;

    @ElementCollection
    private List<byte[]> images;

    // FUNCTIONS

    public byte[] getCoverImage() {     // cover image of the product is the first image in images
        return images.get(0);
    }

    public float getScoreOutOf5() {        // calculate score of the product
        float total = 0;

        for (int i = 0; i < 5; i++) {
            total += (i+1) * totalStarNum.get(i);
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

    public List<Integer> getTotalStarNum() {
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

    public List<Integer> getBuyerUserIds() {
        return buyerUserIds;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<byte[]> getImages() {
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

    public void setTotalStarNum(List<Integer> totalStarNum) {
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

    public void setBuyerUserIds(List<Integer> buyerUserIds) {
        this.buyerUserIds = buyerUserIds;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }
}
