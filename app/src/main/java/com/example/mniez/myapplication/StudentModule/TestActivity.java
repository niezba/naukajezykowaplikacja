package com.example.mniez.myapplication.StudentModule;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.mniez.myapplication.StudentModule.Fragments.AnswersFragment;
import com.example.mniez.myapplication.StudentModule.Fragments.ImageAnswersFragment;
import com.example.mniez.myapplication.StudentModule.Fragments.InputAnswersFragment;
import com.example.mniez.myapplication.StudentModule.Fragments.NumberFragment;
import com.example.mniez.myapplication.StudentModule.Fragments.ProgressFragment;
import com.example.mniez.myapplication.StudentModule.Fragments.QuestionFragment;
import com.example.mniez.myapplication.StudentModule.Fragments.SoundAnswersFragment;

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

public class TestActivity extends AppCompatActivity implements AnswersFragment.OnAnswerSelectedListener, ImageAnswersFragment.OnAnswerSelectedListener, SoundAnswersFragment.OnAnswerSelectedListener, InputAnswersFragment.OnAnswerSelectedListener {

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
    int[] answerIds;
    MobileDatabaseReader dbReader;
    AnswersFragment mAnswersFragment;
    NumberFragment mNumberFragment;
    ProgressFragment mProgressFragment;
    QuestionFragment mQuestionFragment;
    ImageAnswersFragment mImageAnswersFragment;
    SoundAnswersFragment mSoundsAnswersFragment;
    InputAnswersFragment mInputAnswersFragment;
    String inputAnswerString;
    String answerStrings[];
    int inputAnswerId;
    public int isCompleted = 0;
    protected int[] currentAnswerIds = new int[4];
    protected String[] answersString = new String[4];
    int[][] answersOrderArray = {{1,2,3,4},{1,2,4,3},{1,3,2,4},{1,3,4,2},{1,4,2,3},{1,4,3,2},{2,1,3,4},{2,1,4,3},{2,3,1,4},{2,3,4,1},
            {2,4,3,1},{2,4,1,3},{3,1,2,4},{3,1,4,2},{3,2,4,1},{3,2,1,4},{3,4,2,1},{3,4,1,2},{4,1,3,2},{4,1,2,3},{4,2,3,1},{4,2,1,3},{4,3,1,2},{4,3,2,1}};
    int[] answersOrder;
    String currentAnswerType;
    private SendTestResultsTask mSendTask = null;
    ProgressDialog progress;
    int score;
    int courseId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setQuestion(-1);
                    return true;
                case R.id.navigation_dashboard:
                    AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
                    builder.setMessage("Czy chcesz zakończyć rozwiązywanie testu?")
                            .setTitle("Zakończenie testu");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            isCompleted = 1;
                            progress = ProgressDialog.show(TestActivity.this, "Proszę czekać",
                                    "Wysyłam wyniki", true);
                            mSendTask = new SendTestResultsTask();
                            mSendTask.execute();
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
        courseId = extras.getInt("course_id", 0);
        setContentView(R.layout.activity_test);
        dbReader = new MobileDatabaseReader(getApplicationContext());
        String testName = dbReader.selectTestName(testId);
        setTitle(testName);
        testQuestions = dbReader.selectAllQuestionsForTest(testId);
        System.out.println(testQuestions.size());
        questionWords = populateCurrentListOfWords(testQuestions.get(0));
        dbReader.close();
        answerIds = new int[testQuestions.size()];
        answersOrder = new int[testQuestions.size()];
        answerStrings = new String[testQuestions.size()];
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
        args5.putInt("answerId", inputAnswerId);
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
        int tAnswersOrder;
        switch (testQuestion.getAnswerTypeId()) {
            case 7:
                for (int i = 0; i < 4; i++) {
                    tAnswerString[i] = questionWords.get(i).getNativeWord();
                }
                break;
            case 8:
                for (int i = 0; i < 4; i++) {
                    tAnswerString[i] = questionWords.get(i).getTranslatedWord();
                }
                break;
            case 10:
                for (int i = 0; i < 4; i++) {
                    tAnswerString[i] = questionWords.get(i).getTranslatedWord();
                }
                break;
            case 12:
                for (int i = 0; i < 4; i++) {
                    tAnswerString[i] = questionWords.get(i).getNativeDefinition();
                }
                break;
            case 13:
                for (int i = 0; i < 4; i++) {
                    tAnswerString[i] = questionWords.get(i).getTranslatedDefinition();
                }
                break;
            default:
                for (int i = 0; i < 4; i++) {
                    tAnswerString[i] = questionWords.get(i).getTranslatedWord();
                }
                break;
        }
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
        inputAnswerId = testQuestion.getAnswerId();
        System.out.println("Poprawna odpowiedź: " + inputAnswerString);
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
                mProgressFragment.setProgress(((((float) questionCounter) + 1) / (float) testQuestions.size()) * 100.0);
                questionWords = populateCurrentListOfWords(testQuestions.get(questionCounter));
                switchCurrentAnswerType(testQuestions.get(questionCounter));
                populateAnswersForQuestion(testQuestions.get(questionCounter));
                populateAnswersForInputQuestion(testQuestions.get(questionCounter));
                mAnswersFragment.initiateAnswers(answersString, testQuestions.get(questionCounter).getAnswerTypeId(), answerIds[questionCounter], currentAnswerIds);
                mImageAnswersFragment.initiateAnswers(answersString, testQuestions.get(questionCounter).getAnswerTypeId(), answerIds[questionCounter], currentAnswerIds);
                mSoundsAnswersFragment.initiateAnswers(answersString, testQuestions.get(questionCounter).getAnswerTypeId(), answerIds[questionCounter], currentAnswerIds);
                String currentString;
                if (answerStrings[questionCounter] == null) {
                    currentString = "";
                } else {
                    currentString = answerStrings[questionCounter];
                }
                mInputAnswersFragment.initiateAnswers(inputAnswerString, testQuestions.get(questionCounter).getAnswerTypeId(), currentString, inputAnswerId);
            } else {
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
                switchCurrentAnswerType(testQuestions.get(questionCounter));
                populateAnswersForQuestion(testQuestions.get(questionCounter));
                populateAnswersForInputQuestion(testQuestions.get(questionCounter));
                mAnswersFragment.initiateAnswers(answersString, testQuestions.get(questionCounter).getAnswerTypeId(), answerIds[questionCounter], currentAnswerIds);
                mImageAnswersFragment.initiateAnswers(answersString, testQuestions.get(questionCounter).getAnswerTypeId(), answerIds[questionCounter], currentAnswerIds);
                mSoundsAnswersFragment.initiateAnswers(answersString, testQuestions.get(questionCounter).getAnswerTypeId(), answerIds[questionCounter], currentAnswerIds);
                String currentString;
                if (answerStrings[questionCounter] == null) {
                    currentString = "";
                }
                else {
                    currentString = answerStrings[questionCounter];
                }
                mInputAnswersFragment.initiateAnswers(inputAnswerString, testQuestions.get(questionCounter).getAnswerTypeId(), currentString, inputAnswerId);
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

    @Override
    public void onAnswerSelected(int answerId, String currentAnswerString) {
        answerIds[questionCounter] = answerId;
        answerStrings[questionCounter] = currentAnswerString;
        System.out.println("Zaznaczona odpowiedź dla pytania " + (questionCounter+1) + ": " + answerId);
    }

    public class SendTestResultsTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
                String answerArray = new String();
                answerArray = answerArray.concat("[");
                for (int i = 0; i < testQuestions.size(); i++) {
                    answerArray = answerArray.concat("{\"questionId\":" + testQuestions.get(i).getId() + ",\"wordId\":" + answerIds[i] + "},");
                }
                answerArray = answerArray.substring(0, answerArray.length() - 1);
                answerArray = answerArray.concat("]");
                System.out.println(answerArray);
                try {
                    URL webpageEndpoint = new URL("http://10.0.2.2:8000/api/updateUserTestScore");
                    HttpURLConnection myConnection = (HttpURLConnection) webpageEndpoint.openConnection();
                    myConnection.setRequestMethod("POST");
                    myConnection.setDoOutput(true);
                    myConnection.setRequestProperty("Accept","*/*");
                    String request;
                    request = "testId=" + testId + "&answers=" + answerArray;
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
                        JSONObject resultsObject = new JSONObject(jsonString);
                        String resultString = resultsObject.getString("score");
                        Integer resultInt = Integer.parseInt(resultString);
                        score = resultInt;
                    }

                    else {
                        return false;
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            return true;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSendTask = null;

            if (success) {
                progress.dismiss();
                int totalPoints = dbReader.calculateTotalPointsForTest(testId);
                AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
                builder.setMessage("Zdobyłeś w tym teście " + score + " na " +  totalPoints + " punktów!")
                        .setTitle("Wyniki").setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbReader.updateScoreForTest(testId, score);
                        Intent intent = new Intent(TestActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                progress.dismiss();
            }
        }

        @Override
        protected void onCancelled() {
            mSendTask = null;
        }

    }
}
