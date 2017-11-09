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
import java.util.Random;

public class TestExamActivity extends AppCompatActivity implements AnswersFragment.OnAnswerSelectedListener {

    private static final String ANSWERS_TAG = "answers";
    private static final String NUMBER_TAG = "number";
    private static final String QUESTION_TAG = "question";
    private static final String PROGRESS_TAG = "progress";
    private int testId;
    ArrayList<TestQuestion> testQuestions;
    ArrayList<Word> questionWords;
    protected int questionCounter = 0;
    protected int currentQuestionTypeId;
    protected int currentQuestionWordId;
    int[] answerIds;
    MobileDatabaseReader dbReader;
    AnswersFragment mAnswersFragment;
    NumberFragment mNumberFragment;
    ProgressFragment mProgressFragment;
    QuestionFragment mQuestionFragment;
    public int isCompleted = 0;
    protected int[] currentAnswerIds = new int[4];
    protected String[] answersString = new String[4];
    int[][] answersOrderArray = {{1,2,3,4},{1,2,4,3},{1,3,2,4},{1,3,4,2},{1,4,2,3},{1,4,3,2},{2,1,3,4},{2,1,4,3},{2,3,1,4},{2,3,4,1},
            {2,4,3,1},{2,4,1,3},{3,1,2,4},{3,1,4,2},{3,2,4,1},{3,2,1,4},{3,4,2,1},{3,4,1,2},{4,1,3,2},{4,1,2,3},{4,2,3,1},{4,2,1,3},{4,3,1,2},{4,3,2,1}};
    int[] answersOrder;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setQuestion(-1);
                    return true;
                case R.id.navigation_dashboard:
                    AlertDialog.Builder builder = new AlertDialog.Builder(TestExamActivity.this);
                    builder.setMessage("Czy chcesz zakończyć rozwiązywanie kursu?")
                            .setTitle("Zakończenie testu");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            isCompleted = 1;
                        }
                    });
                    builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
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
        setContentView(R.layout.activity_test_exam);
        dbReader = new MobileDatabaseReader(getApplicationContext());
        String testName = dbReader.selectTestName(testId);
        setTitle(testName);
        testQuestions = dbReader.selectAllQuestionsForTest(testId);
        System.out.println(testQuestions.size());
        questionWords = populateCurrentListOfWords(testQuestions.get(0));
        dbReader.close();
        answerIds = new int[testQuestions.size()];
        answersOrder = new int[testQuestions.size()];
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
        Bundle args3 = new Bundle();
        args3.putInt("answerType", testQuestions.get(questionCounter).getAnswerTypeId());
        populateAnswersForQuestion(testQuestions.get(questionCounter));
        args3.putStringArray("answerString", answersString);
        args3.putIntArray("answerIds", currentAnswerIds);
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

    protected void populateAnswersForQuestion(TestQuestion testQuestion) {
        Random generator = new Random();
        String[] tAnswerString = new String[4];
        switch (testQuestion.getAnswerTypeId()) {
            case 7:
                for (int i = 0; i<4; i++) {
                    tAnswerString[i] = questionWords.get(i).getTranslatedWord();
                }
                int tAnswersOrder;
                if(answersOrder[questionCounter] == 0) {
                    tAnswersOrder = generator.nextInt(23);
                }
                else {
                    tAnswersOrder = answersOrder[questionCounter];
                }
                for (int i = 0; i<4; i++) {
                    answersString[i] = tAnswerString[answersOrderArray[tAnswersOrder][i]-1];
                    currentAnswerIds[i] = questionWords.get(answersOrderArray[tAnswersOrder][i]-1).getId();
                }
                answersOrder[questionCounter] = tAnswersOrder;
                break;
            default:
                for (int i = 0; i<4; i++) {
                    tAnswerString[i] = "Odpowiedź " + i;
                }
                for (int i = 0; i<4; i++) {
                    answersString[i] = tAnswerString[i];
                }
                break;
        }
    }

    @Override
    public void onAnswerSelected(int answerId) {
        answerIds[questionCounter] = answerId;
        System.out.println("Zaznaczona odpowiedź dla pytania " + (questionCounter+1) + ": " + answerId);
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
                questionWords = populateCurrentListOfWords(testQuestions.get(questionCounter));
                populateAnswersForQuestion(testQuestions.get(questionCounter));
                mAnswersFragment.initiateAnswers(answersString, testQuestions.get(questionCounter).getAnswerTypeId(), answerIds[questionCounter], currentAnswerIds);
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
                questionWords = populateCurrentListOfWords(testQuestions.get(questionCounter));
                populateAnswersForQuestion(testQuestions.get(questionCounter));
                mAnswersFragment.initiateAnswers(answersString, testQuestions.get(questionCounter).getAnswerTypeId(), answerIds[questionCounter], currentAnswerIds);
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
