package com.oak.bookyourshelf.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

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
    private ProductType productType;

    private int itemNum;
    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Subcategory> subcategories;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> books;

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

    private void traverseSubcategories(Subcategory subcategory, List<Book> bookList) {
        bookList.addAll(subcategory.getBooks());
        for (Subcategory sub : subcategory.getSubcategories()) {
            bookList.addAll(sub.getBooks());
            traverseSubcategories(sub, bookList);
        }
    }

    public List<Book> getBooks() {
        List<Book> bookList = new ArrayList<>(books);
        for (Subcategory sub : this.subcategories) {
            traverseSubcategories(sub, bookList);
        }

        return bookList;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
