package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.util.List;

@Entity
public abstract class Book extends Product {

    private int publishedYear;
    private String bookName;
    private String language;
    private String translator;
    private String isbn;
    private String publisher;

    @ElementCollection
    private List<String> authors;

    @ElementCollection
    private List<String> keywords;

    // GETTER & SETTER

    public int getPublishedYear() {
        return publishedYear;
    }

    public String getBookName() {
        return bookName;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getLanguage() {
        return language;
    }

    public String getTranslator() {
        return translator;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public void setBookName(String name) {
        this.bookName = name;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
