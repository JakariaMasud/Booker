package com.example.booker;

public class Info {
    String name;
    String profilePicLink;
    Message Last_Msg;

    public Info(String name, String profilePicLink, Message last_Msg) {
        this.name = name;
        this.profilePicLink = profilePicLink;
        Last_Msg = last_Msg;
    }

    public Info() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }

    public void setProfilePicLink(String profilePicLink) {
        this.profilePicLink = profilePicLink;
    }

    public Message getLast_Msg() {
        return Last_Msg;
    }

    public void setLast_Msg(Message last_Msg) {
        Last_Msg = last_Msg;
    }

    @Override
    public String toString() {
        return "Info{" +
                "name='" + name + '\'' +
                ", profilePicLink='" + profilePicLink + '\'' +
                ", Last_Msg=" + Last_Msg +
                '}';
    }
}
