package com.example.comp7506_testing.Model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class GroupForUser implements Serializable{
    private String _id;
    private String courseCode;
    private String introduction;
    private int maxNum;
    private Date completionDate;
    private String creator;
    private User[] list;
    private Date createAt;

    public GroupForUser(){

    }

    public String get_id() {
        return _id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getIntroduction() {
        return introduction;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public String getCreator() {
        return creator;
    }

    public User[] getList() {
        return list;
    }

    public Date getCreateAt() {
        return createAt;
    }

    @Override
    public String toString() {
        return "GroupForUser{" +
                "_id='" + _id + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", introduction='" + introduction + '\'' +
                ", maxNum=" + maxNum +
                ", completionDate=" + completionDate +
                ", creator='" + creator + '\'' +
                ", list=" + Arrays.toString(list) +
                ", createAt=" + createAt +
                '}';
    }
}
