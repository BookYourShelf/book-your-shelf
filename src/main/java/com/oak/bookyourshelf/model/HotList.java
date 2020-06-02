package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Entity
public class HotList {

    private enum HotListType {
        BEST_SELLERS,
        NEW_RELEASES;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    private HotListType hotListType;

    @ManyToMany(
            cascade = CascadeType.ALL)
    private List<Category> categories;


    @ManyToMany(
            cascade = CascadeType.ALL)
    private List<Subcategory> subcategories;

    @ManyToMany(
            cascade = CascadeType.ALL)
    private List<Product> products;

    private int productNum;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private Category.ProductType productType;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Category.ProductType getProductType() {
        return productType;
    }

    public void setProductType(Category.ProductType productType) {
        this.productType = productType;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public void addSubcategory(Subcategory subcategory){this.getSubcategories().add(subcategory);}

    public String getCategoryName(){return this.getCategories().get(0).getName();}

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
