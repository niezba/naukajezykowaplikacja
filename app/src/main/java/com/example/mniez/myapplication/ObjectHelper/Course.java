package com.example.mniez.myapplication.ObjectHelper;

import java.io.File;
import java.sql.Date;

/**
 * Created by mniez on 11.10.2017.
 */

public class Course {

    //pola klasy
    Integer id;
    String accessCode;
    String avatar;
    String courseName;
    String createdAt;
    String description;
    Integer learningLanguageId;
    Integer levelId;
    Integer nativeLanguageId;
    String tags;
    Integer teacherId;
    String teacherName;
    String teacherSurname;
    String levelName;
    String nativeLanguageName;
    String learnedLanguageName;

    //konstruktory - pusty i pełny - więcej nie trzeba
    public Course() {
    }

    public Course(Integer id, String accessCode, String avatar, String courseName, String createdAt, String description, Integer learningLanguageId, Integer levelId, Integer nativeLanguageId, String tags, Integer teacherId, String teacherName, String teacherSurname, String levelName, String nativeLanguageName, String learnedLanguageName) {
        this.id = id;
        this.accessCode = accessCode;
        this.avatar = avatar;
        this.courseName = courseName;
        this.createdAt = createdAt;
        this.description = description;
        this.learningLanguageId = learningLanguageId;
        this.levelId = levelId;
        this.nativeLanguageId = nativeLanguageId;
        this.tags = tags;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.teacherSurname = teacherSurname;
        this.levelName = levelName;
        this.nativeLanguageName = nativeLanguageName;
        this.learnedLanguageName = learnedLanguageName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getNativeLanguageName() {
        return nativeLanguageName;
    }

    public void setNativeLanguageName(String nativeLanguageName) {
        this.nativeLanguageName = nativeLanguageName;
    }

    public String getLearnedLanguageName() {
        return learnedLanguageName;
    }

    public void setLearnedLanguageName(String learnedLanguageName) {
        this.learnedLanguageName = learnedLanguageName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLearningLanguageId() {
        return learningLanguageId;
    }

    public void setLearningLanguageId(Integer learningLanguageId) {
        this.learningLanguageId = learningLanguageId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public Integer getNativeLanguageId() {
        return nativeLanguageId;
    }

    public void setNativeLanguageId(Integer nativeLanguageId) {
        this.nativeLanguageId = nativeLanguageId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherSurname() {
        return teacherSurname;
    }

    public void setTeacherSurname(String teacherSurname) {
        this.teacherSurname = teacherSurname;
    }
}
