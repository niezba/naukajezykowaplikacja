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
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by mniez on 06.11.2017.
 */

public class InputAnswersFragment extends Fragment {

    OnAnswerSelectedListener mCallback;
    private EditText inputText;
    private TextView correctAnswerView;
    protected int currentAnswerTypeId;
    protected String correctAnswer;
    protected int correctAnswerId;
    protected String setAnswerString = "";
    protected int isCompleted;
    MobileDatabaseReader dbReader;

    public interface OnAnswerSelectedListener {
        public void onAnswerSelected(int answerId, String currentAnswerString);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        correctAnswerId = b.getInt("answerId");
        correctAnswer = b.getString("answerString");
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
        View rootView = inflater.inflate(R.layout.input_answers_fragment, container, false);
        inputText = (EditText) rootView.findViewById(R.id.answerInput);
        correctAnswerView = (TextView) rootView.findViewById(R.id.correctAnswer);
        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (inputText.getText().toString().equals(correctAnswer)) {
                    mCallback.onAnswerSelected(correctAnswerId, inputText.getText().toString());
                    System.out.println("Input answers fragment zaznacza: " + correctAnswerId);
                }
                else {
                    mCallback.onAnswerSelected(-1, inputText.getText().toString());
                    System.out.println("Input answers fragment zaznacza: -1");
                }
            }
        });
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initiateAnswers(correctAnswer, currentAnswerTypeId, "", correctAnswerId, 0);
    }

    public void initiateAnswers(String newAnswerString, int newQuestionType, String newSetAnswerString, int newAnswerId, int isNewCompleted) {
        correctAnswer = newAnswerString;
        currentAnswerTypeId = newQuestionType;
        correctAnswerId = newAnswerId;
        setAnswerString = newSetAnswerString;
        isCompleted = isNewCompleted;
        System.out.println("Typ pytania: " + newQuestionType);
        switch(currentAnswerTypeId) {
            case 16:case 17:
            if (isCompleted == 0) {
                if (setAnswerString.length() != 0) {
                    inputText.setText(setAnswerString);
                } else {
                    inputText.setText("");
                }
            } else {
                correctAnswerView.setVisibility(View.GONE);
                inputText.setEnabled(false);
                if (setAnswerString.length() != 0) {
                    inputText.setText(setAnswerString);
                } else {
                    inputText.setText("");
                }
                if (setAnswerString.equals(correctAnswer)) {
                    inputText.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                    inputText.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
                } else {
                    inputText.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    inputText.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.colorRed));
                    correctAnswerView.setVisibility(View.VISIBLE);
                    correctAnswerView.setText("Poprawna odpowiedź: " + correctAnswer);
                }
            }
            break;
        }
    }

}
