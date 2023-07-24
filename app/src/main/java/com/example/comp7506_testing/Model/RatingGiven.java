package com.example.comp7506_testing.Model;

import java.io.Serializable;
import java.util.Date;

public class RatingGiven implements Serializable {

    private float rating;
    private String comment;
    private Date createAt;

    public RatingGiven(){

    }

    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Date getCreateAt() {
        return createAt;
    }

    @Override
    public String toString() {
        return "RatingGiven{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
