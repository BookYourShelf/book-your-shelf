package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "orders")
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

    public enum PaymentStatus{
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

    public enum PaymentOption{
        CREDIT_CARD,
        PAYPAL,
        TRANSFERRING_MONEY_PTT
    }

    public enum OrderStatus{
        PENDING,
        CONFIRMED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;

    @ElementCollection
    private List<Integer> productId;

    private int userId;
    private String userName;
    private Timestamp orderDate;
    private String customerAddress;
    private String billingAddress;
    private String shippingCompany;
    private String shippingMethod;
    private String totalAmount;
    private String subTotal;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Enumerated(EnumType.STRING)
    PaymentOption paymentOption;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;


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

    public List<Integer> getProductId() {
        return productId;
    }

    public void setProductId(List<Integer> productId) {
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

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
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

    public String getShippingMethod() { return shippingMethod; }

    public void setShippingMethod(String shippingMethod) { this.shippingMethod = shippingMethod; }

    public String getTotalAmount() { return totalAmount; }

    public void setTotalAmount(String totalAmount) { this.totalAmount = totalAmount; }

    public String getSubTotal() { return subTotal; }

    public void setSubTotal(String subTotal) { this.subTotal = subTotal; }

    public OrderStatus getOrderStatus() { return orderStatus; }

    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }
}
