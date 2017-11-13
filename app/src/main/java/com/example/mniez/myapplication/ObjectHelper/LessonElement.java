package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 05.11.2017.
 */

public class LessonElement {

    Integer lessonElementType;
    Integer lessonElementId;
    String lessonElementName;
    Integer lessonElementTotalPoints;
    Integer lessonElementScoredPoints;
    Integer lessonElementGrade;

    public Integer getLessonElementId() {
        return lessonElementId;
    }

    public LessonElement(Integer lessonElementType, Integer lessonElementId, String lessonElementName, Integer lessonElementTotalPoints, Integer lessonElementScoredPoints) {
        this.lessonElementType = lessonElementType;
        this.lessonElementId = lessonElementId;
        this.lessonElementName = lessonElementName;
        this.lessonElementTotalPoints = lessonElementTotalPoints;
        this.lessonElementScoredPoints = lessonElementScoredPoints;
    }

    public LessonElement(Integer lessonElementType, Integer lessonElementId, String lessonElementName, Integer lessonElementTotalPoints, Integer lessonElementScoredPoints, Integer lessonElementGrade) {
        this.lessonElementType = lessonElementType;
        this.lessonElementId = lessonElementId;
        this.lessonElementName = lessonElementName;
        this.lessonElementTotalPoints = lessonElementTotalPoints;
        this.lessonElementScoredPoints = lessonElementScoredPoints;
        this.lessonElementGrade = lessonElementGrade;
    }

    public void setLessonElementId(Integer lessonElementId) {
        this.lessonElementId = lessonElementId;
    }

    public LessonElement(Integer lessonElementType, Integer lessonElementId, String lessonElementName) {

        this.lessonElementType = lessonElementType;
        this.lessonElementId = lessonElementId;
        this.lessonElementName = lessonElementName;
    }

    public Integer getLessonElementType() {
        return lessonElementType;
    }

    public void setLessonElementType(Integer lessonElementType) {
        this.lessonElementType = lessonElementType;
    }

    public String getLessonElementName() {
        return lessonElementName;
    }

    public void setLessonElementName(String lessonElementName) {
        this.lessonElementName = lessonElementName;
    }

    public Integer getLessonElementTotalPoints() {
        return lessonElementTotalPoints;
    }

    public void setLessonElementTotalPoints(Integer lessonElementTotalPoints) {
        this.lessonElementTotalPoints = lessonElementTotalPoints;
    }

    public Integer getLessonElementScoredPoints() {
        return lessonElementScoredPoints;
    }

    public void setLessonElementScoredPoints(Integer lessonElementScoredPoints) {
        this.lessonElementScoredPoints = lessonElementScoredPoints;
    }

    public Integer getLessonElementGrade() {
        return lessonElementGrade;
    }

    public void setLessonElementGrade(Integer lessonElementGrade) {
        this.lessonElementGrade = lessonElementGrade;
    }
}
