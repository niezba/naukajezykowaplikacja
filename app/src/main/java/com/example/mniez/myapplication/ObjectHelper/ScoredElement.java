package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 19.11.2017.
 */

public class ScoredElement {

    String courseName;
    String lessonName;
    Integer courseId;
    Integer lessonId;
    Integer rowType;
    String testExamName;
    Integer testExamScore;
    Integer examGrade;

    public ScoredElement(String courseName, String lessonName, Integer courseId, Integer lessonId, Integer rowType, String testExamName, Integer testExamScore, Integer examGrade) {
        this.courseName = courseName;
        this.lessonName = lessonName;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.rowType = rowType;
        this.testExamName = testExamName;
        this.testExamScore = testExamScore;
        this.examGrade = examGrade;
    }

    public ScoredElement(Integer courseId, String courseName, Integer rowType) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.rowType = rowType;
    }

    public ScoredElement(Integer courseId, String courseName, Integer lessonId, String lessonName, Integer rowType) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.rowType = rowType;
    }


    public ScoredElement() {

    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getRowType() {
        return rowType;
    }

    public void setRowType(Integer rowType) {
        this.rowType = rowType;
    }

    public void setExamGrade(Integer examGrade) {
        this.examGrade = examGrade;
    }

    public String getLessonName() {

        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getTestExamName() {
        return testExamName;
    }

    public void setTestExamName(String testExamName) {
        this.testExamName = testExamName;
    }

    public Integer getTestExamScore() {
        return testExamScore;
    }

    public void setTestExamScore(Integer testExamScore) {
        this.testExamScore = testExamScore;
    }

    public Integer getExamGrade() {
        return examGrade;
    }

}
