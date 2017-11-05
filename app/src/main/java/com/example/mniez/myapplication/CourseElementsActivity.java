package com.example.mniez.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.mniez.myapplication.ActivityAdapter.CourseElementListAdapter;
import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Language;
import com.example.mniez.myapplication.ObjectHelper.Lecture;
import com.example.mniez.myapplication.ObjectHelper.Lesson;
import com.example.mniez.myapplication.ObjectHelper.QuestionAnswerType;
import com.example.mniez.myapplication.ObjectHelper.Test;
import com.example.mniez.myapplication.ObjectHelper.TestQuestion;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CourseElementsActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    private static final String MY_PREFERENCES = "DummyLangPreferences";
    private static final String PREFERENCES_USERNAME = "loggedUserLogin";
    private static final String PREFERENCES_PASSWORD = "loggedUserPassword";
    private static final String PREFERENCES_NAMESURNAME = "loggedUserNameSurname";
    private static final String PREFERENCES_ROLE = "loggedUserMainRole";
    private static final String PREFERENCES_ID = "loggedUserId";
    private static final String ADMIN_ROLE_NAME = "Administrator";
    private static final String TEACHER_ROLE_NAME = "Nauczyciel";
    private static final String STUDENT_ROLE_NAME = "Uczeń";

    private RecyclerView recyclerView;
    private CourseElementListAdapter mAdapter;

    private View mProgressView;
    String currentId;
    String currentRole;

    ArrayList<Lesson> lessonList = new ArrayList<Lesson>();
    ArrayList<Integer> lessonIdList = new ArrayList<>();
    ArrayList<Test> courseTestsList = new ArrayList<>();
    ArrayList<Lecture> courseLecturesList = new ArrayList<>();

    private CourseElementsActivity.LessonFetchTask mFetchTask = null;

    MobileDatabaseReader dbReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        int courseId = 0;
        courseId = extras.getInt("courseId", 0);
        setContentView(R.layout.activity_course_elements);
        supportPostponeEnterTransition();
        String courseImage = extras.getString("courseImage");
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString("imageTransition");
            imageView.setTransitionName(imageTransitionName);
        }
        Picasso.with(this)
                .load(courseImage)
                .noFade()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });
        mProgressView = findViewById(R.id.login_progress_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String courseName = intent.getStringExtra("titleBar");
        setTitle(courseName);
        dbReader = new MobileDatabaseReader(getApplicationContext());
        dbReader.getWritableDatabase();
        showProgress(true);
        mFetchTask = new CourseElementsActivity.LessonFetchTask(courseId);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCourseElementList);
        recyclerView.setHasFixedSize(true);
        mAdapter = new CourseElementListAdapter(lessonList, courseTestsList, courseLecturesList, this, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mFetchTask.execute((Void) null);
        System.out.println(mAdapter.getItemCount());
        dbReader.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            /*mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });*/

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
            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class LessonFetchTask extends AsyncTask<Void, Void, Boolean> {

        private final int fetchedCourseId;

        LessonFetchTask(int courseId) {
            fetchedCourseId = courseId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                URL webpageEndpoint = new URL("http://10.0.2.2:8000/api/lessonsForCourse/"+fetchedCourseId);
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
                System.out.println("JSON Lessons data downloaded");

                try {
                    JSONArray jsonObject = new JSONArray(jsonString);
                    String jsonObjectString = jsonObject.toString();
                    System.out.println(jsonObjectString);
                    myConnection.disconnect();
                    int lessonsCount = jsonObject.length();
                    for (int i = 0; i < lessonsCount; i++){
                        Lesson newLesson = new Lesson();
                        JSONObject singleLesson = jsonObject.getJSONObject(i);
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
                        }
                        dbReader.insertLesson(newLesson, fetchedCourseId);
                        System.out.println("Lesson inserted");
                        URL lessonElementsEndpoint = new URL("http://10.0.2.2:8000/api/lesson/"+lessonIdInteger);
                        HttpURLConnection lessonConnection = (HttpURLConnection) lessonElementsEndpoint.openConnection();
                        lessonConnection.setRequestMethod("GET");
                        lessonConnection.setDoOutput(true);
                        lessonConnection.connect();

                        BufferedReader br2 = new BufferedReader(new InputStreamReader(lessonElementsEndpoint.openStream()));
                        StringBuilder sb2 = new StringBuilder();

                        String line2;
                        while ((line2 = br2.readLine()) != null) {
                            sb2.append(line2 + "\n");
                        }
                        br2.close();

                        String lessonString = sb2.toString();
                        try {
                            JSONArray lessonObject = new JSONArray(lessonString);
                            String lessonObjectString = lessonObject.toString();
                            System.out.println(lessonObjectString);
                            JSONObject elementsObject = lessonObject.getJSONObject(0);
                            JSONArray testsArray = elementsObject.getJSONArray("tests");
                            int testsCount = testsArray.length();
                            for (int j = 0; j<testsCount; j++) {
                                Test newTest = new Test();
                                JSONObject singleTest = testsArray.getJSONObject(j);
                                String testId = singleTest.get("id").toString();
                                Integer testIdInteger = Integer.parseInt(testId);
                                newTest.setId(testIdInteger);
                                String testName = singleTest.get("name").toString();
                                newTest.setName(testName);
                                String testDescription = singleTest.get("description").toString();
                                newTest.setDescription(testDescription);
                                newTest.setScore(0);
                                newTest.setIsNew(1);
                                newTest.setIsLocal(1);
                                newTest.setIsCompleted(0);
                                newTest.setLessonId(lessonIdInteger);
                                JSONArray questionsArray = singleTest.getJSONArray("questions");
                                int questionsCount = questionsArray.length();
                                for (int k = 0; k<questionsCount; k++) {
                                    TestQuestion newQuestion = new TestQuestion();
                                    JSONObject singleQuestion = questionsArray.getJSONObject(k);
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
                                        String nativeLanguage = answerNativeLang.get("id").toString();
                                        Integer nativeLanguageInteger = Integer.parseInt(nativeLanguage);
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
                                        dbReader.insertLanguage(nativeLang);
                                        dbReader.insertLanguage(translatLang);
                                    }
                                    JSONArray otherAnswers = singleQuestion.getJSONArray("others");
                                    int othersCount = otherAnswers.length();
                                    int wrongAnswerIds[] = new int[otherAnswers.length()];
                                    for (int l=0; l<othersCount; l++) {
                                        Word wrongAnswer = new Word();
                                        JSONObject singleOtherAnswer = otherAnswers.getJSONObject(l);
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
                                        wrongAnswerIds[l] = wordIdInteger;
                                        dbReader.insertWord(wrongAnswer);
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
                                System.out.println("Test inserted");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    return true;
                } catch (JSONException e) {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String errCode = jsonObject.get("error_code").toString();
                    System.out.println("Error code: " + errCode);
                }
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
            lessonList.addAll(dbReader.selectAllLessonsForCourse(fetchedCourseId));
            lessonIdList.addAll(dbReader.selectAllLessonsIdsForCourse(fetchedCourseId));
            for (Integer element : lessonIdList) {
                courseTestsList.addAll(dbReader.selectAllTestsForLesson(element));
                courseLecturesList.addAll(dbReader.selectAllLecturesForLesson(element));
            }
            mAdapter.notifyDataSetChanged();
            mAdapter.getItemCount();
            System.out.println(lessonList);
            mFetchTask = null;
            showProgress(false);


            if (success) {

            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mFetchTask = null;
            showProgress(false);
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
