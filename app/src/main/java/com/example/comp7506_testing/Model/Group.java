package com.example.comp7506_testing.Model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class Group implements Serializable {

    private String _id;
    private String courseCode;
    private String introduction;
    private int maxNum;
    private Date completionDate;
    private UserForGroup creator;
    private UserForGroup[] list;
    private Date createAt;

    public Group(){

    }

    public static Group addGroup(String courseCode, String introduction,  int maxNum, Date completionDate){
        Group group = new Group();
        group.setCourseCode(courseCode);
        group.setIntroduction(introduction);
        group.setMaxNum(maxNum);
        group.setCompletionDate(completionDate);
        return group;
    }

    public static Group joinOrQuitGroup(String id){
        Group group = new Group();
        group.set_id(id);
        return group;
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

    public UserForGroup getCreator() {
        return creator;
    }

    public UserForGroup[] getList() {
        return list;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Group{" +
                "_id='" + _id + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", introduction='" + introduction + '\'' +
                ", maxNum=" + maxNum +
                ", completionDate=" + completionDate +
                ", creator=" + creator +
                ", list=" + Arrays.toString(list) +
                '}';
    }
}
