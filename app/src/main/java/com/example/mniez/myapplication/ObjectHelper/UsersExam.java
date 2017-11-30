package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 27.11.2017.
 */

public class UsersExam extends UsersLesson {

    Integer examId;
    Integer grade;
    String examDescription;

    public String getExamDescription() {
        return examDescription;
    }

    public void setExamDescription(String examDescription) {
        this.examDescription = examDescription;
    }

    public UsersExam(Integer userId, String userName, String userSurname, Integer courseId, Integer lessonId, Integer examId, Integer grade) {
        super(userId, userName, userSurname, courseId, lessonId);
        this.examId = examId;
        this.grade = grade;
    }

    public UsersExam(Integer userId, String userName, String userSurname, Integer courseId, Integer lessonId) {
        super(userId, userName, userSurname, courseId, lessonId);
    }

    public UsersExam() {
    }

    public Integer getExamId() {

        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
