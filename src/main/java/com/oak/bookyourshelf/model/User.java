package com.oak.bookyourshelf.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    @NotEmpty(message = "*Please Provide an E-Mail Address")
    @Email(message = "*Please Provide a valid E-Mail Address")
    private String email;
    @Pattern(regexp = "[a-zA-Z ]+$", message = "*Please enter a valid Name")
    @NotEmpty(message = "*Please Provide your Name")
    private String name;
    @Pattern(regexp = "[a-zA-Z ]+$", message = "*Please enter a valid Surname")
    @NotEmpty(message = "*Please Provide your SurName")
    private String surname;
    private Date birthDate;
    private String phoneNumber;
    @NotNull(message = "*Please accept the terms and conditions.")
    private String checked;
    @NotNull(message = "*Please read and approve the membership agreement")
    private String checkedAgain;


    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Review> reviews;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Address> billingAddresses;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Address> deliveryAddresses;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Product> shoppingCart;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Product> wishList;
    @NotEmpty(message = "*Please Provide your Password")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,16}$", message = "*Please enter a valid Password")
    @Transient
    transient private String password;

    @ElementCollection
    private Map<String, Integer> searchHistory;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Order> orders;

    public User() {
    }

    // FUNCTIONS

    public ArrayList<Product> getProductsPurchased() {

        ArrayList<Product> products = new ArrayList<>();
        // TODO get all products from orders
        return products;
    }

    // GETTER & SETTER

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Product> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(List<Product> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public List<Product> getWishList() {
        return wishList;
    }

    public void setWishList(List<Product> wishList) {
        this.wishList = wishList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Integer> getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(Map<String, Integer> searchHistory) {
        this.searchHistory = searchHistory;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Address> getBillingAddresses() {
        return billingAddresses;
    }

    public void setBillingAddresses(List<Address> billingAddresses) {
        this.billingAddresses = billingAddresses;
    }

    public List<Address> getDeliveryAddresses() {
        return deliveryAddresses;
    }

    public void setDeliveryAddresses(List<Address> deliveryAddresses) {
        this.deliveryAddresses = deliveryAddresses;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getCheckedAgain() {
        return checkedAgain;
    }

    public void setCheckedAgain(String checkedAgain) {
        this.checkedAgain = checkedAgain;
    }
}
