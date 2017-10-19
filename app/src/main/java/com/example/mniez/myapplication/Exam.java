package com.example.mniez.myapplication;

/**
 * Created by mniez on 15.10.2017.
 */

public class Exam {

    Integer id;
    String description;
    Integer lessonId;
    String name;
    Integer pointsToPass;
    Integer isNew;
    Integer isPassed;
    Integer score;
    Integer grade;
    Integer isLocal;

    public Exam() {
    }

    public Exam(Integer id, String description, Integer lessonId, String name, Integer pointsToPass, Integer isNew, Integer isPassed, Integer score, Integer grade, Integer isLocal) {
        this.id = id;
        this.description = description;
        this.lessonId = lessonId;
        this.name = name;
        this.pointsToPass = pointsToPass;
        this.isNew = isNew;
        this.isPassed = isPassed;
        this.score = score;
        this.grade = grade;
        this.isLocal = isLocal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getPointsToPass() {
        return pointsToPass;
    }

    public void setPointsToPass(Integer pointsToPass) {
        this.pointsToPass = pointsToPass;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(Integer isPassed) {
        this.isPassed = isPassed;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(Integer isLocal) {
        this.isLocal = isLocal;
    }
}
