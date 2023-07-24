package com.example.comp7506_testing.Model;

import java.io.Serializable;
import java.util.Arrays;

public class UserForGroup implements Serializable {

    private String introduction;
    private String major;
    private String name;
    private RatingGiven[] ratingGiven;
    private String email;
    private String _id;

    public UserForGroup(){

    }


    public String get_id() {
        return _id;
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


    public RatingGiven[] getRatingGiven() {
        return ratingGiven;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "UserForGroup{" +
                "introduction='" + introduction + '\'' +
                ", major='" + major + '\'' +
                ", name='" + name + '\'' +
                ", ratingGiven=" + Arrays.toString(ratingGiven) +
                ", email='" + email + '\'' +
                ", _id='" + _id + '\'' +
                '}';
    }
}
