package com.example.mniez.myapplication.TeacherModule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.TestQuestion;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.TeacherModule.Fragments.AnswersFragment;
import com.example.mniez.myapplication.TeacherModule.Fragments.ImageAnswersFragment;
import com.example.mniez.myapplication.TeacherModule.Fragments.InputAnswersFragment;
import com.example.mniez.myapplication.TeacherModule.Fragments.NumberFragment;
import com.example.mniez.myapplication.TeacherModule.Fragments.ProgressFragment;
import com.example.mniez.myapplication.TeacherModule.Fragments.QuestionFragment;
import com.example.mniez.myapplication.TeacherModule.Fragments.SoundAnswersFragment;
import com.example.mniez.myapplication.TeacherModule.TeacherMainActivity;

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
import java.util.Random;

public class TestActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    private static final String MY_PREFERENCES = "DummyLangPreferences";
    private static final String PREFERENCES_OFFLINE = "isOffline";
    private static final String ANSWERS_TAG = "answers";
    private static final String NUMBER_TAG = "number";
    private static final String QUESTION_TAG = "question";
    private static final String PROGRESS_TAG = "progress";
    private static final String IMG_ANSWERS_TAG = "imageAnswers";
    private static final String SOUND_ANSWERS_TAG = "soundAnswers";
    private static final String INPUT_ANSWERS_TAG = "inputAnswers";
    private int testId;
    ArrayList<TestQuestion> testQuestions;
    ArrayList<Word> questionWords;
    protected int questionCounter = 0;
    protected int currentQuestionTypeId;
    protected int currentQuestionWordId;
    MobileDatabaseReader dbReader;
    AnswersFragment mAnswersFragment;
    NumberFragment mNumberFragment;
    ProgressFragment mProgressFragment;
    QuestionFragment mQuestionFragment;
    ImageAnswersFragment mImageAnswersFragment;
    SoundAnswersFragment mSoundsAnswersFragment;
    InputAnswersFragment mInputAnswersFragment;
    String inputAnswerString;
    protected int currentAnswerId;
    protected String answerString;
    int courseId;
    int isOffline;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setQuestion(-1);
                    return true;
                case R.id.navigation_dashboard:
                    onBackPressed();
                    return true;
                case R.id.navigation_notifications:
                    setQuestion(1);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        testId = extras.getInt("test_id", 0);
        courseId = extras.getInt("course_id", 0);
        setContentView(R.layout.activity_test);
        dbReader = new MobileDatabaseReader(getApplicationContext());
        String testName = dbReader.selectTestName(testId);
        setTitle(testName);
        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        isOffline = sharedpreferences.getInt(PREFERENCES_OFFLINE, 0);
        testQuestions = dbReader.selectAllQuestionsForTest(testId);
        System.out.println(testQuestions.size());
        questionWords = populateCurrentListOfWords(testQuestions.get(0));
        dbReader.close();
        Bundle args = new Bundle();
        args.putInt("question_num", questionCounter + 1);
        args.putInt("question_sum", testQuestions.size());
        args.putInt("question_id", testQuestions.get(questionCounter).getId());
        android.app.FragmentManager fragmentManager = getFragmentManager();
        mNumberFragment = (NumberFragment) fragmentManager.findFragmentByTag(NUMBER_TAG);
        if (mNumberFragment == null) {
            mNumberFragment = new NumberFragment();
            mNumberFragment.setArguments(args);
            fragmentManager.beginTransaction().add(R.id.number_fragment_container, mNumberFragment, NUMBER_TAG).commit();
        }
        Bundle args2 = new Bundle();
        args2.putString("quesstionToAsk", testQuestions.get(questionCounter).getQuestion());
        args2.putInt("questionType", testQuestions.get(questionCounter).getQuestionTypeId());
        args2.putInt("questionWord", testQuestions.get(questionCounter).getAnswerId());
        args2.putInt("isOffline", isOffline);
        mQuestionFragment = (QuestionFragment) fragmentManager.findFragmentByTag(QUESTION_TAG);
        if (mQuestionFragment == null) {
            mQuestionFragment = new QuestionFragment();
            mQuestionFragment.setArguments(args2);
            fragmentManager.beginTransaction().add(R.id.question_fragment_container, mQuestionFragment, QUESTION_TAG).commit();
        }
        Bundle args3 = new Bundle();
        args3.putInt("answerType", testQuestions.get(questionCounter).getAnswerTypeId());
        populateAnswersForQuestion(testQuestions.get(questionCounter));
        args3.putString("answerString", answerString);
        args3.putInt("answerIds", currentAnswerId);
        args3.putInt("isOffline", isOffline);
        args3.putInt("correctId", testQuestions.get(questionCounter).getAnswerId());
        mAnswersFragment = (AnswersFragment) fragmentManager.findFragmentByTag(ANSWERS_TAG);
        if (mAnswersFragment == null) {
            mAnswersFragment = new AnswersFragment();
            mAnswersFragment.setArguments(args3);
            fragmentManager.beginTransaction().add(R.id.answer_fragment_container, mAnswersFragment, ANSWERS_TAG).commit();
        }
        Bundle args4 = new Bundle();
        args4.putDouble("progress", ((((float) questionCounter)+1)/ (float) testQuestions.size())*100.0);
        System.out.println("Progres:" + ((((float) questionCounter)+1)/ (float) testQuestions.size())*100.0);
        mProgressFragment = (ProgressFragment) fragmentManager.findFragmentByTag(PROGRESS_TAG);
        if (mProgressFragment == null) {
            mProgressFragment = new ProgressFragment();
            mProgressFragment.setArguments(args4);
            fragmentManager.beginTransaction().add(R.id.progress_fragment_container, mProgressFragment, PROGRESS_TAG).commit();
        }
        mImageAnswersFragment = (ImageAnswersFragment) fragmentManager.findFragmentByTag(IMG_ANSWERS_TAG);
        if (mImageAnswersFragment == null) {
            mImageAnswersFragment = new ImageAnswersFragment();
            mImageAnswersFragment.setArguments(args3);
            fragmentManager.beginTransaction().add(R.id.answer_fragment_container, mImageAnswersFragment, IMG_ANSWERS_TAG).commit();
        }
        mSoundsAnswersFragment = (SoundAnswersFragment) fragmentManager.findFragmentByTag(SOUND_ANSWERS_TAG);
        if (mSoundsAnswersFragment == null) {
            mSoundsAnswersFragment = new SoundAnswersFragment();
            mSoundsAnswersFragment.setArguments(args3);
            fragmentManager.beginTransaction().add(R.id.answer_fragment_container, mSoundsAnswersFragment, SOUND_ANSWERS_TAG).commit();
        }
        Bundle args5 = new Bundle();
        args5.putInt("answerType", testQuestions.get(questionCounter).getAnswerTypeId());
        populateAnswersForInputQuestion(testQuestions.get(questionCounter));
        args5.putString("answerString", inputAnswerString);
        mInputAnswersFragment = (InputAnswersFragment) fragmentManager.findFragmentByTag(INPUT_ANSWERS_TAG);
        if (mInputAnswersFragment == null) {
            mInputAnswersFragment = new InputAnswersFragment();
            mInputAnswersFragment.setArguments(args5);
            fragmentManager.beginTransaction().add(R.id.answer_fragment_container, mInputAnswersFragment, INPUT_ANSWERS_TAG).commit();
        }
        switchCurrentAnswerType(testQuestions.get(questionCounter));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    protected ArrayList<Word> populateCurrentListOfWords(TestQuestion testQuestion) {
        ArrayList<Word> newList = new ArrayList<>();
        System.out.println(testQuestion.getId());
        Word correctAnswer = dbReader.getParticularWordData(testQuestion.getAnswerId());
        newList.add(correctAnswer);
        return newList;
    }

    protected void populateAnswersForQuestion(TestQuestion testQuestion) {
        Random generator = new Random();
        String tAnswerString = new String();
        int tAnswersOrder;
        switch (testQuestion.getAnswerTypeId()) {
            case 7:
                tAnswerString = questionWords.get(0).getNativeWord();
                break;
            case 8:
                tAnswerString = questionWords.get(0).getTranslatedWord();
                break;
            case 10:
                tAnswerString = questionWords.get(0).getTranslatedWord();
                break;
            case 12:
                tAnswerString = questionWords.get(0).getNativeDefinition();
                break;
            case 13:
                tAnswerString = questionWords.get(0).getTranslatedDefinition();
                break;
            default:
                tAnswerString = questionWords.get(0).getTranslatedWord();
                break;
        }
        answerString = tAnswerString;
        currentAnswerId = testQuestions.get(questionCounter).getAnswerId();
    }

    protected void populateAnswersForInputQuestion(TestQuestion testQuestion) {
        switch (testQuestion.getAnswerTypeId()) {
            case 16:
                inputAnswerString = dbReader.getParticularWordData(testQuestion.getAnswerId()).getNativeWord();
                break;
            case 17:
                inputAnswerString = dbReader.getParticularWordData(testQuestion.getAnswerId()).getTranslatedWord();
                break;
            default:
                inputAnswerString = "";
                break;
        }
        currentAnswerId = testQuestions.get(questionCounter).getAnswerId();
    }

    public void switchCurrentAnswerType(TestQuestion testQuestion) {
        switch (testQuestion.getAnswerTypeId()) {
            case 7: case 10: case 12: case 13:
                System.out.println("Wyświetlam fragment typu select text");
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction().show(mAnswersFragment);
                if(mImageAnswersFragment != null) ft.hide(mImageAnswersFragment);
                if(mInputAnswersFragment != null) ft.hide(mInputAnswersFragment);
                if(mSoundsAnswersFragment != null) ft.hide(mSoundsAnswersFragment).commit();
                break;
            case 8:
                System.out.println("Wyświetlam fragment typu select image");
                android.app.FragmentTransaction fx = getFragmentManager().beginTransaction().show(mImageAnswersFragment);
                if(mAnswersFragment != null) fx.hide(mAnswersFragment);
                if(mInputAnswersFragment != null) fx.hide(mInputAnswersFragment);
                if(mSoundsAnswersFragment != null) fx.hide(mSoundsAnswersFragment).commit();
                break;
            case 9:
                System.out.println("Wyświetlam fragment typu select sound");
                android.app.FragmentTransaction fz = getFragmentManager().beginTransaction().show(mSoundsAnswersFragment);
                if(mAnswersFragment != null) fz.hide(mAnswersFragment);
                if(mInputAnswersFragment != null) fz.hide(mInputAnswersFragment);
                if(mImageAnswersFragment != null) fz.hide(mImageAnswersFragment).commit();
                break;
            case 16: case 17:
                System.out.println("Wyświetlam fragment typu input");
                android.app.FragmentTransaction fa = getFragmentManager().beginTransaction().show(mInputAnswersFragment);
                if(mAnswersFragment != null) fa.hide(mAnswersFragment);
                if(mSoundsAnswersFragment != null) fa.hide(mSoundsAnswersFragment);
                if(mImageAnswersFragment != null) fa.hide(mImageAnswersFragment).commit();
                break;
            default:
                android.app.FragmentTransaction fy = getFragmentManager().beginTransaction().show(mAnswersFragment);
                if(mImageAnswersFragment != null) fy.hide(mImageAnswersFragment);
                if(mInputAnswersFragment != null) fy.hide(mInputAnswersFragment);
                if(mSoundsAnswersFragment != null) fy.hide(mSoundsAnswersFragment).commit();
                break;
        }
    }


    public void setQuestion(int prevOrNext) {
        if (prevOrNext == 1) {
            if (questionCounter < testQuestions.size() - 1) {
                questionCounter++;
                currentQuestionTypeId = testQuestions.get(questionCounter).getQuestionTypeId();
                currentQuestionWordId = testQuestions.get(questionCounter).getAnswerId();
                String newQuestionToAsk = testQuestions.get(questionCounter).getQuestion();
                mNumberFragment.setNumberOfQuestion(questionCounter + 1);
                mQuestionFragment.setQuestionBasedOnType(currentQuestionTypeId, currentQuestionWordId, newQuestionToAsk);
                mProgressFragment.setProgress(((((float) questionCounter) + 1) / (float) testQuestions.size()) * 100.0);
                questionWords = populateCurrentListOfWords(testQuestions.get(questionCounter));
                switchCurrentAnswerType(testQuestions.get(questionCounter));
                populateAnswersForQuestion(testQuestions.get(questionCounter));
                populateAnswersForInputQuestion(testQuestions.get(questionCounter));
                mAnswersFragment.initiateAnswers(answerString, testQuestions.get(questionCounter).getAnswerTypeId(), currentAnswerId, currentAnswerId, 0, testQuestions.get(questionCounter).getAnswerId());
                mImageAnswersFragment.initiateAnswers(answerString, testQuestions.get(questionCounter).getAnswerTypeId(), currentAnswerId, currentAnswerId, 0, testQuestions.get(questionCounter).getAnswerId());
                mSoundsAnswersFragment.initiateAnswers(answerString, testQuestions.get(questionCounter).getAnswerTypeId(), currentAnswerId, currentAnswerId, 0, testQuestions.get(questionCounter).getAnswerId());
                String currentString;
                mInputAnswersFragment.initiateAnswers(inputAnswerString, testQuestions.get(questionCounter).getAnswerTypeId(), "", currentAnswerId, 0);
            } else {
                Toast.makeText(this, "To jest ostatnie pytanie w teście", Toast.LENGTH_SHORT).show();
            }
        }
        else if (prevOrNext == 2) {
            System.out.println("Ustawiam wyniki testu");
            questionCounter = 0;
            currentQuestionTypeId = testQuestions.get(questionCounter).getQuestionTypeId();
            currentQuestionWordId = testQuestions.get(questionCounter).getAnswerId();
            String newQuestionToAsk = testQuestions.get(questionCounter).getQuestion();
            mNumberFragment.setNumberOfQuestion(questionCounter + 1);
            mQuestionFragment.setQuestionBasedOnType(currentQuestionTypeId, currentQuestionWordId, newQuestionToAsk);
            mProgressFragment.setProgress(((((float) questionCounter)+1)/ (float) testQuestions.size())*100.0);
            questionWords = populateCurrentListOfWords(testQuestions.get(questionCounter));
            switchCurrentAnswerType(testQuestions.get(questionCounter));
            populateAnswersForQuestion(testQuestions.get(questionCounter));
            populateAnswersForInputQuestion(testQuestions.get(questionCounter));
            mAnswersFragment.initiateAnswers(answerString, testQuestions.get(questionCounter).getAnswerTypeId(), currentAnswerId, currentAnswerId, 0, testQuestions.get(questionCounter).getAnswerId());
            mImageAnswersFragment.initiateAnswers(answerString, testQuestions.get(questionCounter).getAnswerTypeId(), currentAnswerId, currentAnswerId, 0, testQuestions.get(questionCounter).getAnswerId());
            mSoundsAnswersFragment.initiateAnswers(answerString, testQuestions.get(questionCounter).getAnswerTypeId(), currentAnswerId, currentAnswerId, 0, testQuestions.get(questionCounter).getAnswerId());
            String currentString;
            mInputAnswersFragment.initiateAnswers(inputAnswerString, testQuestions.get(questionCounter).getAnswerTypeId(), "", currentAnswerId, 0);
        }
        else if (prevOrNext == -1) {
            if (questionCounter > 0 ) {
                questionCounter--;
                currentQuestionTypeId = testQuestions.get(questionCounter).getQuestionTypeId();
                currentQuestionWordId = testQuestions.get(questionCounter).getAnswerId();
                String newQuestionToAsk = testQuestions.get(questionCounter).getQuestion();
                mNumberFragment.setNumberOfQuestion(questionCounter + 1);
                mQuestionFragment.setQuestionBasedOnType(currentQuestionTypeId, currentQuestionWordId, newQuestionToAsk);
                mProgressFragment.setProgress(((((float) questionCounter)+1)/ (float) testQuestions.size())*100.0);
                questionWords = populateCurrentListOfWords(testQuestions.get(questionCounter));
                switchCurrentAnswerType(testQuestions.get(questionCounter));
                populateAnswersForQuestion(testQuestions.get(questionCounter));
                populateAnswersForInputQuestion(testQuestions.get(questionCounter));
                mAnswersFragment.initiateAnswers(answerString, testQuestions.get(questionCounter).getAnswerTypeId(), currentAnswerId, currentAnswerId, 0, testQuestions.get(questionCounter).getAnswerId());
                mImageAnswersFragment.initiateAnswers(answerString, testQuestions.get(questionCounter).getAnswerTypeId(), currentAnswerId, currentAnswerId, 0, testQuestions.get(questionCounter).getAnswerId());
                mSoundsAnswersFragment.initiateAnswers(answerString, testQuestions.get(questionCounter).getAnswerTypeId(), currentAnswerId, currentAnswerId, 0, testQuestions.get(questionCounter).getAnswerId());
                String currentString;
                mInputAnswersFragment.initiateAnswers(inputAnswerString, testQuestions.get(questionCounter).getAnswerTypeId(), "", currentAnswerId, 0);
            }
            else {
                Toast.makeText(this, "To jest pierwsze pytanie w teście", Toast.LENGTH_SHORT).show();
            }
        }
        else {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
              onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
