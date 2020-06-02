package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int itemNum;
    private String name;
    private boolean inCampaign=false;
    private boolean inHotList=false;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Subcategory> subcategories;

    @ManyToMany(cascade = CascadeType.ALL )
    private List<Book> books;

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

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
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
}
