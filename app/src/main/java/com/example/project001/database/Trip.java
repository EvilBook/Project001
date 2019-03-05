package com.example.project001.database;

public class Trip {

    private String destination;
    private String departure;
    private String date;
    private String price;
    private String availableSeats;
    private String freeSeats;
    private String author;

    public Trip(String destination, String departure, String date, String price, String availableSeats, String freeSeats, String author) {
        this.destination = destination;
        this.departure = departure;
        this.date = date;
        this.price = price;
        this.availableSeats = availableSeats;
        this.freeSeats = freeSeats;
        this.author = author;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(String availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(String freeSeats) {
        this.freeSeats = freeSeats;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.freeSeats = author;
    }

}
