package com.example.travelplanapp;

public class City {
    private String country;
    private String city;
    private double amount;

    public City(String country, String city, double amount) {
        this.country = country;
        this.city = city;
        this.amount = amount;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
