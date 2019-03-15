package com.example.project001.message;

import android.os.Parcel;
import android.os.Parcelable;


public class TripObject implements Parcelable{

    String destinationfrom;
    String destinatinationTo;
    int Hr;
    int Min;
    int Month;
    int Day;
    int year;
    int numberOfSeat;
    String price;
    String licensePlate;


    public TripObject(String destinationfrom, String destinatinationTo, int hr, int min, int month, int day, int year,String price,String licensePlate,
                      int numberOfSeat) {

        this.destinationfrom = destinationfrom;
        this.destinatinationTo = destinatinationTo;
        this.numberOfSeat = numberOfSeat;
        this.price = price;
        this.licensePlate = licensePlate;
        Hr = hr;
        Min = min;
        Month = month;
        Day = day;
        this.year = year;
    }

    protected TripObject(Parcel in) {
        destinationfrom = in.readString();
        destinatinationTo = in.readString();
        Hr = Integer.parseInt(in.readString());
        Min = Integer.parseInt(in.readString());
        Month = Integer.parseInt(in.readString());
        Day = Integer.parseInt(in.readString());
        year = Integer.parseInt(in.readString());
        numberOfSeat = Integer.parseInt(in.readString());
        price = in.readString();
        licensePlate = in.readString();

    }


    public static final Creator<TripObject> CREATOR = new Creator<TripObject>() {
        @Override
        public TripObject createFromParcel(Parcel in) {
            return new TripObject(in);
        }

        @Override
        public TripObject[] newArray(int size) {
            return new TripObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(destinationfrom);
        dest.writeString(destinatinationTo);
        dest.writeString(String.valueOf(Hr));
        dest.writeString(String.valueOf(Min));
        dest.writeString(String.valueOf(year));
        dest.writeString(String.valueOf(Month));
        dest.writeString(String.valueOf(Day));
        dest.writeString(String.valueOf(numberOfSeat));
        dest.writeString(licensePlate);
        dest.writeString(price);





    }

    @Override
    public String toString() {
        return "{" +
                "Your trafvelling  from:'" + destinationfrom + '\'' +
                ",To' " + destinatinationTo + '\'' +
                ",at  " + Hr + ":"+
                "" + Min +
                ", On  " + year +
                ",/" + Day +
                ",/" + Month +
                " At a price of :"+ price +
                " With " + numberOfSeat + " Free seats "+
                " Your License Plate: "+licensePlate +

                '}';
    }
}
