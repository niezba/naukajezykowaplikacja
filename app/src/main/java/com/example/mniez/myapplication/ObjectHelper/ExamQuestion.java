package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 15.10.2017.
 */

public class ExamQuestion {

    Integer id;
    Integer answerId;
    Integer answerTypeId;
    Integer points;
    String question;
    Integer questionTypeId;
    Integer examId;
    Integer otherAnswerOneId;
    Integer otherAnswerTwoId;
    Integer otherAnswerThreeId;

    public ExamQuestion() {
    }

    public ExamQuestion(Integer id, Integer answerId, Integer answerTypeId, Integer points, String question, Integer questionTypeId, Integer examId, Integer otherAnswerOneId, Integer otherAnswerTwoId, Integer otherAnswerThreeId) {
        this.id = id;
        this.answerId = answerId;
        this.answerTypeId = answerTypeId;
        this.points = points;
        this.question = question;
        this.questionTypeId = questionTypeId;
        this.examId = examId;
        this.otherAnswerOneId = otherAnswerOneId;
        this.otherAnswerTwoId = otherAnswerTwoId;
        this.otherAnswerThreeId = otherAnswerThreeId;
    }

    public Integer getOtherAnswerOneId() {
        return otherAnswerOneId;
    }

    public void setOtherAnswerOneId(Integer otherAnswerOneId) {
        this.otherAnswerOneId = otherAnswerOneId;
    }

    public Integer getOtherAnswerTwoId() {
        return otherAnswerTwoId;
    }

    public void setOtherAnswerTwoId(Integer otherAnswerTwoId) {
        this.otherAnswerTwoId = otherAnswerTwoId;
    }

    public Integer getOtherAnswerThreeId() {
        return otherAnswerThreeId;
    }

    public void setOtherAnswerThreeId(Integer otherAnswerThreeId) {
        this.otherAnswerThreeId = otherAnswerThreeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public Integer getAnswerTypeId() {
        return answerTypeId;
    }

    public void setAnswerTypeId(Integer answerTypeId) {
        this.answerTypeId = answerTypeId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(Integer questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }
}
