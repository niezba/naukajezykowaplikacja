package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 15.10.2017.
 */

public class Lesson {

    Integer lessonId;
    Integer courseId;
    String description;
    Integer lessonNumber;
    String name;
    Integer overallPoints;
    Integer userPoints;

    public Lesson() {
    }

    public Integer getOverallPoints() {
        return overallPoints;
    }

    public void setOverallPoints(Integer overallPoints) {
        this.overallPoints = overallPoints;
    }

    public Integer getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(Integer userPoints) {
        this.userPoints = userPoints;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Lesson(Integer lessonId, Integer courseId, String description, Integer lessonNumber, String name, Integer overallPoints, Integer userPoints) {
        this.lessonId = lessonId;

        this.courseId = courseId;
        this.description = description;
        this.lessonNumber = lessonNumber;
        this.name = name;
        this.overallPoints = overallPoints;
        this.userPoints = userPoints;

    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(Integer lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
