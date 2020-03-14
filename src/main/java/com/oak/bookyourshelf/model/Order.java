package com.oak.bookyourshelf.model;

import java.sql.Date;
import java.util.ArrayList;

public class Order {

    private static final float MIN_SHIPPING = 100f;
    private static final float SHIPPING_PRICE = 6.99f;

    private enum DeliveryStatus{
        INFO_RECEIVED,
        IN_TRANSIT,
        OUT_FOR_DELIVERY,
        FAILED_ATTEMPT,
        DELIVERED,
        EXCEPTION,
        EXPIRED,
        PENDING
    }

    private enum PaymentStatus{
        PENDING,
        PROCESSED,
        COMPLETED,
        REFUNDED,
        FAILED,
        EXPIRED,
        REVOKED,
        PREAPPROVED,
        CANCELLED
    }

    private enum PaymentOption{
        CREDIT_CARD,
        PAYPAL,
        TRANSFERRING_MONEY_PTT
    }

    private int orderId;
    private ArrayList<Integer> productId;
    private int userId;
    private String userName;
    private Date orderDate;
    private String customerAddress;
    private String billingAddress;
    private String shippingCompany;
    private DeliveryStatus deliveryStatus;
    private PaymentOption paymentOption;
    private PaymentStatus paymentStatus;

    public float getTotalAmountOfShipping(){

        float totalAmount = 0;
        /* TODO: find sum of products */
        if (totalAmount < MIN_SHIPPING){
            totalAmount += SHIPPING_PRICE;
        }
        return totalAmount;
    }

    // GETTER & SETTER

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public ArrayList<Integer> getProductId() {
        return productId;
    }

    public void setProductId(ArrayList<Integer> productId) {
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public PaymentOption getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(PaymentOption paymentOption) {
        this.paymentOption = paymentOption;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
