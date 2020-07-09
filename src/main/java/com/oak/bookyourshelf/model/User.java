package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class User {

    public enum Roles {
        USER(0),
        ADMIN(1);

        private int role;

        Roles(int role) {
            this.role = role;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private int role;
    private String email;
    private String name;
    private String surname;
    private String birthDate;
    private String phoneNumber;
    private Boolean receiveMessage = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Address> billingAddresses;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Address> deliveryAddresses;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    private Set<CartItem> shoppingCart;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    private Set<Product> wishList;

    private String password;

    @ElementCollection
    private Map<String, Integer> searchHistory;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Order> orders;

    public User() {
    }

    // FUNCTIONS

    public void addToWishList(Product product) {
        this.wishList.add(product);
    }

    public void addToCart(CartItem product) {
        this.shoppingCart.add(product);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public ArrayList<Product> getProductsPurchased() {

        ArrayList<Product> products = new ArrayList<>();
        Set<CartItem> orderProducts = new HashSet<>();
        if (getOrders() != null) {
            for (Order o : getOrders())
                orderProducts.addAll(o.getProducts());

            for (CartItem c : orderProducts)
                products.add(c.getProduct());
        }

        return products;
    }

    // GETTER & SETTER

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<CartItem> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(Set<CartItem> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Set<Product> getWishList() {
        return wishList;
    }

    public void setWishList(Set<Product> wishList) {
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

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<Address> getBillingAddresses() {
        return billingAddresses;
    }

    public void setBillingAddresses(Set<Address> billingAddresses) {
        this.billingAddresses = billingAddresses;
    }

    public Set<Address> getDeliveryAddresses() {
        return deliveryAddresses;
    }

    public void setDeliveryAddresses(Set<Address> deliveryAddresses) {
        this.deliveryAddresses = deliveryAddresses;
    }

    public Boolean getReceiveMessage() {
        return receiveMessage;
    }

    public void setReceiveMessage(Boolean receiveMeesage) {
        this.receiveMessage = receiveMeesage;
    }

}
