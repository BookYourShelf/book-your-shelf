package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Subcategory {

    public enum ProductType {
        BOOK,
        E_BOOK,
        AUDIO_BOOK,
        E_BOOK_READER,
        E_BOOK_READER_CASE,
        BOOK_CASE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private Category.ProductType productType;

    private int itemNum;
    private String name;
    private boolean inCampaign = false;
    private boolean inHotList = false;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Subcategory> subcategories;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books;

    // GETTER & SETTER

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Set<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public boolean isInCampaign() {
        return inCampaign;
    }

    public void setInCampaign(boolean inCampaign) {
        this.inCampaign = inCampaign;
    }

    public boolean isInHotList() {
        return inHotList;
    }

    public void setInHotList(boolean inHotList) {
        this.inHotList = inHotList;
    }

    public Category.ProductType getProductType() {
        return productType;
    }

    public void setProductType(Category.ProductType productType) {
        this.productType = productType;
    }
}
