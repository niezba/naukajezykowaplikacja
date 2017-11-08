package com.example.mniez.myapplication.StudentModule;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Test;
import com.example.mniez.myapplication.ObjectHelper.TestQuestion;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.Fragments.AnswersFragment;
import com.example.mniez.myapplication.StudentModule.Fragments.NumberFragment;
import com.example.mniez.myapplication.StudentModule.Fragments.ProgressFragment;
import com.example.mniez.myapplication.StudentModule.Fragments.QuestionFragment;

import java.util.ArrayList;

public class TestExamActivity extends AppCompatActivity implements AnswersFragment.OnAnswerSelectedListener {

    private static final String ANSWERS_TAG = "answers";
    private static final String NUMBER_TAG = "number";
    private static final String QUESTION_TAG = "question";
    private static final String PROGRESS_TAG = "progress";
    private int testId;
    private int courseId;
    ArrayList<TestQuestion> testQuestions;
    ArrayList<Word> questionWords;
    protected int questionCounter = 0;
    protected int currentQuestionTypeId;
    protected int currentQuestionWordId;
    int[] answerIds;
    private TextView mTextMessage;
    MobileDatabaseReader dbReader;
    AnswersFragment mAnswersFragment;
    NumberFragment mNumberFragment;
    ProgressFragment mProgressFragment;
    QuestionFragment mQuestionFragment;
    public int isCompleted = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setQuestion(-1);
                    return true;
                case R.id.navigation_dashboard:
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
        setContentView(R.layout.activity_test_exam);
        dbReader = new MobileDatabaseReader(getApplicationContext());
        String testName = dbReader.selectTestName(testId);
        setTitle(testName);
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
        mQuestionFragment = (QuestionFragment) fragmentManager.findFragmentByTag(QUESTION_TAG);
        if (mQuestionFragment == null) {
            mQuestionFragment = new QuestionFragment();
            mQuestionFragment.setArguments(args2);
            fragmentManager.beginTransaction().add(R.id.question_fragment_container, mQuestionFragment, QUESTION_TAG).commit();
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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    protected ArrayList<Word> populateCurrentListOfWords(TestQuestion testQuestion) {
        ArrayList<Word> newList = new ArrayList<>();
        System.out.println(testQuestion.getId());
        Word correctAnswer = dbReader.getParticularWordData(testQuestion.getAnswerId());
        Word otherAnswerOne = dbReader.getParticularWordData(testQuestion.getOtherAnswerOneId());
        Word otherAnswerTwo = dbReader.getParticularWordData(testQuestion.getOtherAnswerTwoId());
        Word otherAnswerThree = dbReader.getParticularWordData(testQuestion.getOtherAnswerTwoId());
        newList.add(correctAnswer);
        newList.add(otherAnswerOne);
        newList.add(otherAnswerTwo);
        newList.add(otherAnswerThree);
        System.out.println(correctAnswer.getNativeWord() + " " + otherAnswerOne.getNativeWord() + " " + otherAnswerTwo.getNativeWord() + " " + otherAnswerThree.getNativeWord());
        return newList;
    }

    @Override
    public void onAnswerSelected(int answerId) {
        answerIds[questionCounter] = answerId;
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
                mProgressFragment.setProgress(((((float) questionCounter)+1)/ (float) testQuestions.size())*100.0);
            }
            else {
                Toast.makeText(this, "To jest ostatnie pytanie w teście", Toast.LENGTH_SHORT).show();
            }
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
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                if (isCompleted == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Czy chcesz wrócić do kursu? Twoje odpowiedzi z tego testu zostaną utracone.")
                            .setTitle("Niezapisany test");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            onBackPressed();
                        }
                    });
                    builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    onBackPressed();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
