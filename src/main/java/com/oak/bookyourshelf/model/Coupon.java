package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int couponId;

    private Timestamp uploadDate;
    private String couponCode;
    private float discountRate;

    @ElementCollection
    private List<Integer> userId;

    public int getCouponId() { return couponId; }

    public void setCouponId(int couponId) { this.couponId = couponId; }

    public Timestamp getUploadDate() { return uploadDate; }

    public void setUploadDate(Timestamp uploadDate) { this.uploadDate = uploadDate; }

    public String getCouponCode() { return couponCode; }

    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public float getDiscountRate() { return discountRate; }

    public void setDiscountRate(float discountRate) { this.discountRate = discountRate; }

    public List<Integer> getUserId() { return userId; }

    public void setUserId(List<Integer> userId) { this.userId = userId; }
}
