package com.oak.bookyourshelf.model;

import java.util.ArrayList;

public class Subcategory {

    private int itemNum;
    private String name;
    private ArrayList<Subcategory> subcategories;

    // GETTER & SETTER

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

    public ArrayList<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }
}
