package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.util.List;

@Entity
public abstract class Book extends Product {

    private int publishedYear;
    private String language;
    private String isbn;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> category;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subcategory> subcategory;

    @ElementCollection
    private List<String> publishers;

    @ElementCollection
    private List<String> translators;

    @ElementCollection
    private List<String> authors;

    @ElementCollection
    private List<String> keywords;

    // GETTER & SETTER

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public List<Subcategory> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(List<Subcategory> subcategory) {
        this.subcategory = subcategory;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getLanguage() {
        return language;
    }

    public List<String> getTranslators() {
        return translators;
    }

    public String getIsbn() {
        return isbn;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTranslators(List<String> translators) {
        this.translators = translators;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
