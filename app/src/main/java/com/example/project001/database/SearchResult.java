package com.example.project001.database;

public class SearchResult {

    String departure;
    String destination;
    String driver;
    String seats;
    String price;
    String date;
    String time;


    public SearchResult(String departure, String destination, String driver, String seats, String price, String date, String time) {
        this.departure = departure;
        this.destination = destination;
        this.driver = driver;
        this.seats = seats;
        this.price = price;
        this.date = date;
        this.time = time;
    }


    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public String getDriver() {
        return driver;
    }

    public String getSeats() {
        return seats;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
