package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 27.11.2017.
 */

public class UsersCourse extends User {

    Integer courseId;

    public UsersCourse(Integer userId, String userName, String userSurname, Integer courseId) {
        super(userId, userName, userSurname);
        this.courseId = courseId;
    }

    public UsersCourse(Integer userId, String userName, String userSurname) {
        super(userId, userName, userSurname);
    }

    public UsersCourse() {

    }

    public Integer getCourseId() {

        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
