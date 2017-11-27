package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 27.11.2017.
 */

public class User {

    Integer userId;
    String userName;
    String userSurname;

    public User() {
    }

    public User(Integer userId, String userName, String userSurname) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }
}
