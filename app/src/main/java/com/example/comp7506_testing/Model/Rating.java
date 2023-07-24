package com.example.comp7506_testing.Model;

import java.io.Serializable;
import java.util.Date;

public class Rating implements Serializable {

    private String groupID;
    private String receiver;
    private Date createAt;

    public Rating(){

    }

    public String getGroupID() {
        return groupID;
    }

    public String getReceiver() {
        return receiver;
    }

    public Date getCreateAt() {
        return createAt;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "groupID='" + groupID + '\'' +
                ", receiver='" + receiver + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
