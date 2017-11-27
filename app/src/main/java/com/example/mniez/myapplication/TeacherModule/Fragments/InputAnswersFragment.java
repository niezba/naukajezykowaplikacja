package com.example.mniez.myapplication.TeacherModule.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.R;

/**
 * Created by mniez on 06.11.2017.
 */

public class InputAnswersFragment extends Fragment {

    private EditText inputText;
    private TextView correctAnswerView;
    protected int currentAnswerTypeId;
    protected String correctAnswer;
    protected int correctAnswerId;
    protected String setAnswerString = "";
    protected int isCompleted;
    MobileDatabaseReader dbReader;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        correctAnswerId = b.getInt("answerId");
        correctAnswer = b.getString("answerString");
        currentAnswerTypeId = b.getInt("answerType");

    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onDetach() {
        super.onDetach();
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

            }
        });
        inputText.setEnabled(false);
        inputText.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
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
        System.out.println("Odpowiedź do pytania typu input: " + correctAnswer);
        if(isCompleted == 0) {
            inputText.setText(correctAnswer);
        }
        else {
            correctAnswerView.setVisibility(View.GONE);
            inputText.setText(correctAnswer);
        }
    }

}
