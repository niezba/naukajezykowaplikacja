package com.example.mniez.myapplication.StudentModule.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;

/**
 * Created by mniez on 06.11.2017.
 */

public class ImageAnswersFragment extends Fragment {

    OnAnswerSelectedListener mCallback;
    private ImageButton ans1, ans2, ans3, ans4;
    protected int[] answerIds;
    protected int currentAnswerTypeId;
    protected String[] answerString;
    int setAnswerId;
    MobileDatabaseReader dbReader;
    ArrayList<Word> wordList;
    Integer isOffline;
    int isCompleted;
    int correctAnswer;

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
        View rootView = inflater.inflate(R.layout.image_answers_fragment, container, false);
        ans1 = (ImageButton) rootView.findViewById(R.id.answerOneImg);
        ans2 = (ImageButton) rootView.findViewById(R.id.answerTwoImg);
        ans3 = (ImageButton) rootView.findViewById(R.id.answerThreeImg);
        ans4 = (ImageButton) rootView.findViewById(R.id.answerFourImg);
        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                mCallback.onAnswerSelected(answerIds[0]);
            }
        });
        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                mCallback.onAnswerSelected(answerIds[1]);
            }
        });
        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                mCallback.onAnswerSelected(answerIds[2]);
            }
        });
        ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                mCallback.onAnswerSelected(answerIds[3]);
            }
        });
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initiateAnswers(answerString, currentAnswerTypeId, 0, answerIds, 0, 0);
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
            case 8:
                if(isCompleted == 0) {
                    dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                    wordList = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        wordList.add(dbReader.getParticularWordData(answerIds[i]));
                    }
                    if (isOffline == 0) {
                        Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(0).getPicture()).fit().into(ans1);
                        System.out.println(wordList.get(0).getPicture());
                        Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(1).getPicture()).fit().into(ans2);
                        System.out.println(wordList.get(1).getPicture());
                        Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(2).getPicture()).fit().into(ans3);
                        System.out.println(wordList.get(2).getPicture());
                        Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(3).getPicture()).fit().into(ans4);
                        System.out.println(wordList.get(3).getPicture());
                    } else {
                        File avatar = new File(ImageAnswersFragment.this.getActivity().getFilesDir() + "/Pictures");
                        if (wordList.get(0).getIsPictureLocal() == 1) {
                            File avatar1Local = new File(avatar, wordList.get(0).getPictureLocal());
                            Picasso.with(ImageAnswersFragment.this.getActivity()).load(avatar1Local).fit().centerCrop().into(ans1);
                        }
                        if (wordList.get(1).getIsPictureLocal() == 1) {
                            File avatar2Local = new File(avatar, wordList.get(1).getPictureLocal());
                            Picasso.with(ImageAnswersFragment.this.getActivity()).load(avatar2Local).fit().centerCrop().into(ans2);
                        }
                        if (wordList.get(2).getIsPictureLocal() == 1) {
                            File avatar3Local = new File(avatar, wordList.get(2).getPictureLocal());
                            Picasso.with(ImageAnswersFragment.this.getActivity()).load(avatar3Local).fit().centerCrop().into(ans3);
                        }
                        if (wordList.get(3).getIsPictureLocal() == 1) {
                            File avatar4Local = new File(avatar, wordList.get(3).getPictureLocal());
                            Picasso.with(ImageAnswersFragment.this.getActivity()).load(avatar4Local).fit().centerCrop().into(ans4);
                        }
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
                        ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                        ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                        ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                        ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                    }
                }
                else {
                    dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                    wordList = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        wordList.add(dbReader.getParticularWordData(answerIds[i]));
                    }
                    if (isOffline == 0) {
                        Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(0).getPicture()).fit().into(ans1);
                        System.out.println(wordList.get(0).getPicture());
                        Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(1).getPicture()).fit().into(ans2);
                        System.out.println(wordList.get(1).getPicture());
                        Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(2).getPicture()).fit().into(ans3);
                        System.out.println(wordList.get(2).getPicture());
                        Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(3).getPicture()).fit().into(ans4);
                        System.out.println(wordList.get(3).getPicture());
                    } else {
                        File avatar = new File(ImageAnswersFragment.this.getActivity().getFilesDir() + "/Pictures");
                        if (wordList.get(0).getIsPictureLocal() == 1) {
                            File avatar1Local = new File(avatar, wordList.get(0).getPictureLocal());
                            Picasso.with(ImageAnswersFragment.this.getActivity()).load(avatar1Local).fit().centerCrop().into(ans1);
                        }
                        if (wordList.get(1).getIsPictureLocal() == 1) {
                            File avatar2Local = new File(avatar, wordList.get(1).getPictureLocal());
                            Picasso.with(ImageAnswersFragment.this.getActivity()).load(avatar2Local).fit().centerCrop().into(ans2);
                        }
                        if (wordList.get(2).getIsPictureLocal() == 1) {
                            File avatar3Local = new File(avatar, wordList.get(2).getPictureLocal());
                            Picasso.with(ImageAnswersFragment.this.getActivity()).load(avatar3Local).fit().centerCrop().into(ans3);
                        }
                        if (wordList.get(3).getIsPictureLocal() == 1) {
                            File avatar4Local = new File(avatar, wordList.get(3).getPictureLocal());
                            Picasso.with(ImageAnswersFragment.this.getActivity()).load(avatar4Local).fit().centerCrop().into(ans4);
                        }
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
                        ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                        ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                        ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                        ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
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
                        ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                    }
                    if (answerIds[1] == correctAnswer) {
                        ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                    } else if (answerIds[1] == newSetAnswerId && (answerIds[1] > correctAnswer || answerIds[1] < correctAnswer)) {
                        ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    } else if (answerIds[1] > newSetAnswerId || answerIds[1] < newSetAnswerId) {
                        ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                    }
                    if (answerIds[2] == correctAnswer) {
                        ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                    } else if (answerIds[2] == newSetAnswerId && (answerIds[2] > correctAnswer || answerIds[2] < correctAnswer)) {
                        ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    } else if (answerIds[2] > newSetAnswerId || answerIds[2] < newSetAnswerId) {
                        ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                    }
                    if (answerIds[3] == correctAnswer) {
                        ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                    } else if (answerIds[3] == newSetAnswerId && (answerIds[3] > correctAnswer || answerIds[3] < correctAnswer)) {
                        ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    } else if (answerIds[3] > newSetAnswerId || answerIds[3] < newSetAnswerId) {
                        ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                    }
                }
                break;
            default:
                dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                wordList = new ArrayList<>();
                for(int i = 0; i<4; i++) {
                    wordList.add(dbReader.getParticularWordData(answerIds[i]));
                }
                //Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(0).getPicture()).fit().into(ans1);
                //Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(1).getPicture()).fit().into(ans2);
                //Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(2).getPicture()).fit().into(ans3);
                //Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(3).getPicture()).fit().into(ans4);
                ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.lightGray));
                break;
        }
    }

}
