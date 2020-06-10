package com.oak.bookyourshelf.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    private int productId;
    private int userId;
    private String reviewTitle;

    @Column(length = 1000)
    @Length(min = 1, max = 1000)
    private String reviewContent;

    private int scoreOutOf5;
    private Timestamp uploadDate;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> user;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Product> product;

    // GETTER & SETTER

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public int getScoreOutOf5() {
        return scoreOutOf5;
    }

    public void setScoreOutOf5(int scoreOutOf5) {
        this.scoreOutOf5 = scoreOutOf5;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }
}
