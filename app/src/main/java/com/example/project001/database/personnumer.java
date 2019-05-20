package com.example.project001.database;

public class personnumer {


    String numer ="fuckOff";
    String firstName ="fuckOff";
    String lastName ="fuckOff";
    String address ="fuckOff";
    String postcode ="fuckOff";
    String city ="fuckOff";
    String verified ="false";
    String email ="fuckOff";

    public personnumer(String numer, String firstName, String lastName, String address, String postcode, String city, String verified, String email) {
        this.numer = numer;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.verified = verified;
        this.email = email;
    }

    public String getNumber() {
        return numer;
    }

    public void setNumber(String numer) {
        this.numer = numer;
    }


    public String getNumer() {
        return numer;
    }

    public void setNumer(String numer) {
        this.numer = numer;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
