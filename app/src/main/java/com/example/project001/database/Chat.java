package com.example.project001.database;

public class Chat {

    private String driver;
    private String id;
    private String passenger;

    public Chat(String driver, String id, String passenger) {
        this.driver = driver;
        this.id = id;
        this.passenger = passenger;
        
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }
}
