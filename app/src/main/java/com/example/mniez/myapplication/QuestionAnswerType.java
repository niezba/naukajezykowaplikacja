package com.example.mniez.myapplication;

/**
 * Created by mniez on 15.10.2017.
 */

public class QuestionAnswerType {

    Integer id;
    String typeName;

    public QuestionAnswerType() {
    }

    public QuestionAnswerType(Integer id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
