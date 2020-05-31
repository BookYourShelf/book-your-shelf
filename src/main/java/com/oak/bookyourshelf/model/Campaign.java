package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int rate;
    private String name;
    private String startDate;
    private String endDate;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Category> categories;


    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Subcategory> subcategories;

    private Category.ProductType productType;

    // GETTER & SETTER

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

}
