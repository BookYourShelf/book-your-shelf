package com.oak.bookyourshelf.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Address {
    public enum AddressTitle {
        HOME,
        WORKPLACE,
        SUMMERY,
        OTHER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressId;

    @Enumerated(EnumType.STRING)
    AddressTitle addressTitle;

    private String name;
    private String surname;
    private String country;
    private String city;
    private String town;
    private int zipCode;
    private String fullAddress;
    private String phoneNumber;
    private Timestamp date;

    // GETTER & SETTER

    public Set<String> getOrderAddress() {
        Set<String> orderAddress = new HashSet<>();
        orderAddress.add(this.fullAddress);
        orderAddress.add(this.town + " / " + this.city + " / " + this.country + " " + this.zipCode);
        orderAddress.add(this.name + " " + this.surname + " - " + this.phoneNumber);
        return orderAddress;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public AddressTitle getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(AddressTitle addressTitle) {
        this.addressTitle = addressTitle;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Timestamp getTime() { return date; }

    public void setTime(Timestamp time) { this.date = time; }
}
