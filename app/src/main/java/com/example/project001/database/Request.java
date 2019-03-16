package com.example.project001.database;

public class Request {
    //tripId, passenger, driver, soolean

    public String tripId;

    public Request(String tripId, String passenger, String driver, String soolean) {
        this.tripId = tripId;
        this.passenger = passenger;
        this.driver = driver;
        this.soolean = soolean;
    }

    public String passenger;
    public String driver;
    public String soolean;


    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getSoolean() {
        return soolean;
    }

    public void setSoolean(String soolean) {
        this.soolean = soolean;
    }
}

