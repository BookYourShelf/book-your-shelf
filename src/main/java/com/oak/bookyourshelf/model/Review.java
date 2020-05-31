package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    private int productId;
    private int userId;
    private String userName;
    private String userSurname;
    private String reviewTitle;
    private String reviewContent;
    private int scoreOutOf5;
    private Date uploadDate;

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

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
