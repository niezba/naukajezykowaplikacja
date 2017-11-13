package com.example.mniez.myapplication.StudentModule.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
                //ans2.setChecked(false);
                ans2.setTypeface(null, Typeface.NORMAL);
                //ans3.setChecked(false);
                ans3.setTypeface(null, Typeface.NORMAL);
                //ans4.setChecked(false);
                ans4.setTypeface(null, Typeface.NORMAL);
                mCallback.onAnswerSelected(answerIds[0]);
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
            }
        });
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initiateAnswers(answerString, currentAnswerTypeId, 0, answerIds);
    }

    public void initiateAnswers(String[] newAnswerString, int newQuestionType, int newSetAnswerId, int[] newAnswerIds) {
        answerString = newAnswerString;
        currentAnswerTypeId = newQuestionType;
        answerIds = newAnswerIds;
        setAnswerId = newSetAnswerId;
        System.out.println("Typ pytania: " + newQuestionType);
        switch(currentAnswerTypeId) {
            case 10:
                ans1.setText(answerString[0]);
                ans2.setText(answerString[1]);
                ans3.setText(answerString[2]);
                ans4.setText(answerString[3]);
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
