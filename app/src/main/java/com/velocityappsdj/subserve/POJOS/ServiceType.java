package com.velocityappsdj.subserve.POJOS;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ServiceType implements Parcelable {

   private String name;
    private String price;
    public ServiceType(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public ServiceType() {
    }

    public ServiceType(Parcel in) {
        name = in.readString();
        price = in.readString();
    }

    public static final Creator<ServiceType> CREATOR = new Creator<ServiceType>() {
        @Override
        public ServiceType createFromParcel(Parcel in) {
            return new ServiceType(in);
        }

        @Override
        public ServiceType[] newArray(int size) {
            return new ServiceType[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(price);
    }
}
