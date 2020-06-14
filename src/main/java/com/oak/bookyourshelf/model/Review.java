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

    private String reviewTitle;

    @Column(length = 1000)
    @Length(min = 1, max = 1000)
    private String reviewContent;

    private int scoreOutOf5;
    private Timestamp uploadDate;


    @ManyToOne
    private Product product;

    @ManyToOne
    private User user;

    // GETTER & SETTER

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
