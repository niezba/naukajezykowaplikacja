package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 15.10.2017.
 */

public class Test {

    Integer id;
    String description;
    Integer lessonId;
    String name;
    Integer isNew;
    Integer isCompleted;
    Integer score;
    Integer isLocal;

    public Test() {
    }

    public Test(Integer id, String description, Integer lessonId, String name, Integer pointsToPass, Integer isNew, Integer isCompleted, Integer score, Integer isLocal) {
        this.id = id;
        this.description = description;
        this.lessonId = lessonId;
        this.name = name;
        this.isNew = isNew;
        this.isCompleted = isCompleted;
        this.score = score;
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

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Integer isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(Integer isLocal) {
        this.isLocal = isLocal;
    }
}
