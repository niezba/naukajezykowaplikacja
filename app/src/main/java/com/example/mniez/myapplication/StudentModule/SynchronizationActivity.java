package com.example.mniez.myapplication.StudentModule;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import com.example.mniez.myapplication.ObjectHelper.QuestionAnswerType;
import com.example.mniez.myapplication.ObjectHelper.Test;
import com.example.mniez.myapplication.ObjectHelper.TestQuestion;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class SynchronizationActivity extends AppCompatActivity {

    /*Aktywność wywołująca pierwszą, częściową synchronizację. Wykonał: Marcin Niezbecki*/

    ProgressBar mProgressView;
    MobileDatabaseReader dbReader;
    private SynchronizationActivity.CourseFetchTask mFetchTask = null;
    ArrayList<Exam> examsToSynchr = new ArrayList<>();
    ArrayList<Test> testsToSynchr = new ArrayList<>();

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
                examsToSynchr = dbReader.getAllLocallyCompletedExams();
                for(Exam ex: examsToSynchr) {
                    try {
                        URL webpageEndpoint = new URL("http://pzmmd.cba.pl/api/updateUserExamScore");
                        HttpURLConnection myConnection = (HttpURLConnection) webpageEndpoint.openConnection();
                        myConnection.setRequestMethod("POST");
                        myConnection.setDoOutput(true);
                        myConnection.setRequestProperty("Accept","*/*");
                        String request;
                        request = "examId=" + ex.getId() + "&answers=" + ex.getAnswersConcatenation();
                        System.out.println(request);
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        PrintWriter out = new PrintWriter(myConnection.getOutputStream());
                        out.print(request);
                        out.close();
                        myConnection.connect();
                        System.out.print(myConnection.getResponseCode());
                        BufferedReader br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();

                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();

                        String jsonString = sb.toString();
                        System.out.println("JSON: " + jsonString);
                        myConnection.disconnect();
                        if (myConnection.getResponseCode() == 200) {

                        }

                        else {
                        }
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                testsToSynchr = dbReader.getAllLocallyCompletedTests();
                for(Test ts: testsToSynchr) {
                    try {
                        URL webpageEndpoint = new URL("http://pzmmd.cba.pl/api/updateUserTestScore");
                        HttpURLConnection myConnection = (HttpURLConnection) webpageEndpoint.openConnection();
                        myConnection.setRequestMethod("POST");
                        myConnection.setDoOutput(true);
                        myConnection.setRequestProperty("Accept","*/*");
                        String request;
                        request = "testId=" + ts.getId() + "&answers=" + ts.getAnswersConcatenation();
                        System.out.println(request);
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        PrintWriter out = new PrintWriter(myConnection.getOutputStream());
                        out.print(request);
                        out.close();
                        myConnection.connect();
                        System.out.print(myConnection.getResponseCode());
                        BufferedReader br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();

                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();

                        String jsonString = sb.toString();
                        System.out.println("JSON: " + jsonString);
                        myConnection.disconnect();
                        if (myConnection.getResponseCode() == 200) {

                        }

                        else {
                        }
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                        String avatar = singleCourse.get("avatar").toString();
                        newCourse.setAvatar(avatar);
                        String teacherFirstName = singleCourse.get("teacherFirstName").toString();
                        newCourse.setTeacherName(teacherFirstName);
                        String teacherLastName = singleCourse.get("teacherLastName").toString();
                        newCourse.setTeacherSurname(teacherLastName);
                        String nativeLanguage = singleCourse.get("nativeLanguage").toString();
                        newCourse.setNativeLanguageName(nativeLanguage);
                        String learningLanguage = singleCourse.get("learningLanguage").toString();
                        newCourse.setLearnedLanguageName(learningLanguage);
                        dbReader.insertCourse(newCourse);
                        dbReader.updateCourse(newCourse);
                        try {
                            URL webpageEndpoint2 = new URL("http://pzmmd.cba.pl/api/lessonsForCourse/"+courseIdInteger);
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

                            try {
                                JSONArray json2Object = new JSONArray(json2String);
                                String json2ObjectString = json2Object.toString();
                                System.out.println(json2ObjectString);
                                myConnection2.disconnect();
                                int lessonsCount = json2Object.length();
                                for (int j = 0; j < lessonsCount; j++){
                                    Lesson newLesson = new Lesson();
                                    JSONObject singleLesson = json2Object.getJSONObject(j);
                                    String lessonId = singleLesson.get("id").toString();
                                    Integer lessonIdInteger = Integer.parseInt(lessonId);
                                    newLesson.setLessonId(lessonIdInteger);
                                    String lessonName = singleLesson.get("name").toString();
                                    newLesson.setName(lessonName);
                                    String lessonDescription = singleLesson.get("description").toString();
                                    newLesson.setDescription(lessonDescription);
                                    String lessonNumber = singleLesson.get("lessonNumber").toString();
                                    newLesson.setLessonNumber(Integer.parseInt(lessonNumber));
                                    String lessonPoints = singleLesson.get("points").toString();
                                    newLesson.setOverallPoints(Integer.parseInt(lessonPoints));
                                    String lessonUserPoints = singleLesson.get("userPoints").toString();
                                    newLesson.setUserPoints(Integer.parseInt(lessonUserPoints));
                                    JSONArray lessonLectures = singleLesson.getJSONArray("lectures");
                                    int lessonlectLength = lessonLectures.length();
                                    for (int e = 0; e<lessonlectLength; e++) {
                                        JSONObject singleLecture = lessonLectures.getJSONObject(e);
                                        String singleLectureId = singleLecture.get("id").toString();
                                        Integer singleLectureIdInteger = Integer.parseInt(singleLectureId);
                                        String singleLectureName = singleLecture.get("name").toString();
                                        String singleLectureUrl = singleLecture.get("url").toString();
                                        Lecture lecture = new Lecture();
                                        lecture.setLessonId(lessonIdInteger);
                                        lecture.setId(singleLectureIdInteger);
                                        lecture.setName(singleLectureName);
                                        lecture.setLectureUrl(singleLectureUrl);
                                        dbReader.insertLecture(lecture);
                                        dbReader.updateLecture(lecture);
                                    }
                                    dbReader.insertLesson(newLesson, courseIdInteger);
                                    System.out.println("Lesson inserted");
                                    URL lessonElementsEndpoint = new URL("http://pzmmd.cba.pl/api/lesson/"+lessonIdInteger);
                                    HttpURLConnection lessonConnection = (HttpURLConnection) lessonElementsEndpoint.openConnection();
                                    lessonConnection.setRequestMethod("GET");
                                    lessonConnection.setDoOutput(true);
                                    lessonConnection.connect();

                                    BufferedReader br3 = new BufferedReader(new InputStreamReader(lessonElementsEndpoint.openStream()));
                                    StringBuilder sb3 = new StringBuilder();

                                    String line3;
                                    while ((line3 = br3.readLine()) != null) {
                                        sb3.append(line3 + "\n");
                                    }
                                    br3.close();

                                    String lessonString = sb3.toString();
                                    if(!lessonString.contains("no tests for this lesson")) {
                                        try {
                                            JSONArray lessonObject = new JSONArray(lessonString);
                                            String lessonObjectString = lessonObject.toString();
                                            System.out.println(lessonObjectString);
                                            JSONObject elementsObject = lessonObject.getJSONObject(0);
                                            JSONArray testsArray = elementsObject.getJSONArray("tests");
                                            int testsCount = testsArray.length();
                                            for (int k = 0; k < testsCount; k++) {
                                                Test newTest = new Test();
                                                JSONObject singleTest = testsArray.getJSONObject(k);
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
                                                        Word answerWord = new Word();
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
                                                            String pictureUrl = questionAnswer.get("picture").toString();
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
                                                    JSONArray otherAnswers = singleQuestion.getJSONArray("others");
                                                    int othersCount = otherAnswers.length();
                                                    int wrongAnswerIds[] = new int[otherAnswers.length()];
                                                    for (int m = 0; m < othersCount; m++) {
                                                        Word wrongAnswer = new Word();
                                                        JSONObject singleOtherAnswer = otherAnswers.getJSONObject(m);
                                                        String wordId = singleOtherAnswer.get("id").toString();
                                                        Integer wordIdInteger = Integer.parseInt(wordId);
                                                        wrongAnswer.setId(wordIdInteger);
                                                        String nativeWord = singleOtherAnswer.get("nativeWord").toString();
                                                        wrongAnswer.setNativeWord(nativeWord);
                                                        String translatedWord = singleOtherAnswer.get("translatedWord").toString();
                                                        wrongAnswer.setTranslatedWord(translatedWord);
                                                        String nativeDefinition = singleOtherAnswer.get("nativeDefinition").toString();
                                                        wrongAnswer.setNativeDefinition(nativeDefinition);
                                                        String translatedDefinition = singleOtherAnswer.get("translatedDefinition").toString();
                                                        wrongAnswer.setTranslatedDefinition(translatedDefinition);
                                                        if (singleOtherAnswer.has("nativeSound")) {
                                                            String nativeSound = singleOtherAnswer.get("nativeSound").toString();
                                                            wrongAnswer.setNativeSound(nativeSound);
                                                        }
                                                        if (singleOtherAnswer.has("translatedSound")) {
                                                            String translatedSound = singleOtherAnswer.get("translatedSound").toString();
                                                            wrongAnswer.setTranslatedSound(translatedSound);
                                                        }
                                                        if (singleOtherAnswer.has("picture")) {
                                                            String pictureUrl = singleOtherAnswer.get("picture").toString();
                                                            wrongAnswer.setPicture(pictureUrl);
                                                        }
                                                        if (singleOtherAnswer.has("tags")) {
                                                            String wordTags = singleOtherAnswer.get("tags").toString();
                                                            wrongAnswer.setTags(wordTags);
                                                        }
                                                        wrongAnswerIds[m] = wordIdInteger;
                                                        dbReader.insertWord(wrongAnswer);
                                                        dbReader.updateWord(wrongAnswer);
                                                    }
                                                    newQuestion.setOtherAnswerOneId(wrongAnswerIds[0]);
                                                    newQuestion.setOtherAnswerTwoId(wrongAnswerIds[1]);
                                                    newQuestion.setOtherAnswerThreeId(wrongAnswerIds[2]);
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
                                                dbReader.updateTest(newTest);
                                                System.out.println("Test inserted");

                                            }
                                            JSONArray examsArray = elementsObject.getJSONArray("exams");
                                            int examsCount = examsArray.length();
                                            for (int n = 0; n < examsCount; n++) {
                                                System.out.println("Pobieram egzamin");
                                                String examId = examsArray.getJSONObject(0).get("id").toString();
                                                int examIdInteger = Integer.parseInt(examId);
                                                URL examEndpoint = new URL("http://pzmmd.cba.pl/api/exam?id=" + examIdInteger);
                                                HttpURLConnection examConnection = (HttpURLConnection) examEndpoint.openConnection();
                                                examConnection.setRequestMethod("GET");
                                                examConnection.setDoOutput(true);
                                                examConnection.connect();

                                                BufferedReader br4 = new BufferedReader(new InputStreamReader(examEndpoint.openStream()));
                                                StringBuilder sb4 = new StringBuilder();

                                                String line4;
                                                while ((line4 = br4.readLine()) != null) {
                                                    sb4.append(line4 + "\n");
                                                }
                                                br4.close();
                                                String examString = sb4.toString();
                                                examConnection.disconnect();
                                                JSONObject examObject = new JSONObject(examString);
                                                Exam newExam = new Exam();
                                                JSONObject singleExam = examObject.getJSONObject("0");
                                                String isNew = examObject.get("isNew").toString();
                                                Boolean isNewBoolean = Boolean.parseBoolean(isNew);
                                                if(isNewBoolean == false) {
                                                    Exam completedExam = new Exam();
                                                    completedExam.setId(examIdInteger);
                                                    completedExam.setLessonId(lessonIdInteger);
                                                    String examScore = singleExam.getString("score");
                                                    Integer examScoreInteger = Integer.parseInt(examScore);
                                                    completedExam.setScore(examScoreInteger);
                                                    String examGrade = singleExam.getString("grade");
                                                    Integer examGradeInteger = Integer.parseInt(examGrade);
                                                    completedExam.setGrade(examGradeInteger);
                                                    System.out.println("Ocena z egzaminu: " + examGradeInteger);
                                                    String isPassed = singleExam.getString("isPassed");
                                                    Boolean isPassedBoolean = Boolean.parseBoolean(isPassed);
                                                    if (isPassedBoolean == true) {
                                                        completedExam.setIsPassed(1);
                                                    }
                                                    else {
                                                        completedExam.setIsPassed(0);
                                                    }
                                                    completedExam.setIsNew(0);
                                                    String totalPoints = singleExam.getString("totalPoints");
                                                    Integer totalPointsInteger = Integer.parseInt(totalPoints);
                                                    completedExam.setPointsToPass(totalPointsInteger);
                                                    dbReader.insertExam(completedExam);
                                                    dbReader.updateExam(completedExam);
                                                    System.out.println("Completed exam inserted");
                                                }
                                                else {
                                                    String testId = singleExam.get("id").toString();
                                                    Integer testIdInteger = Integer.parseInt(testId);
                                                    newExam.setId(testIdInteger);
                                                    String testName = singleExam.get("name").toString();
                                                    newExam.setName(testName);
                                                    String testDescription = singleExam.get("description").toString();
                                                    newExam.setDescription(testDescription);
                                                    int inb = 0;
                                                    if (isNewBoolean == true) {
                                                        inb = 1;
                                                    } else {
                                                        inb = 0;
                                                    }
                                                    newExam.setScore(0);
                                                    newExam.setIsNew(inb);
                                                    newExam.setIsLocal(1);
                                                    newExam.setIsPassed(0);
                                                    newExam.setLessonId(lessonIdInteger);
                                                    if (isNewBoolean == true) {
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
                                                                Word answerWord = new Word();
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
                                                                    String pictureUrl = questionAnswer.get("picture").toString();
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
                                                            JSONArray otherAnswers = singleQuestion.getJSONArray("others");
                                                            int othersCount = otherAnswers.length();
                                                            int wrongAnswerIds[] = new int[otherAnswers.length()];
                                                            for (int o = 0; o < othersCount; o++) {
                                                                Word wrongAnswer = new Word();
                                                                JSONObject singleOtherAnswer = otherAnswers.getJSONObject(o);
                                                                String wordId = singleOtherAnswer.get("id").toString();
                                                                Integer wordIdInteger = Integer.parseInt(wordId);
                                                                wrongAnswer.setId(wordIdInteger);
                                                                String nativeWord = singleOtherAnswer.get("nativeWord").toString();
                                                                wrongAnswer.setNativeWord(nativeWord);
                                                                String translatedWord = singleOtherAnswer.get("translatedWord").toString();
                                                                wrongAnswer.setTranslatedWord(translatedWord);
                                                                String nativeDefinition = singleOtherAnswer.get("nativeDefinition").toString();
                                                                wrongAnswer.setNativeDefinition(nativeDefinition);
                                                                String translatedDefinition = singleOtherAnswer.get("translatedDefinition").toString();
                                                                wrongAnswer.setTranslatedDefinition(translatedDefinition);
                                                                if (singleOtherAnswer.has("nativeSound")) {
                                                                    String nativeSound = singleOtherAnswer.get("nativeSound").toString();
                                                                    wrongAnswer.setNativeSound(nativeSound);
                                                                }
                                                                if (singleOtherAnswer.has("translatedSound")) {
                                                                    String translatedSound = singleOtherAnswer.get("translatedSound").toString();
                                                                    wrongAnswer.setTranslatedSound(translatedSound);
                                                                }
                                                                if (singleOtherAnswer.has("picture")) {
                                                                    String pictureUrl = singleOtherAnswer.get("picture").toString();
                                                                    wrongAnswer.setPicture(pictureUrl);
                                                                }
                                                                if (singleOtherAnswer.has("tags")) {
                                                                    String wordTags = singleOtherAnswer.get("tags").toString();
                                                                    wrongAnswer.setTags(wordTags);
                                                                }
                                                                wrongAnswerIds[o] = wordIdInteger;
                                                                dbReader.insertWord(wrongAnswer);
                                                                dbReader.updateWord(wrongAnswer);
                                                            }
                                                            newQuestion.setOtherAnswerOneId(wrongAnswerIds[0]);
                                                            newQuestion.setOtherAnswerTwoId(wrongAnswerIds[1]);
                                                            newQuestion.setOtherAnswerThreeId(wrongAnswerIds[2]);
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

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } /*catch (JSONException e) {
                e.printStackTrace();
            }*/
                        System.out.println(courseIdInteger + " " + courseName + " " + description + " " + createdAt + " " + levelName
                                + " " + teacherFirstName + " " + teacherLastName + " " + nativeLanguage + " " + learningLanguage);
                    }
                   // return true;
                } catch (JSONException e) {
                    e.printStackTrace();
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
                Intent intent = new Intent(SynchronizationActivity.this, MainActivity.class);
                startActivity(intent);
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }


    }
}
