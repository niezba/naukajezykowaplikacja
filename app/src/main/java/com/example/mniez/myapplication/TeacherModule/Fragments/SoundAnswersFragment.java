package com.example.mniez.myapplication.TeacherModule.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mniez on 06.11.2017.
 */

public class SoundAnswersFragment extends Fragment {

    private Button ans1, ans2, ans3, ans4;
    MediaPlayer mediaPlayer = new MediaPlayer();
    protected int answerIds;
    protected int currentAnswerTypeId;
    protected String answerString;
    int setAnswerId;
    int isCompleted;
    int correctAnswer;
    MobileDatabaseReader dbReader;
    ArrayList<Word> wordList;
    int isOffline;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        answerIds = b.getInt("answerIds");
        answerString = b.getString("answerString");
        currentAnswerTypeId = b.getInt("answerType");
        isOffline = b.getInt("isOffline", 0);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                resetTexts();
            }
        });
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onDetach() {
        super.onDetach();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sound_answers_fragment_teacher, container, false);
        ans1 = (Button) rootView.findViewById(R.id.answerOneSnd);
        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ans1.setChecked(true);
                ans1.setTypeface(null, Typeface.BOLD);
                ans1.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                //ans2.setChecked(false);
                try {
                    resetTexts();
                    ans1.setText("...");
                    mediaPlayer.reset();
                    if (isOffline == 0) {
                        mediaPlayer.setDataSource("http://pzmmd.cba.pl/media/sounds/" + wordList.get(0).getTranslatedSound());
                    }
                    else {
                        if (wordList.get(0).getIsTranslatedSoundLocal() == 1) {
                            System.out.println("Playing local 1");
                            File soundDir = new File(SoundAnswersFragment.this.getActivity().getFilesDir() + "/Sounds");
                            File sound = new File(soundDir, wordList.get(0).getTranslatedSoundLocal());
                            FileInputStream inputStream = new FileInputStream(sound);
                            mediaPlayer.setDataSource(inputStream.getFD());
                            inputStream.close();
                        }
                    }
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initiateAnswers(answerString, currentAnswerTypeId, 0, answerIds, 0, 0);
    }

    public void resetTexts() {
        ans1.setText("Dźwięk 1");
    }

    public void initiateAnswers(String newAnswerString, int newQuestionType, int newSetAnswerId, int newAnswerIds, int newIsCompleted, int currentCorrectAnswer) {
        answerString = newAnswerString;
        currentAnswerTypeId = newQuestionType;
        answerIds = newAnswerIds;
        setAnswerId = newSetAnswerId;
        isCompleted = newIsCompleted;
        correctAnswer = currentCorrectAnswer;
        System.out.println("Typ pytania: " + newQuestionType);
        switch(currentAnswerTypeId) {
            case 9:
                    dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                    wordList = new ArrayList<>();
                        wordList.add(dbReader.getParticularWordData(answerIds));
                break;
            default:
                dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                wordList = new ArrayList<>();
                    wordList.add(dbReader.getParticularWordData(answerIds));
                break;
        }
    }

}
