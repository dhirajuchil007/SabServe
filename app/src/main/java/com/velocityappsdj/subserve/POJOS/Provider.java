package com.velocityappsdj.subserve.POJOS;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Provider implements Parcelable {
    String id;
    String name;
    String addressLine2;

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    String addressLine1;
    LatLng location;


    public Provider(String id, String name, String addressLine2, LatLng location) {
        this.id = id;
        this.name = name;
        this.addressLine2 = addressLine2;
        this.location = location;

    }

    public Provider(String addressLine2, LatLng location) {
        this.addressLine2 = addressLine2;
        this.location = location;
    }

    public Provider(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Provider() {
    }

    public static final Creator<Provider> CREATOR = new Creator<Provider>() {
        @Override
        public Provider createFromParcel(Parcel in) {
            return new Provider(in);
        }

        @Override
        public Provider[] newArray(int size) {
            return new Provider[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected Provider(Parcel in) {
        id = in.readString();
        name = in.readString();
        addressLine2 = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());


    }


    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(addressLine2);
        dest.writeParcelable(location, flags);

    }
}
