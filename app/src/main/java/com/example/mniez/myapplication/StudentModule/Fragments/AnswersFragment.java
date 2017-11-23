package com.example.mniez.myapplication.StudentModule.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by mniez on 06.11.2017.
 */

public class AnswersFragment extends Fragment {

    OnAnswerSelectedListener mCallback;
    private Button ans1, ans2, ans3, ans4;
    protected int[] answerIds;
    protected int currentAnswerTypeId;
    protected String[] answerString;
    int setAnswerId;
    int isCompleted;
    int correctAnswer;
    MobileDatabaseReader dbReader;

    public interface OnAnswerSelectedListener {
        public void onAnswerSelected(int answerId);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        answerIds = b.getIntArray("answerIds");
        answerString = b.getStringArray("answerString");
        currentAnswerTypeId = b.getInt("answerType");
        for(int i = 0; i< 4; i++) {
            System.out.println(answerIds[i]);
        }

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
        View rootView = inflater.inflate(R.layout.answers_fragment, container, false);
        ans1 = (Button) rootView.findViewById(R.id.answerOne);
        ans2 = (Button) rootView.findViewById(R.id.answerTwo);
        ans3 = (Button) rootView.findViewById(R.id.answerThree);
        ans4 = (Button) rootView.findViewById(R.id.answerFour);
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
        System.out.println("Odpowiedź do zaznaczenia: " + newSetAnswerId);
        System.out.println("Typ Odpowiedzi: " + newQuestionType);
        switch(currentAnswerTypeId) {
            case 10:case 7:
                ans1.setText(answerString[0]);
                ans2.setText(answerString[1]);
                ans3.setText(answerString[2]);
                ans4.setText(answerString[3]);
                if(isCompleted == 0) {
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
                    if(answerIds[0] == correctAnswer) {
                        ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                    }
                    else if(answerIds[0] == newSetAnswerId && (answerIds[0] > correctAnswer || answerIds[0] < correctAnswer)) {
                        ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    }
                    else if(answerIds[0] > newSetAnswerId || answerIds[0] < newSetAnswerId) {
                        ans1.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                    }
                    if(answerIds[1] == correctAnswer) {
                        ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                    }
                    else if(answerIds[1] == newSetAnswerId && (answerIds[1] > correctAnswer || answerIds[1] < correctAnswer)) {
                            ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    }
                    else if(answerIds[1] > newSetAnswerId || answerIds[1] < newSetAnswerId) {
                        ans2.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                    }
                    if(answerIds[2] == correctAnswer) {
                        ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                    }
                    else if(answerIds[2] == newSetAnswerId && (answerIds[2] > correctAnswer || answerIds[2] < correctAnswer)) {
                        ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    }
                    else if(answerIds[2] > newSetAnswerId || answerIds[2] < newSetAnswerId) {
                        ans3.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                    }
                    if(answerIds[3] == correctAnswer) {
                        ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                    }
                    else if(answerIds[3] == newSetAnswerId && (answerIds[3] > correctAnswer || answerIds[3] < correctAnswer)) {
                        ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    }
                    else if(answerIds[3] > newSetAnswerId || answerIds[3] < newSetAnswerId) {
                        ans4.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
                    }
                }
                break;
            case 8:
                break;
            default:
                ans1.setText(answerString[0]);
                ans2.setText(answerString[1]);
                ans3.setText(answerString[2]);
                ans4.setText(answerString[3]);
                //ans1.setChecked(false);
                //ans2.setChecked(false);
                //ans3.setChecked(false);
                //ans4.setChecked(false);
                break;
        }
    }

}
