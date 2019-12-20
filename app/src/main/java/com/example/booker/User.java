package com.example.booker;

public class User {
    String phone;
    String name;
    String email;
    String profession;
    String password;
    Address userAddress;
    Boolean isOnline;
    long lastSeenTimeStamp;
    String profilePicLink;

    public User() {
    }

    public User(String phone, String name, String email, String profession, String password, Address userAddress) {
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.profession = profession;
        this.password = password;
        this.userAddress = userAddress;
    }

    public User(String phone, String name, String email, String profession, String password, Address userAddress, Boolean isOnline, long lastSeenTimeStamp) {
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.profession = profession;
        this.password = password;
        this.userAddress = userAddress;
        this.isOnline = isOnline;
        this.lastSeenTimeStamp = lastSeenTimeStamp;
    }

    public User(String phone, String name, String email, String profession, String password, Address userAddress, Boolean isOnline, long lastSeenTimeStamp, String profilePicLink) {
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.profession = profession;
        this.password = password;
        this.userAddress = userAddress;
        this.isOnline = isOnline;
        this.lastSeenTimeStamp = lastSeenTimeStamp;
        this.profilePicLink = profilePicLink;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(Address userAddress) {
        this.userAddress = userAddress;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public long getLastSeenTimeStamp() {
        return lastSeenTimeStamp;
    }

    public void setLastSeenTimeStamp(long lastSeenTimeStamp) {
        this.lastSeenTimeStamp = lastSeenTimeStamp;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }

    public void setProfilePicLink(String profilePicLink) {
        this.profilePicLink = profilePicLink;
    }
}
