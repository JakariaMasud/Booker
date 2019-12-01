package com.example.booker;

public class Address {
    double longitude;
    double lattitude;
    String address;
    String zipcode;

    public Address() {
    }

    public Address(double longitude, double lattitude, String address, String zipcode) {
        this.longitude = longitude;
        this.lattitude = lattitude;
        this.address = address;
        this.zipcode = zipcode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
