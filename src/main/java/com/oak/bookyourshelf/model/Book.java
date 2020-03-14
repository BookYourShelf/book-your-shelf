package com.oak.bookyourshelf.model;

import java.util.ArrayList;

public abstract class Book extends Product {

    private int bookId;
    private int publishedYear;
    private String bookName;
    private String language;
    private String translator;
    private String isbn;
    private String publisher;
    private ArrayList<String> authors;
    private ArrayList<String> keywords;

    // GETTER & SETTER

    public int getBookId() {
        return bookId;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public String getBookName() {
        return bookName;
    }

    public ArrayList<String> getAuthors() {
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

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public void setBookName(String name) {
        this.bookName = name;
    }

    public void setAuthors(ArrayList<String> authors) {
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

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }
}
