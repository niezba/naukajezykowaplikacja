package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 15.10.2017.
 */

public class Difficulty {

    Integer id;
    String levelName;

    public Difficulty() {
    }

    public Difficulty(Integer id, String levelName) {
        this.id = id;
        this.levelName = levelName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
