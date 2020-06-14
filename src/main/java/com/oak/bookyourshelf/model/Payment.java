package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Payment {

    public enum PaymentMethod {
        PAYMENT_METHOD_PAYPAL,
        PAYMENT_METHOD_CREDIT_CARD,
        PAYMENT_METHOD_BANK_TRANSFER
    }

    public enum PaymentResult {
        PAYMENT_RESULT_SUCCESS,
        PAYMENT_RESULT_DECLINED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int paymentId;

    int payerId;
    int orderId;
    float amount;
    Timestamp issueDate;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    PaymentResult paymentResult;

    // GETTER & SETTER
    
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    public Timestamp getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Timestamp issueDate) {
        this.issueDate = issueDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentResult getPaymentResult() {
        return paymentResult;
    }

    public void setPaymentResult(PaymentResult paymentResult) {
        this.paymentResult = paymentResult;
    }
}
