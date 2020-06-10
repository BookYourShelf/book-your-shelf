package com.oak.bookyourshelf.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int couponId;

    private Timestamp uploadDate;
    private String couponCode;
    private int discountRate;

   

    public int getCouponId() { return couponId; }

    public void setCouponId(int couponId) { this.couponId = couponId; }

    public Timestamp getUploadDate() { return uploadDate; }

    public void setUploadDate(Timestamp uploadDate) { this.uploadDate = uploadDate; }

    public String getCouponCode() { return couponCode; }

    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public int getDiscountRate() { return discountRate; }

    public void setDiscountRate(int discountRate) { this.discountRate = discountRate; }
}
