package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 27.11.2017.
 */

public class UsersCourse extends User {

    Integer courseId;
    String courseName;
    String courseDescription;
    String courseAvatar;
    Integer isCourseAvatarLocal;
    String courseAvatarLocal;

    public Integer getIsCourseAvatarLocal() {
        return isCourseAvatarLocal;
    }

    public void setIsCourseAvatarLocal(Integer isCourseAvatarLocal) {
        this.isCourseAvatarLocal = isCourseAvatarLocal;
    }

    public String getCourseAvatarLocal() {
        return courseAvatarLocal;
    }

    public void setCourseAvatarLocal(String courseAvatarLocal) {
        this.courseAvatarLocal = courseAvatarLocal;
    }

    public String getCourseAvatar() {
        return courseAvatar;
    }

    public void setCourseAvatar(String courseAvatar) {
        this.courseAvatar = courseAvatar;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public UsersCourse(Integer userId, String userName, String userSurname, Integer courseId) {
        super(userId, userName, userSurname);
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
