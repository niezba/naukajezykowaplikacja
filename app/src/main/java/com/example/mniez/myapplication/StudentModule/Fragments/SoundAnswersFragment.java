package com.example.mniez.myapplication.StudentModule.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mniez on 06.11.2017.
 */

public class SoundAnswersFragment extends Fragment {

    OnAnswerSelectedListener mCallback;
    private Button ans1, ans2, ans3, ans4;
    MediaPlayer mediaPlayer = new MediaPlayer();
    protected int[] answerIds;
    protected int currentAnswerTypeId;
    protected String[] answerString;
    int setAnswerId;
    MobileDatabaseReader dbReader;
    ArrayList<Word> wordList;

    public interface OnAnswerSelectedListener {
        public void onAnswerSelected(int answerId);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        answerIds = b.getIntArray("answerIds");
        answerString = b.getStringArray("answerString");
        currentAnswerTypeId = b.getInt("answerType");
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
        if (context instanceof OnAnswerSelectedListener) {
            mCallback = (OnAnswerSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAnswerSelectedListener" );
        }
    }

    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sound_answers_fragment, container, false);
        ans1 = (Button) rootView.findViewById(R.id.answerOneSnd);
        ans2 = (Button) rootView.findViewById(R.id.answerTwoSnd);
        ans3 = (Button) rootView.findViewById(R.id.answerThreeSnd);
        ans4 = (Button) rootView.findViewById(R.id.answerFourSnd);
        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ans1.setChecked(true);
                ans1.setTypeface(null, Typeface.BOLD);
                //ans2.setChecked(false);
                ans2.setTypeface(null, Typeface.NORMAL);
                //ans3.setChecked(false);
                ans3.setTypeface(null, Typeface.NORMAL);
                //ans4.setChecked(false);
                ans4.setTypeface(null, Typeface.NORMAL);
                mCallback.onAnswerSelected(answerIds[0]);
                try {
                    resetTexts();
                    ans1.setText("...");
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource("http://10.0.2.2:8000/media/sounds/" + wordList.get(0).getTranslatedSound());
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ans1.setChecked(false);
                ans1.setTypeface(null, Typeface.NORMAL);
                //ans2.setChecked(true);
                ans2.setTypeface(null, Typeface.BOLD);
                //ans3.setChecked(false);
                ans3.setTypeface(null, Typeface.NORMAL);
                //ans4.setChecked(false);
                ans4.setTypeface(null, Typeface.NORMAL);
                mCallback.onAnswerSelected(answerIds[1]);
                try {
                    resetTexts();
                    ans2.setText("...");
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource("http://10.0.2.2:8000/media/sounds/" + wordList.get(1).getTranslatedSound());
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ans1.setChecked(false);
                ans1.setTypeface(null, Typeface.NORMAL);
                //ans2.setChecked(false);
                ans2.setTypeface(null, Typeface.NORMAL);
                //ans3.setChecked(true);
                ans3.setTypeface(null, Typeface.BOLD);
                //ans4.setChecked(false);
                ans4.setTypeface(null, Typeface.NORMAL);
                mCallback.onAnswerSelected(answerIds[2]);
                try {
                    resetTexts();
                    ans3.setText("...");
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource("http://10.0.2.2:8000/media/sounds/" + wordList.get(2).getTranslatedSound());
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ans1.setChecked(false);
                ans1.setTypeface(null, Typeface.NORMAL);
                //ans2.setChecked(false);
                ans2.setTypeface(null, Typeface.NORMAL);
                //ans3.setChecked(false);
                ans3.setTypeface(null, Typeface.NORMAL);
                //ans4.setChecked(true);
                ans4.setTypeface(null, Typeface.BOLD);
                mCallback.onAnswerSelected(answerIds[3]);
                try {
                    resetTexts();
                    ans4.setText("...");
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource("http://10.0.2.2:8000/media/sounds/" + wordList.get(3).getTranslatedSound());
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
        initiateAnswers(answerString, currentAnswerTypeId, 0, answerIds);
    }

    public void resetTexts() {
        ans1.setText("Dźwięk 1");
        ans2.setText("Dźwięk 2");
        ans3.setText("Dźwięk 3");
        ans4.setText("Dźwięk 4");
    }

    public void initiateAnswers(String[] newAnswerString, int newQuestionType, int newSetAnswerId, int[] newAnswerIds) {
        answerString = newAnswerString;
        currentAnswerTypeId = newQuestionType;
        answerIds = newAnswerIds;
        setAnswerId = newSetAnswerId;
        System.out.println("Typ pytania: " + newQuestionType);
        switch(currentAnswerTypeId) {
            case 9:
                dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                wordList = new ArrayList<>();
                for(int i = 0; i<4; i++) {
                    wordList.add(dbReader.getParticularWordData(answerIds[i]));
                }
                if (answerIds[0] == newSetAnswerId) {
                    ans1.performClick();
                }
                else if (answerIds[1] == newSetAnswerId) {
                    ans2.performClick();
                }
                else if (answerIds[2] == newSetAnswerId) {
                    ans3.performClick();
                }
                else if (answerIds[3] == newSetAnswerId) {
                    ans4.performClick();
                }
                else {
                    //ans1.setChecked(false);
                    ans1.setTypeface(null, Typeface.NORMAL);
                    //ans2.setChecked(false);
                    ans2.setTypeface(null, Typeface.NORMAL);
                    //ans3.setChecked(false);
                    ans3.setTypeface(null, Typeface.NORMAL);
                    //ans4.setChecked(false);
                    ans4.setTypeface(null, Typeface.NORMAL);
                }
                break;
            default:
                dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                wordList = new ArrayList<>();
                for(int i = 0; i<4; i++) {
                    wordList.add(dbReader.getParticularWordData(answerIds[i]));
                }
                //ans1.setChecked(false);
                ans1.setTypeface(null, Typeface.NORMAL);
                //ans2.setChecked(false);
                ans2.setTypeface(null, Typeface.NORMAL);
                //ans3.setChecked(false);
                ans3.setTypeface(null, Typeface.NORMAL);
                //ans4.setChecked(false);
                ans4.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }

}