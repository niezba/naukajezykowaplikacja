package com.example.mniez.myapplication.StudentModule;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.mniez.myapplication.LoginActivity;
import com.example.mniez.myapplication.ObjectHelper.Exam;
import com.example.mniez.myapplication.ObjectHelper.ExamQuestion;
import com.example.mniez.myapplication.StudentModule.ActivityAdapter.CourseElementListAdapter;
import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Language;
import com.example.mniez.myapplication.ObjectHelper.Lecture;
import com.example.mniez.myapplication.ObjectHelper.Lesson;
import com.example.mniez.myapplication.ObjectHelper.QuestionAnswerType;
import com.example.mniez.myapplication.ObjectHelper.Test;
import com.example.mniez.myapplication.ObjectHelper.TestQuestion;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;
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

    public int courseId;
    private RecyclerView recyclerView;
    private CourseElementListAdapter mAdapter;

    private View mProgressView;
    String currentId;
    String currentRole;
    String courseImage;

    String currentUsername;
    String currentPassword;

    ArrayList<Lesson> lessonList = new ArrayList<Lesson>();
    ArrayList<Integer> lessonIdList = new ArrayList<>();
    ArrayList<Test> courseTestsList = new ArrayList<>();
    ArrayList<Lecture> courseLecturesList = new ArrayList<>();
    ArrayList<Exam> courseExamsList = new ArrayList<>();
    private CourseElementsActivity.LessonFetchTask mFetchTask = null;
    private UserLoginTask mAuthTask = null;

    MobileDatabaseReader dbReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        courseId = 0;
        courseId = extras.getInt("courseId", 0);
        setContentView(R.layout.activity_course_elements);
        supportPostponeEnterTransition();
        courseImage = extras.getString("courseImage");
        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        currentUsername = sharedpreferences.getString(PREFERENCES_USERNAME, "");
        currentPassword = sharedpreferences.getString(PREFERENCES_PASSWORD, "");
        final ImageView imageView = (ImageView) findViewById(R.id.imageView2);
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
        final String courseName = intent.getStringExtra("titleBar");
        setTitle(courseName);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CourseDetailsActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("courseImage", courseImage);
                intent.putExtra("imageTransition", ViewCompat.getTransitionName(imageView));
                intent.putExtra("titleBar", courseName);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(CourseElementsActivity.this, imageView, "courseImage");
                view.getContext().startActivity(intent, options.toBundle());
            }
        });
        dbReader = new MobileDatabaseReader(getApplicationContext());
        dbReader.getWritableDatabase();
        showProgress(true);
        mFetchTask = new CourseElementsActivity.LessonFetchTask(courseId);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCourseElementList);
        recyclerView.setHasFixedSize(true);
        mAdapter = new CourseElementListAdapter(lessonList, courseTestsList, courseLecturesList, courseExamsList, this, recyclerView, courseId);
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
                                //newTest.setScore(0);
                                newTest.setIsNew(1);
                                newTest.setIsLocal(1);
                                //newTest.setIsCompleted(0);
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
                            JSONArray examsArray = elementsObject.getJSONArray("exams");
                            int examsCount = examsArray.length();
                            for (int j = 0; j<examsCount; j++) {
                                String examId = examsArray.getJSONObject(0).get("id").toString();
                                int examIdInteger = Integer.parseInt(examId);
                                URL examEndpoint = new URL("http://10.0.2.2:8000/api/exam?id="+examIdInteger);
                                HttpURLConnection examConnection = (HttpURLConnection) examEndpoint.openConnection();
                                examConnection.setRequestMethod("GET");
                                examConnection.setDoOutput(true);
                                examConnection.connect();

                                BufferedReader br3 = new BufferedReader(new InputStreamReader(examEndpoint.openStream()));
                                StringBuilder sb3 = new StringBuilder();

                                String line3;
                                while ((line3 = br3.readLine()) != null) {
                                    sb3.append(line3 + "\n");
                                }
                                br3.close();
                                String examString = sb3.toString();
                                JSONObject examObject = new JSONObject(examString);
                                Exam newExam = new Exam();
                                JSONObject singleExam = examObject.getJSONObject("0");
                                String testId = singleExam.get("id").toString();
                                Integer testIdInteger = Integer.parseInt(testId);
                                newExam.setId(testIdInteger);
                                String testName = singleExam.get("name").toString();
                                newExam.setName(testName);
                                String testDescription = singleExam.get("description").toString();
                                newExam.setDescription(testDescription);
                                String isNew = examObject.get("isNew").toString();
                                Boolean isNewBoolean = Boolean.parseBoolean(isNew);
                                int inb = 0;
                                if (isNewBoolean == true) {
                                    inb = 1;
                                }
                                else {
                                    inb = 0;
                                }
                                newExam.setScore(0);
                                newExam.setIsNew(inb);
                                newExam.setIsLocal(1);
                                newExam.setIsPassed(0);
                                newExam.setLessonId(lessonIdInteger);
                                if(isNewBoolean == true) {
                                    JSONArray questionsArray = singleExam.getJSONArray("questions");
                                    int questionsCount = questionsArray.length();
                                    for (int k = 0; k < questionsCount; k++) {
                                        ExamQuestion newQuestion = new ExamQuestion();
                                        JSONObject singleQuestion = questionsArray.getJSONObject(k);
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
                                        for (int l = 0; l < othersCount; l++) {
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
                                        dbReader.insertExamQuestion(newQuestion);
                                    }
                                }
                                dbReader.insertExam(newExam);
                                System.out.println("Exam inserted");
                            }
                        } catch (JSONException e) {
                            JSONObject jsonObjectX = new JSONObject(jsonString);
                            String errCode = jsonObjectX.get("error_code").toString();
                            System.out.println("Error code: " + errCode);
                            if(errCode.equals("1")) {
                                return false;
                            }
                        }

                    }
                    return true;
                } catch (JSONException e) {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String errCode = jsonObject.get("error_code").toString();
                    System.out.println("Error code: " + errCode);
                    if(errCode.equals("1")) {
                        return false;
                    }
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



            if (success) {
                lessonList.addAll(dbReader.selectAllLessonsForCourse(fetchedCourseId));
                lessonIdList.addAll(dbReader.selectAllLessonsIdsForCourse(fetchedCourseId));
                for (Integer element : lessonIdList) {
                    courseTestsList.addAll(dbReader.selectAllTestsForLesson(element));
                    courseLecturesList.addAll(dbReader.selectAllLecturesForLesson(element));
                    courseExamsList.addAll(dbReader.selectAllExamsForLesson(element));
                }
                mAdapter.notifyDataSetChanged();
                mAdapter.getItemCount();
                System.out.println(lessonList);
                mFetchTask = null;
                showProgress(false);
            } else {
                mAuthTask = new UserLoginTask(currentUsername, currentPassword);
                mAuthTask.execute();
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

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                URL webpageEndpoint = new URL("http://10.0.2.2:8000/api/login?username="+mEmail+"&password="+mPassword);
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

                JSONObject jsonObject = new JSONObject(jsonString);
                String errCode = jsonObject.get("error_code").toString();
                myConnection.disconnect();
                System.out.println("Error code: " + errCode);
                if(errCode.equals("0")) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    String appUserName = jsonObject.get("username").toString();
                    String appNameSurname = jsonObject.get("firstName").toString() + " " + jsonObject.get("lastName").toString();
                    String appUserId = jsonObject.get("id").toString();
                    JSONArray appRoles = jsonObject.getJSONArray("roles");
                    String appRole = appRoles.getString(0);
                    editor.putString(PREFERENCES_USERNAME, appUserName);
                    editor.putString(PREFERENCES_NAMESURNAME, appNameSurname);
                    editor.putString(PREFERENCES_PASSWORD, mPassword);
                    editor.putString(PREFERENCES_ID, appUserId);
                    editor.putString(PREFERENCES_ROLE, appRole);
                    editor.commit();
                    return true;
                }
                else {
                    return false;
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
            mAuthTask = null;
            if (success) {
                mFetchTask = new LessonFetchTask(courseId);
                mFetchTask.execute();
            } else {
                showProgress(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseElementsActivity.this);
                builder.setMessage("Nastąpił problem z uwierzytelnieniem. Zaloguj się ponownie.")
                        .setTitle("Ups...").setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                        sharedpreferences.edit().clear().commit();
                        CourseElementsActivity.this.deleteDatabase("dummyDatabase");
                        Intent intent = new Intent(CourseElementsActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }


    }

}
