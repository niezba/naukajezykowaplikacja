package com.example.mniez.myapplication;

/**
 * Created by mniez on 15.10.2017.
 */

public class Language {

    Integer id;
    String languageName;

    public Language() {
    }

    public Language(Integer id, String languageName) {
        this.id = id;
        this.languageName = languageName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
}
