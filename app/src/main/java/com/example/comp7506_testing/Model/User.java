package com.example.comp7506_testing.Model;

import java.io.Serializable;
import java.util.Arrays;

public class User implements Serializable {

    private String _id;
    private String firebaseID;
    private String introduction;
    private String major;
    private String name;
    private String email;
    private GroupForUser[] group;
    private int[] rating;
    private int[] ratingGiven;

    public User() {

    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + _id + '\'' +
                ", firebaseID='" + firebaseID + '\'' +
                ", introduction='" + introduction + '\'' +
                ", major='" + major + '\'' +
                ", name='" + name + '\'' +
                ", group=" + Arrays.toString(group) +
                ", rating=" + Arrays.toString(rating) +
                ", ratingGiven=" + Arrays.toString(ratingGiven) +
                '}';
    }

    public static User registerOrLogin(String firebaseID){
        User user = new User();
        user.setFirebaseID(firebaseID);
        return user;
    }

    public static User updateInfo(String name, String major, String introduction, String email){
        User user = new User();
        user.setName(name);
        user.setMajor(major);
        user.setIntroduction(introduction);
        user.setEmail(email);
        return user;
    }

    public String getFirebaseID() {
        return firebaseID;
    }


    public String getId() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getMajor() {
        return major;
    }

    public String getName() {
        return name;
    }

    public GroupForUser[] getGroup() {
        return group;
    }

    public int[] getRating() {
        return rating;
    }

    public int[] getRatingGiven() {
        return ratingGiven;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}