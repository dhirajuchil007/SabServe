package com.velocityappsdj.subserve.POJOS;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Service implements Parcelable {
    String name;
    ArrayList<ServiceType> serviceTypeArrayList;

    public ArrayList<ServiceType> getServiceTypeArrayList() {
        return serviceTypeArrayList;
    }

    public void setServiceTypeArrayList(ArrayList<ServiceType> serviceTypeArrayList) {
        this.serviceTypeArrayList = serviceTypeArrayList;
    }

    public Service() {
    }
    public Service(String name) {
        this.name=name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    protected Service(Parcel in) {
        name = in.readString();
       // serviceTypeList = in.createTypedArrayList(ServiceType.CREATOR);
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
      //  dest.writeTypedList(serviceTypeList);
    }
}
