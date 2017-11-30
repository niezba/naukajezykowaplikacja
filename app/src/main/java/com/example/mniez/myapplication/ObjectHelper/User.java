package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 27.11.2017.
 */

public class User {

    Integer userId;
    String userName;
    String userSurname;
    String avatar;
    Integer isAvatarLocal;
    String avatarLocal;

    public User() {
    }

    public User(Integer userId, String userName, String userSurname) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
    }


    public User(Integer userId, String userName, String userSurname, String avatar, Integer isAvatarLocal, String avatarLocal) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.avatar = avatar;
        this.isAvatarLocal = isAvatarLocal;
        this.avatarLocal = avatarLocal;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getIsAvatarLocal() {
        return isAvatarLocal;
    }

    public void setIsAvatarLocal(Integer isAvatarLocal) {
        this.isAvatarLocal = isAvatarLocal;
    }

    public String getAvatarLocal() {
        return avatarLocal;
    }

    public void setAvatarLocal(String avatarLocal) {
        this.avatarLocal = avatarLocal;
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
