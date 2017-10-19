package com.example.mniez.myapplication;

/**
 * Created by mniez on 15.10.2017.
 */

public class TestQuestion {

    Integer id;
    Integer answerId;
    Integer answerTypeId;
    Integer points;
    String question;
    Integer questionTypeId;
    Integer testId;

    public TestQuestion() {
    }

    public TestQuestion(Integer id, Integer answerId, Integer answerTypeId, Integer points, String question, Integer questionTypeId, Integer testId) {
        this.id = id;
        this.answerId = answerId;
        this.answerTypeId = answerTypeId;
        this.points = points;
        this.question = question;
        this.questionTypeId = questionTypeId;
        this.testId = testId;
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

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }
}
