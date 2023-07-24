package com.example.comp7506_testing.Model;

public class RatingForm {
    private String groupID;
    private float rating;
    private String comment;
    private String receiver;

    public RatingForm(String groupID, float rating, String comment, String receiver) {
        this.groupID = groupID;
        this.rating = rating;
        this.comment = comment;
        this.receiver = receiver;
    }

}
