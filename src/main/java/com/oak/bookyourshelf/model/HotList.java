package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Entity
public class HotList {

    private enum HotListType {
        BEST_SELLERS,
        NEW_RELEASES,
        OUR_PICKS_FOR_YOU;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    private HotListType hotListType;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Category> categories;

    private int productNum;
    private Date startDate;
    private Time startTime;
    private Date endDate;
    private Time endTime;

    // GETTER & SETTER

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HotListType getHotListType() {
        return hotListType;
    }

    public void setHotListType(HotListType hotListType) {
        this.hotListType = hotListType;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
