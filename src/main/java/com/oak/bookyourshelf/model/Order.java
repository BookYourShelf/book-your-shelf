package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    private static final float MIN_SHIPPING = 100f;
    private static final float SHIPPING_PRICE = 6.99f;
    private static final float NEXT_DAY_DEL_PRICE = 7.99f;

    public enum DeliveryStatus {
        INFO_RECEIVED,
        IN_TRANSIT,
        OUT_FOR_DELIVERY,
        FAILED_ATTEMPT,
        DELIVERED,
        EXCEPTION,
        EXPIRED,
        PENDING,
        CANCELED
    }

    public enum PaymentStatus {
        PENDING,
        PROCESSED,
        COMPLETED,
        REFUNDED,
        FAILED,
        EXPIRED,
        REVOKED,
        PREAPPROVED,
        CANCELLED,
        NULL
    }

    public enum PaymentOption {
        CREDIT_CARD,
        PAYPAL,
        TRANSFERRING_MONEY_PTT
    }

    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        CANCELED
    }

    public enum ShippingMethod {
        FREE,
        NEXT_DAY_DELIVERY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<CartItem> products;

    private int userId;
    private String userName;
    private Timestamp orderDate;
    private String shippingCompany;
    private String orderCode;
    private float subTotalAmount;
    private float totalAmount;
    private String couponCode;
    private float discountRate;

    @ElementCollection
    private Set<String> deliveryAddress;

    @ElementCollection
    private Set<String> billingAddress;

    @Enumerated(EnumType.STRING)
    private ShippingMethod shippingMethod;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Enumerated(EnumType.STRING)
    private PaymentOption paymentOption;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // FUNCTIONS

    public int getTotalNumberOfProducts() {
        int total = 0;
        for (CartItem c : products) {
            total += c.getQuantity();
        }
        return total;
    }

    public float calculateTotalAmount() {
        float totalAmount = this.subTotalAmount;
        if (totalAmount < MIN_SHIPPING) {
            totalAmount += SHIPPING_PRICE;
        }

        if (this.shippingMethod == ShippingMethod.NEXT_DAY_DELIVERY) {
            totalAmount += NEXT_DAY_DEL_PRICE;
        }

        if (this.discountRate != 0) {
            totalAmount -= (this.subTotalAmount * this.discountRate);
        }
        return totalAmount;
    }

    // GETTER & SETTER

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setSubTotalAmount(float subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Set<CartItem> getProducts() {
        return products;
    }

    public void setProducts(Set<CartItem> products) {
        this.products = products;
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

    public Set<String> getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Set<String> deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Set<String> getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Set<String> billingAddress) {
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

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }
}
