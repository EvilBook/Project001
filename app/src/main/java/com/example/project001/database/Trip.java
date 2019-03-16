package com.example.project001.database;

import android.os.Parcel;
import android.os.Parcelable;


public class Trip implements Parcelable {

    public String date;
    public String time;
    public String departure;
    public String destination;
    public String price;
    public String seats;
    public String author;


    public Trip(String date, String time, String departure, String destination, String price, String seats, String author) {
        this.date = date;
        this.time = time;
        this.departure = departure;
        this.destination = destination;
        this.price = price;
        this.seats = seats;
        this.author = author;
    }

    protected Trip(Parcel in) {
        date = in.readString();
        time = in.readString();
        departure = in.readString();
        destination = in.readString();
        price = in.readString();
        seats = in.readString();
        author = in.readString();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(departure);
        dest.writeString(destination);
        dest.writeString(price);
        dest.writeString(seats);
        dest.writeString(author);
    }


    @Override
    public String toString() {
        return  "Departure: " + departure + "\n" +
                "Destination: " + destination + "\n" +
                "Date: " + date + "\n" +
                "Time: " + time + "\n" +
                "Price: " + price + "\n" +
                "Seats: " + seats + "\n" +
                "Author: " + author;
    }
}
