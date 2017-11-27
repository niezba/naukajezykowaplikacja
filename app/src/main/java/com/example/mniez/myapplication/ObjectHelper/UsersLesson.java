package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 27.11.2017.
 */

public class UsersLesson extends UsersCourse {

    Integer lessonId;

    public UsersLesson(Integer userId, String userName, String userSurname, Integer courseId) {
        super(userId, userName, userSurname, courseId);
    }

    public UsersLesson(Integer userId, String userName, String userSurname, Integer courseId, Integer lessonId) {
        super(userId, userName, userSurname, courseId);
        this.lessonId = lessonId;
    }

    public UsersLesson() {
    }

    public Integer getLessonId() {

        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }
}
