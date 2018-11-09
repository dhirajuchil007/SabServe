package com.velocityappsdj.subserve.POJOS;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Service implements Parcelable {
    String name;
    ArrayList<ServiceType> serviceTypeList;

    public Service() {
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ServiceType> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(ArrayList<ServiceType> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }

    public Service(String name, ArrayList<ServiceType> serviceList) {
        this.name = name;
        this.serviceTypeList = serviceList;
    }

    protected Service(Parcel in) {
        name = in.readString();
        serviceTypeList = in.createTypedArrayList(ServiceType.CREATOR);
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
        dest.writeTypedList(serviceTypeList);
    }
}
