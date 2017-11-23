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
import android.support.v4.content.ContextCompat;
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

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
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
    int isCompleted;
    int correctAnswer;
    MobileDatabaseReader dbReader;
    ArrayList<Word> wordList;
    int isOffline;

    public interface OnAnswerSelectedListener {
        public void onAnswerSelected(int answerId);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        answerIds = b.getIntArray("answerIds");
        answerString = b.getStringArray("answerString");
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
                ans1.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                //ans2.setChecked(false);
                ans2.setTypeface(null, Typeface.NORMAL);
                ans2.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                //ans3.setChecked(false);
                ans3.setTypeface(null, Typeface.NORMAL);
                ans3.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                //ans4.setChecked(false);
                ans4.setTypeface(null, Typeface.NORMAL);
                ans4.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                mCallback.onAnswerSelected(answerIds[0]);
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
        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ans1.setChecked(false);
                ans1.setTypeface(null, Typeface.NORMAL);
                ans1.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                //ans2.setChecked(true);
                ans2.setTypeface(null, Typeface.BOLD);
                ans2.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                //ans3.setChecked(false);
                ans3.setTypeface(null, Typeface.NORMAL);
                ans3.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                //ans4.setChecked(false);
                ans4.setTypeface(null, Typeface.NORMAL);
                ans4.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                mCallback.onAnswerSelected(answerIds[1]);
                try {
                    resetTexts();
                    ans2.setText("...");
                    mediaPlayer.reset();
                    if(isOffline == 0) {
                        mediaPlayer.setDataSource("http://pzmmd.cba.pl/media/sounds/" + wordList.get(1).getTranslatedSound());
                    }
                    else {
                        if (wordList.get(1).getIsTranslatedSoundLocal() == 1) {
                            System.out.println("Playing local 1");
                            File soundDir = new File(SoundAnswersFragment.this.getActivity().getFilesDir() + "/Sounds");
                            File sound = new File(soundDir, wordList.get(1).getTranslatedSoundLocal());
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
        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ans1.setChecked(false);
                ans1.setTypeface(null, Typeface.NORMAL);
                ans1.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                //ans2.setChecked(false);
                ans2.setTypeface(null, Typeface.NORMAL);
                ans2.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                //ans3.setChecked(true);
                ans3.setTypeface(null, Typeface.BOLD);
                ans3.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                //ans4.setChecked(false);
                ans4.setTypeface(null, Typeface.NORMAL);
                ans4.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                mCallback.onAnswerSelected(answerIds[2]);
                try {
                    resetTexts();
                    ans3.setText("...");
                    mediaPlayer.reset();
                    if(isOffline == 0) {
                        mediaPlayer.setDataSource("http://pzmmd.cba.pl/media/sounds/" + wordList.get(2).getTranslatedSound());
                    }
                    else {
                        if (wordList.get(2).getIsTranslatedSoundLocal() == 1) {
                            System.out.println("Playing local 1");
                            File soundDir = new File(SoundAnswersFragment.this.getActivity().getFilesDir() + "/Sounds");
                            File sound = new File(soundDir, wordList.get(2).getTranslatedSoundLocal());
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
        ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ans1.setChecked(false);
                ans1.setTypeface(null, Typeface.NORMAL);
                ans1.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                //ans2.setChecked(false);
                ans2.setTypeface(null, Typeface.NORMAL);
                ans2.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                //ans3.setChecked(false);
                ans3.setTypeface(null, Typeface.NORMAL);
                ans3.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                //ans4.setChecked(true);
                ans4.setTypeface(null, Typeface.BOLD);
                ans4.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                mCallback.onAnswerSelected(answerIds[3]);
                try {
                    resetTexts();
                    ans4.setText("...");
                    mediaPlayer.reset();
                    if(isOffline == 0) {
                        mediaPlayer.setDataSource("http://pzmmd.cba.pl/media/sounds/" + wordList.get(3).getTranslatedSound());
                    }
                    else {
                        if (wordList.get(3).getIsTranslatedSoundLocal() == 1) {
                            System.out.println("Playing local 1");
                            File soundDir = new File(SoundAnswersFragment.this.getActivity().getFilesDir() + "/Sounds");
                            File sound = new File(soundDir, wordList.get(3).getTranslatedSoundLocal());
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
        ans2.setText("Dźwięk 2");
        ans3.setText("Dźwięk 3");
        ans4.setText("Dźwięk 4");
    }

    public void initiateAnswers(String[] newAnswerString, int newQuestionType, int newSetAnswerId, int[] newAnswerIds, int newIsCompleted, int currentCorrectAnswer) {
        answerString = newAnswerString;
        currentAnswerTypeId = newQuestionType;
        answerIds = newAnswerIds;
        setAnswerId = newSetAnswerId;
        isCompleted = newIsCompleted;
        correctAnswer = currentCorrectAnswer;
        System.out.println("Typ pytania: " + newQuestionType);
        switch(currentAnswerTypeId) {
            case 9:
                if(isCompleted == 0) {
                    dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                    wordList = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        wordList.add(dbReader.getParticularWordData(answerIds[i]));
                    }
                    if (answerIds[0] == newSetAnswerId) {
                        ans1.performClick();
                    } else if (answerIds[1] == newSetAnswerId) {
                        ans2.performClick();
                    } else if (answerIds[2] == newSetAnswerId) {
                        ans3.performClick();
                    } else if (answerIds[3] == newSetAnswerId) {
                        ans4.performClick();
                    } else {
                        //ans1.setChecked(false);
                        ans1.setTypeface(null, Typeface.NORMAL);
                        ans1.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                        //ans2.setChecked(false);
                        ans2.setTypeface(null, Typeface.NORMAL);
                        ans2.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                        //ans3.setChecked(false);
                        ans3.setTypeface(null, Typeface.NORMAL);
                        ans3.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                        //ans4.setChecked(false);
                        ans4.setTypeface(null, Typeface.NORMAL);
                        ans4.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                    }
                }
                else {
                    dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                    wordList = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        wordList.add(dbReader.getParticularWordData(answerIds[i]));
                    }
                    if (answerIds[0] == newSetAnswerId) {
                        ans1.performClick();
                        ans1.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                    } else if (answerIds[1] == newSetAnswerId) {
                        ans2.performClick();
                        ans2.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                    } else if (answerIds[2] == newSetAnswerId) {
                        ans3.performClick();
                        ans3.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                    } else if (answerIds[3] == newSetAnswerId) {
                        ans4.performClick();
                        ans4.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                    } else {
                        ans1.setClickable(false);
                        ans1.setTypeface(null, Typeface.NORMAL);
                        ans1.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                        ans2.setEnabled(false);
                        ans2.setTypeface(null, Typeface.NORMAL);
                        ans2.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                        ans3.setEnabled(false);
                        ans3.setTypeface(null, Typeface.NORMAL);
                        ans3.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                        ans4.setEnabled(false);
                        ans4.setTypeface(null, Typeface.NORMAL);
                        ans4.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.cardview_light_background));
                    }
                    ans1.setClickable(false);
                    ans2.setClickable(false);
                    ans3.setClickable(false);
                    ans4.setClickable(false);
                    if (answerIds[0] == correctAnswer) {
                        ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                    } else if (answerIds[0] == newSetAnswerId && (answerIds[0] > correctAnswer || answerIds[0] < correctAnswer)) {
                        ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    } else if (answerIds[0] > newSetAnswerId || answerIds[0] < newSetAnswerId) {
                        ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                    }
                    if (answerIds[1] == correctAnswer) {
                        ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                    } else if (answerIds[1] == newSetAnswerId && (answerIds[1] > correctAnswer || answerIds[1] < correctAnswer)) {
                        ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    } else if (answerIds[1] > newSetAnswerId || answerIds[1] < newSetAnswerId) {
                        ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                    }
                    if (answerIds[2] == correctAnswer) {
                        ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                    } else if (answerIds[2] == newSetAnswerId && (answerIds[2] > correctAnswer || answerIds[2] < correctAnswer)) {
                        ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    } else if (answerIds[2] > newSetAnswerId || answerIds[2] < newSetAnswerId) {
                        ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                    }
                    if (answerIds[3] == correctAnswer) {
                        ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                    } else if (answerIds[3] == newSetAnswerId && (answerIds[3] > correctAnswer || answerIds[3] < correctAnswer)) {
                        ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    } else if (answerIds[3] > newSetAnswerId || answerIds[3] < newSetAnswerId) {
                        ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                    }
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
