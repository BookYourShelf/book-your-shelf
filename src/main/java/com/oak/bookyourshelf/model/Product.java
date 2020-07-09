package com.oak.bookyourshelf.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    private int stock;
    private int salesNum;

    @ElementCollection
    private List<Integer> totalStarNum = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));

    private float price;
    private float discountRate;
    private boolean onDiscount;
    private Timestamp uploadDate;
    private String productName;
    private String barcode;

    @Column(length = 1000)
    @Length(min = 1, max = 1000)
    private String shortDesc;

    @Column(length = 5000)
    @Length(min = 1, max = 5000)
    private String longDesc;

    @ElementCollection
    private Set<Integer> buyerUserIds;

    @OneToMany(orphanRemoval = true)
    private Set<Review> reviews;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    private Set<User> onWishList;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    private Set<User> onShoppingCart;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RemindProduct> remind;

    // FUNCTIONS

    public void increaseStarNum(int star) {
        this.totalStarNum.set(star, this.totalStarNum.get(star) + 1);
    }

    public void decreaseStarNum(int star) {
        this.totalStarNum.set(star, this.totalStarNum.get(star) - 1);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public Image getCoverImage() {     // cover image of the product is the first image in images
        return images.toArray(new Image[0])[0];
    }

    public String getEncodedCoverImage() {
        return Base64.getEncoder().encodeToString(images.toArray(new Image[0])[0].getImage());
    }

    public double getScoreOutOf5() {        // calculate score of the product
        float total = 0;

        for (int i = 0; i < 5; i++) {
            total += (i + 1) * totalStarNum.get(i);
        }

        int starNum = getStarNum();

        if (starNum == 0) {
            return 0;
        } else {
            return Math.round(total / getStarNum() * 10) / 10.0;
        }
    }

    public int getStarNum() {
        int num = 0;

        for (int i = 0; i < 5; i++) {
            num += totalStarNum.get(i);
        }

        return num;
    }

    public int getTotalReviewNum() {
        return reviews.size();
    }

    public void increaseSalesNum() {
        this.salesNum++;
    }

    public void decreaseSalesNum() {
        this.salesNum--;
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

    public float getDiscountRate() {
        return discountRate;
    }

    public boolean isOnDiscount() {
        return onDiscount;
    }

    public Timestamp getUploadDate() {
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

    public Set<Integer> getBuyerUserIds() {
        return buyerUserIds;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public List<Image> getImages() {
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

    public void setDiscountRate(float saleRate) {
        this.discountRate = saleRate;
    }

    public void setOnDiscount(boolean onSale) {
        this.onDiscount = onSale;
    }

    public void setUploadDate(Timestamp uploadDate) {
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

    public void setBuyerUserIds(Set<Integer> buyerUserIds) {
        this.buyerUserIds = buyerUserIds;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public abstract Object getProductTypeName();

    public Set<RemindProduct> getRemind() {
        return remind;
    }

    public void setRemind(Set<RemindProduct> remind) {
        this.remind = remind;
    }
}
