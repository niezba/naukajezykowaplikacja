package com.example.mniez.myapplication.TeacherModule;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.ObjectHelper.Exam;
import com.example.mniez.myapplication.ObjectHelper.ExamQuestion;
import com.example.mniez.myapplication.ObjectHelper.Language;
import com.example.mniez.myapplication.ObjectHelper.Lecture;
import com.example.mniez.myapplication.ObjectHelper.Lesson;
import com.example.mniez.myapplication.ObjectHelper.NetworkConnection;
import com.example.mniez.myapplication.ObjectHelper.QuestionAnswerType;
import com.example.mniez.myapplication.ObjectHelper.Test;
import com.example.mniez.myapplication.ObjectHelper.TestQuestion;
import com.example.mniez.myapplication.ObjectHelper.User;
import com.example.mniez.myapplication.ObjectHelper.UsersCourse;
import com.example.mniez.myapplication.ObjectHelper.UsersExam;
import com.example.mniez.myapplication.ObjectHelper.UsersLesson;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SynchronizationActivity extends AppCompatActivity {

    /*Aktywność wywołująca pierwszą, częściową synchronizację. Wykonał: Marcin Niezbecki*/

    ProgressBar mProgressView;
    MobileDatabaseReader dbReader;
    private SynchronizationActivity.CourseFetchTask mFetchTask = null;
    ArrayList<Exam> examsToSynchr = new ArrayList<>();
    ArrayList<Test> testsToSynchr = new ArrayList<>();
    int imagesQueue;

    SharedPreferences sharedpreferences;
    private static final String PREFERENCES_OFFLINE = "isOffline";
    private static final String MY_PREFERENCES = "DummyLangPreferences";
    private static final String PREFERENCES_USERNAME = "loggedUserLogin";
    private static final String PREFERENCES_PASSWORD = "loggedUserPassword";
    String mEmail;
    String mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
        mProgressView = (ProgressBar) findViewById(R.id.synchr_progress2);
        showProgress(true);
        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        mEmail = sharedpreferences.getString(PREFERENCES_USERNAME, "");
        mPassword = sharedpreferences.getString(PREFERENCES_PASSWORD, "");
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(PREFERENCES_OFFLINE, 0);
        editor.commit();
        dbReader = new MobileDatabaseReader(getApplicationContext());
        mFetchTask = new SynchronizationActivity.CourseFetchTask("");
        mFetchTask.execute();
    }

    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public class CourseFetchTask extends AsyncTask<Void, Void, Boolean> {

        private final String userId;

        CourseFetchTask(String currentId) {
            userId = currentId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                NetworkConnection nConnection = new NetworkConnection(SynchronizationActivity.this);
                if (nConnection.isNetworkConnection() == false ) {
                    return false;
                }
                URL loginEndpoint = new URL("http://pzmmd.cba.pl/api/login?username=" + mEmail + "&password=" + mPassword);
                HttpURLConnection loginConnection = (HttpURLConnection) loginEndpoint.openConnection();
                loginConnection.setRequestMethod("GET");
                loginConnection.setDoOutput(true);
                loginConnection.connect();

                BufferedReader bl = new BufferedReader(new InputStreamReader(loginEndpoint.openStream()));
                StringBuilder sl = new StringBuilder();

                String linel;
                while ((linel = bl.readLine()) != null) {
                    sl.append(linel + "\n");
                }
                bl.close();

                String jsonloginString = sl.toString();
                System.out.println("JSON: " + jsonloginString);

                JSONObject jsonloginObject = new JSONObject(jsonloginString);
                String errLoginCode = jsonloginObject.get("error_code").toString();
                loginConnection.disconnect();
                System.out.println("Error code: " + errLoginCode);
                if (errLoginCode.equals("0")) {
                    //SharedPreferences.Editor editor = sharedpreferences.edit();
                    /*String appUserName = jsonloginObject.get("username").toString();
                    String appNameSurname = jsonloginObject.get("firstName").toString() + " " + jsonloginObject.get("lastName").toString();
                    String appUserId = jsonloginObject.get("id").toString();
                    JSONArray appRoles = jsonloginObject.getJSONArray("roles");
                    String appRole = appRoles.getString(0);
                    editor.commit();
                    return true;*/
                } else {
                    String errorText = jsonloginObject.get("error_message").toString();
                }

                URL webpageEndpoint = new URL("http://pzmmd.cba.pl/api/courses");
                HttpURLConnection myConnection = (HttpURLConnection) webpageEndpoint.openConnection();
                myConnection.setRequestMethod("GET");
                myConnection.setDoOutput(true);
                myConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(webpageEndpoint.openStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                String jsonString = sb.toString();
                System.out.println("JSON: " + jsonString);

                try {
                    JSONArray jsonObject = new JSONArray(jsonString);
                    String jsonObjectString = jsonObject.toString();
                    System.out.println(jsonObjectString);
                    myConnection.disconnect();
                    int coursesCount = jsonObject.length();
                    for (int i = 0; i < coursesCount; i++){
                        Course newCourse = new Course();
                        JSONObject singleCourse = jsonObject.getJSONObject(i);
                        String courseId = singleCourse.get("id").toString();
                        Integer courseIdInteger = Integer.parseInt(courseId);
                        newCourse.setId(courseIdInteger);
                        String courseName = singleCourse.get("coursename").toString();
                        newCourse.setCourseName(courseName);
                        String description = singleCourse.get("description").toString();
                        newCourse.setDescription(description);
                        String createdAt = singleCourse.get("createdAt").toString();
                        newCourse.setCreatedAt(createdAt);
                        String levelName = singleCourse.get("levelName").toString();
                        newCourse.setLevelName(levelName);
                        final String avatar = singleCourse.get("avatar").toString();
                        newCourse.setAvatar(avatar);
                        String nativeLanguage = singleCourse.get("nativeLanguage").toString();
                        newCourse.setNativeLanguageName(nativeLanguage);
                        String learningLanguage = singleCourse.get("learningLanguage").toString();
                        newCourse.setLearnedLanguageName(learningLanguage);
                        dbReader.insertCourse(newCourse);
                        dbReader.updateCourse(newCourse);
                        try {
                            URL webpageEndpoint2 = new URL("http://pzmmd.cba.pl/api/teacher/course/" + courseIdInteger);
                            HttpURLConnection myConnection2 = (HttpURLConnection) webpageEndpoint2.openConnection();
                            myConnection2.setRequestMethod("GET");
                            myConnection2.setDoOutput(true);
                            myConnection2.connect();

                            BufferedReader br2 = new BufferedReader(new InputStreamReader(webpageEndpoint2.openStream()));
                            StringBuilder sb2 = new StringBuilder();

                            String line2;
                            while ((line2 = br2.readLine()) != null) {
                                sb2.append(line2 + "\n");
                            }
                            br2.close();

                            String json2String = sb2.toString();
                            System.out.println("JSON Lessons data downloaded");

                            JSONArray lessonsProtoArray = new JSONArray(json2String);
                            if (lessonsProtoArray.length() > 0) {
                                JSONArray lessonsArray = lessonsProtoArray.getJSONObject(0).getJSONArray("lessons");
                                int lessonsLength = lessonsArray.length();
                                myConnection2.disconnect();
                                for (int j = 0; j < lessonsLength; j++) {
                                    Lesson newLesson = new Lesson();
                                    JSONObject singleLesson = lessonsArray.getJSONObject(j);
                                    String lessonId = singleLesson.get("id").toString();
                                    Integer lessonIdInteger = Integer.parseInt(lessonId);
                                    newLesson.setLessonId(lessonIdInteger);
                                    String lessonName = singleLesson.get("name").toString();
                                    newLesson.setName(lessonName);
                                    String lessonDescription = singleLesson.get("description").toString();
                                    newLesson.setDescription(lessonDescription);
                                    String lessonNumber = singleLesson.get("lessonNumber").toString();
                                    newLesson.setLessonNumber(Integer.parseInt(lessonNumber));
                                    JSONArray lessonLectures = singleLesson.getJSONArray("lectures");
                                    int lessonlectLength = lessonLectures.length();
                                    for (int e = 0; e < lessonlectLength; e++) {
                                        Lecture lecture = new Lecture();
                                        JSONObject singleLecture = lessonLectures.getJSONObject(e);
                                        String singleLectureId = singleLecture.get("id").toString();
                                        Integer singleLectureIdInteger = Integer.parseInt(singleLectureId);
                                        String singleLectureName = singleLecture.get("name").toString();
                                        String singleLectureUrl = singleLecture.get("url").toString();
                                        dbReader.insertLecture(lecture);
                                        dbReader.updateLecture(lecture);
                                    }
                                    JSONArray lessonTests = singleLesson.getJSONArray("tests");
                                    int lessontestLength = lessonTests.length();
                                    for (int k = 0; k < lessontestLength; k++) {
                                        Test newTest = new Test();
                                        JSONObject singleTest = lessonTests.getJSONObject(k);
                                        String testId = singleTest.get("id").toString();
                                        Integer testIdInteger = Integer.parseInt(testId);
                                        newTest.setId(testIdInteger);
                                        String testName = singleTest.get("name").toString();
                                        newTest.setName(testName);
                                        String testDescription = singleTest.get("description").toString();
                                        newTest.setDescription(testDescription);
                                        //newTest.setScore(0);
                                        newTest.setIsNew(1);
                                        newTest.setIsLocal(1);
                                        //newTest.setIsCompleted(0);
                                        newTest.setLessonId(lessonIdInteger);
                                        JSONArray questionsArray = singleTest.getJSONArray("questions");
                                        int questionsCount = questionsArray.length();
                                        for (int l = 0; l < questionsCount; l++) {
                                            TestQuestion newQuestion = new TestQuestion();
                                            JSONObject singleQuestion = questionsArray.getJSONObject(l);
                                            newQuestion.setTestId(testIdInteger);
                                            String questionId = singleQuestion.get("id").toString();
                                            Integer questionIdInteger = Integer.parseInt(questionId);
                                            newQuestion.setId(questionIdInteger);
                                            String questionToAsk = singleQuestion.get("question").toString();
                                            newQuestion.setQuestion(questionToAsk);
                                            String questionPoints = singleQuestion.get("points").toString();
                                            Integer questionPointsInteger = Integer.parseInt(questionPoints);
                                            newQuestion.setPoints(questionPointsInteger);
                                            JSONObject questionAnswer = singleQuestion.getJSONObject("answer");
                                            {
                                                final Word answerWord = new Word();
                                                String wordId = questionAnswer.get("id").toString();
                                                Integer wordIdInteger = Integer.parseInt(wordId);
                                                answerWord.setId(wordIdInteger);
                                                String nativeWord = questionAnswer.get("nativeWord").toString();
                                                answerWord.setNativeWord(nativeWord);
                                                String translatedWord = questionAnswer.get("translatedWord").toString();
                                                answerWord.setTranslatedWord(translatedWord);
                                                if (questionAnswer.has("nativeSound")) {
                                                    String nativeSound = questionAnswer.get("nativeSound").toString();
                                                    answerWord.setNativeSound(nativeSound);
                                                }
                                                if (questionAnswer.has("translatedSound")) {
                                                    String translatedSound = questionAnswer.get("translatedSound").toString();
                                                    answerWord.setTranslatedSound(translatedSound);
                                                }
                                                if (questionAnswer.has("picture")) {
                                                    final String pictureUrl = questionAnswer.get("picture").toString();
                                                    answerWord.setPicture(pictureUrl);
                                                }
                                                if (questionAnswer.has("tags")) {
                                                    String wordTags = questionAnswer.get("tags").toString();
                                                    answerWord.setTags(wordTags);
                                                }
                                                String nativeDefinition = questionAnswer.get("nativeDefinition").toString();
                                                answerWord.setNativeDefinition(nativeDefinition);
                                                String translatedDefinition = questionAnswer.get("translatedDefinition").toString();
                                                answerWord.setTranslatedDefinition(translatedDefinition);
                                                JSONObject answerNativeLang = questionAnswer.getJSONObject("nativeLanguage");
                                                String nativeXLanguage = answerNativeLang.get("id").toString();
                                                Integer nativeLanguageInteger = Integer.parseInt(nativeXLanguage);
                                                String nativeLanguageName = answerNativeLang.get("languageName").toString();
                                                answerWord.setNativeLanguageId(nativeLanguageInteger);
                                                JSONObject answerTranslatLang = questionAnswer.getJSONObject("translatedLanguage");
                                                String translatedLanguage = answerTranslatLang.get("id").toString();
                                                Integer translatedLanguageInteger = Integer.parseInt(translatedLanguage);
                                                String translatedLanguageName = answerTranslatLang.get("languageName").toString();
                                                answerWord.setTranslatedLanguageId(translatedLanguageInteger);
                                                newQuestion.setAnswerId(wordIdInteger);
                                                Language nativeLang = new Language();
                                                Language translatLang = new Language();
                                                nativeLang.setId(nativeLanguageInteger);
                                                nativeLang.setLanguageName(nativeLanguageName);
                                                translatLang.setId(translatedLanguageInteger);
                                                translatLang.setLanguageName(translatedLanguageName);
                                                dbReader.insertWord(answerWord);
                                                dbReader.updateWord(answerWord);
                                                dbReader.insertLanguage(nativeLang);
                                                dbReader.insertLanguage(translatLang);
                                            }
                                            JSONObject questionType = singleQuestion.getJSONObject("questionType");
                                            String questionTypeId = questionType.get("id").toString();
                                            Integer questionTypeIdInteger = Integer.parseInt(questionTypeId);
                                            String questionTypeName = questionType.get("typeName").toString();
                                            JSONObject answerType = singleQuestion.getJSONObject("answerType");
                                            String answerTypeId = answerType.get("id").toString();
                                            Integer answerTypeIdInteger = Integer.parseInt(answerTypeId);
                                            String answerTypeName = answerType.get("typeName").toString();
                                            QuestionAnswerType questionTypeOne = new QuestionAnswerType();
                                            questionTypeOne.setId(answerTypeIdInteger);
                                            questionTypeOne.setTypeName(answerTypeName);
                                            QuestionAnswerType questionTypeTwo = new QuestionAnswerType();
                                            questionTypeTwo.setId(questionTypeIdInteger);
                                            questionTypeTwo.setTypeName(questionTypeName);
                                            dbReader.insertAnswertypes(questionTypeOne);
                                            dbReader.insertAnswertypes(questionTypeTwo);
                                            newQuestion.setQuestionTypeId(questionTypeIdInteger);
                                            newQuestion.setAnswerTypeId(answerTypeIdInteger);
                                            dbReader.insertTestQuestion(newQuestion);
                                        }
                                        dbReader.insertTest(newTest);
                                        dbReader.updateTestFullSynch(newTest);
                                        System.out.println("Test inserted");

                                    }
                                    JSONArray lessonExams = singleLesson.getJSONArray("exams");
                                    int lessonexamLength = lessonExams.length();
                                    System.out.println("Ilość egzaminów: " + lessonexamLength);
                                    if (lessonexamLength > 0) {
                                        for (int k = 0; k < lessonexamLength; k++) {
                                            Exam newExam = new Exam();
                                            JSONObject singleExam = lessonExams.getJSONObject(k);
                                            String testId = singleExam.get("id").toString();
                                            Integer testIdInteger = Integer.parseInt(testId);
                                            newExam.setId(testIdInteger);
                                            String testName = singleExam.get("name").toString();
                                            newExam.setName(testName);
                                            String testDescription = singleExam.get("description").toString();
                                            newExam.setDescription(testDescription);
                                            //newTest.setScore(0);
                                            newExam.setIsNew(1);
                                            newExam.setIsLocal(1);
                                            //newTest.setIsCompleted(0);
                                            newExam.setLessonId(lessonIdInteger);
                                            JSONArray questionsArray = singleExam.getJSONArray("questions");
                                            int questionsCount = questionsArray.length();
                                            for (int l = 0; l < questionsCount; l++) {
                                                ExamQuestion newQuestion = new ExamQuestion();
                                                JSONObject singleQuestion = questionsArray.getJSONObject(l);
                                                newQuestion.setExamId(testIdInteger);
                                                String questionId = singleQuestion.get("id").toString();
                                                Integer questionIdInteger = Integer.parseInt(questionId);
                                                newQuestion.setId(questionIdInteger);
                                                String questionToAsk = singleQuestion.get("question").toString();
                                                newQuestion.setQuestion(questionToAsk);
                                                String questionPoints = singleQuestion.get("points").toString();
                                                Integer questionPointsInteger = Integer.parseInt(questionPoints);
                                                newQuestion.setPoints(questionPointsInteger);
                                                JSONObject questionAnswer = singleQuestion.getJSONObject("answer");
                                                {
                                                    final Word answerWord = new Word();
                                                    String wordId = questionAnswer.get("id").toString();
                                                    Integer wordIdInteger = Integer.parseInt(wordId);
                                                    answerWord.setId(wordIdInteger);
                                                    String nativeWord = questionAnswer.get("nativeWord").toString();
                                                    answerWord.setNativeWord(nativeWord);
                                                    String translatedWord = questionAnswer.get("translatedWord").toString();
                                                    answerWord.setTranslatedWord(translatedWord);
                                                    if (questionAnswer.has("nativeSound")) {
                                                        String nativeSound = questionAnswer.get("nativeSound").toString();
                                                        answerWord.setNativeSound(nativeSound);
                                                    }
                                                    if (questionAnswer.has("translatedSound")) {
                                                        String translatedSound = questionAnswer.get("translatedSound").toString();
                                                        answerWord.setTranslatedSound(translatedSound);
                                                    }
                                                    if (questionAnswer.has("picture")) {
                                                        final String pictureUrl = questionAnswer.get("picture").toString();
                                                        answerWord.setPicture(pictureUrl);
                                                    }
                                                    if (questionAnswer.has("tags")) {
                                                        String wordTags = questionAnswer.get("tags").toString();
                                                        answerWord.setTags(wordTags);
                                                    }
                                                    String nativeDefinition = questionAnswer.get("nativeDefinition").toString();
                                                    answerWord.setNativeDefinition(nativeDefinition);
                                                    String translatedDefinition = questionAnswer.get("translatedDefinition").toString();
                                                    answerWord.setTranslatedDefinition(translatedDefinition);
                                                    JSONObject answerNativeLang = questionAnswer.getJSONObject("nativeLanguage");
                                                    String nativeXLanguage = answerNativeLang.get("id").toString();
                                                    Integer nativeLanguageInteger = Integer.parseInt(nativeXLanguage);
                                                    String nativeLanguageName = answerNativeLang.get("languageName").toString();
                                                    answerWord.setNativeLanguageId(nativeLanguageInteger);
                                                    JSONObject answerTranslatLang = questionAnswer.getJSONObject("translatedLanguage");
                                                    String translatedLanguage = answerTranslatLang.get("id").toString();
                                                    Integer translatedLanguageInteger = Integer.parseInt(translatedLanguage);
                                                    String translatedLanguageName = answerTranslatLang.get("languageName").toString();
                                                    answerWord.setTranslatedLanguageId(translatedLanguageInteger);
                                                    newQuestion.setAnswerId(wordIdInteger);
                                                    Language nativeLang = new Language();
                                                    Language translatLang = new Language();
                                                    nativeLang.setId(nativeLanguageInteger);
                                                    nativeLang.setLanguageName(nativeLanguageName);
                                                    translatLang.setId(translatedLanguageInteger);
                                                    translatLang.setLanguageName(translatedLanguageName);
                                                    dbReader.insertWord(answerWord);
                                                    dbReader.updateWord(answerWord);
                                                    dbReader.insertLanguage(nativeLang);
                                                    dbReader.insertLanguage(translatLang);
                                                }
                                                JSONObject questionType = singleQuestion.getJSONObject("questionType");
                                                String questionTypeId = questionType.get("id").toString();
                                                Integer questionTypeIdInteger = Integer.parseInt(questionTypeId);
                                                String questionTypeName = questionType.get("typeName").toString();
                                                JSONObject answerType = singleQuestion.getJSONObject("answerType");
                                                String answerTypeId = answerType.get("id").toString();
                                                Integer answerTypeIdInteger = Integer.parseInt(answerTypeId);
                                                String answerTypeName = answerType.get("typeName").toString();
                                                QuestionAnswerType questionTypeOne = new QuestionAnswerType();
                                                questionTypeOne.setId(answerTypeIdInteger);
                                                questionTypeOne.setTypeName(answerTypeName);
                                                QuestionAnswerType questionTypeTwo = new QuestionAnswerType();
                                                questionTypeTwo.setId(questionTypeIdInteger);
                                                questionTypeTwo.setTypeName(questionTypeName);
                                                dbReader.insertAnswertypes(questionTypeOne);
                                                dbReader.insertAnswertypes(questionTypeTwo);
                                                newQuestion.setQuestionTypeId(questionTypeIdInteger);
                                                newQuestion.setAnswerTypeId(answerTypeIdInteger);
                                                dbReader.insertExamQuestion(newQuestion);
                                            }
                                            dbReader.insertExam(newExam);
                                            dbReader.updateExam(newExam);
                                            System.out.println("Exam inserted");

                                        }
                                    }
                                    dbReader.insertLesson(newLesson, courseIdInteger);
                                    System.out.println("Lesson inserted");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } /*catch (JSONException e) {
                e.printStackTrace();
            }*/         URL participantsEndpoint = new URL("http://pzmmd.cba.pl/api/teacher/courseParticipants/" + courseId);
                        HttpURLConnection participantsConnection = (HttpURLConnection) participantsEndpoint.openConnection();
                        participantsConnection.setRequestMethod("GET");
                        participantsConnection.setDoOutput(true);
                        participantsConnection.connect();

                        BufferedReader bp = new BufferedReader(new InputStreamReader(participantsEndpoint.openStream()));
                        StringBuilder sp = new StringBuilder();

                        String linep;
                        while ((linep = bp.readLine()) != null) {
                            sp.append(linep + "\n");
                        }
                        bp.close();

                        String jsonParticipantsString = sp.toString();
                        System.out.println("JSON: " + jsonParticipantsString);
                        JSONArray participantsArray = new JSONArray(jsonParticipantsString);
                        participantsConnection.disconnect();
                        int participantsCount = participantsArray.length();
                        for (int p = 0; p< participantsCount; p++) {
                            JSONObject singleParticipant = participantsArray.getJSONObject(p);
                            User singleUser = new User();
                            singleUser.setUserId(singleParticipant.getInt("id"));
                            singleUser.setUserName(singleParticipant.getString("firstName"));
                            singleUser.setUserSurname(singleParticipant.getString("lastName"));
                            if (singleParticipant.has("avatar")) {
                                final String participantAvatar = singleParticipant.get("avatar").toString();
                                singleUser.setAvatar(participantAvatar);
                            }
                            UsersCourse singleUsersCourse = new UsersCourse(singleUser.getUserId(), singleUser.getUserName(), singleUser.getUserSurname(), courseIdInteger);
                            dbReader.insertUser(singleUser);
                            dbReader.insertUserCourse(singleUsersCourse);
                            JSONArray userLessons = singleParticipant.getJSONArray("grades");
                            for (int l = 0; l< userLessons.length(); l++) {
                                JSONObject singleUsersLessonJson = userLessons.getJSONObject(l);
                                UsersLesson singleUsersLesson = new UsersLesson(singleUsersCourse.getUserId(), singleUsersCourse.getUserName(), singleUsersCourse.getUserSurname(), singleUsersCourse.getCourseId());
                                singleUsersLesson.setLessonId(singleUsersLessonJson.getInt("id"));
                                dbReader.insertUserLesson(singleUsersLesson);
                                JSONArray userExamsArray = singleUsersLessonJson.getJSONArray("exams");
                                for (int x = 0; x<userExamsArray.length(); x++) {
                                    JSONObject singleUsersExamJson = userExamsArray.getJSONObject(x);
                                    UsersExam singleUsersExam = new UsersExam(singleUsersLesson.getUserId(), singleUsersLesson.getUserName(), singleUsersLesson.getUserSurname(), singleUsersLesson.getCourseId(), singleUsersLesson.getLessonId());
                                    singleUsersExam.setExamId(singleUsersExamJson.getInt("id"));
                                    singleUsersExam.setGrade(singleUsersExamJson.getJSONArray("users").getJSONObject(0).getInt("grade"));
                                    dbReader.insertUserExam(singleUsersExam);
                                }
                            }

                        }

                        System.out.println(courseIdInteger + " " + courseName + " " + description + " " + createdAt + " " + levelName
                                + " " + nativeLanguage + " " + learningLanguage);
                    }
                   // return true;
                } catch (JSONException e) {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String errCode = jsonObject.get("error_code").toString();
                    System.out.println("Error code: " + errCode);
                    if(errCode.equals("1")) {
                        return false;
                    }
                }

                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } /*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (JSONException e) {
                e.printStackTrace();
            }


            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(SynchronizationActivity.this, TeacherMainActivity.class);
                startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SynchronizationActivity.this);
                builder.setMessage("Aby móc wykonać synchronizację musi być obecne połączenie z internetem.")
                        .setTitle("Brak połączenia z internetem");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(SynchronizationActivity.this, TeacherMainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }


    }
}
