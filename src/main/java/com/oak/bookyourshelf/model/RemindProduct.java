package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class RemindProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int remindProductId;

    private int productId;
    private int userId;
    private Timestamp requestTime;
    private Timestamp availableTime;

    public enum ProductAvailability{
        AVAILABLE,
        PENDING
    }

    @Enumerated(EnumType.STRING)
    private ProductAvailability productAvailability;

    public int getRemindProductId() { return remindProductId; }

    public void setRemindProductId(int remindProductId) { this.remindProductId = remindProductId; }

    public int getProductId() { return productId; }

    public void setProductId(int productId) { this.productId = productId; }

    public Timestamp getRequestTime() { return requestTime; }

    public void setRequestTime(Timestamp requestTime) { this.requestTime = requestTime; }

    public ProductAvailability getProductAvailability() { return productAvailability; }

    public void setProductAvailability(ProductAvailability productAvailability) { this.productAvailability = productAvailability; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public Timestamp getAvailableTime() { return availableTime; }

    public void setAvailableTime(Timestamp availableTime) { this.availableTime = availableTime; }
}
