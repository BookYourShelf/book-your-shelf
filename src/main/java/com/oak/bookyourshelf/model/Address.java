package com.oak.bookyourshelf.model;

import javax.persistence.*;

@Entity
public class Address {
    public enum AddressTitle {
        HOME,
        WORKPLACE,
        SUMMERY,
        OTHER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int addressId;

    @Enumerated(EnumType.STRING)
    AddressTitle addressTitle;

    private String name;
    private String surname;
    private String country;
    private String city;
    private String province;
    private String neighborhood;
    private int zipCode;
    private String fullAddress;
    private String phoneNumber;

    // GETTER & SETTER

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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
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

}
