package com.oak.bookyourshelf.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
public class Category {

    public enum ProductType {
        BOOK("Book"),
        E_BOOK("E-Book"),
        AUDIO_BOOK("Audio Book"),
        E_BOOK_READER("E-Book Reader"),
        E_BOOK_READER_CASE("E-Book Reader Case"),
        BOOK_CASE("Book Case");

        private String description;

        ProductType(final String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private int itemNum;
    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Subcategory> subcategories;

    // GETTER & SETTER

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

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
}
