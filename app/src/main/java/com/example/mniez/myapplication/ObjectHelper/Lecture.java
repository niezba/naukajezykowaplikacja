package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 15.10.2017.
 */

public class Lecture {

    Integer id;
    Integer lessonId;
    String name;
    String lectureUrl;
    Integer isLectureLocal;
    String lectureLocal;

    public Lecture() {
    }

    public Lecture(Integer id, Integer lessonId, String name, String lectureUrl) {
        this.id = id;
        this.lessonId = lessonId;
        this.name = name;
        this.lectureUrl = lectureUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLectureUrl() {
        return lectureUrl;
    }

    public void setLectureUrl(String lectureUrl) {
        this.lectureUrl = lectureUrl;
    }

    public Integer getIsLectureLocal() {
        return isLectureLocal;
    }

    public void setIsLectureLocal(Integer isLectureLocal) {
        this.isLectureLocal = isLectureLocal;
    }

    public String getLectureLocal() {
        return lectureLocal;
    }

    public void setLectureLocal(String lectureLocal) {
        this.lectureLocal = lectureLocal;
    }
}
