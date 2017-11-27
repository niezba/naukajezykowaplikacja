package com.example.mniez.myapplication.TeacherModule.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.R;

/**
 * Created by mniez on 06.11.2017.
 */

public class AnswersFragment extends Fragment {

    private Button ans1;
    protected int answerIds;
    protected int currentAnswerTypeId;
    protected String answerString;
    int setAnswerId;
    int isCompleted;
    int correctAnswer;
    MobileDatabaseReader dbReader;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        answerIds = b.getInt("answerIds");
        answerString = b.getString("answerString");
        currentAnswerTypeId = b.getInt("answerType");
            System.out.println(answerIds);

    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onDetach() {
        super.onDetach();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.answers_fragment_teacher, container, false);
        ans1 = (Button) rootView.findViewById(R.id.answerOne);
        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initiateAnswers(answerString, currentAnswerTypeId, 0, answerIds, 0, 0);
    }

    public void initiateAnswers(String newAnswerString, int newQuestionType, int newSetAnswerId, int newAnswerIds, int newIsCompleted, int currentCorrectAnswer) {
        answerString = newAnswerString;
        currentAnswerTypeId = newQuestionType;
        answerIds = newAnswerIds;
        setAnswerId = newSetAnswerId;
        isCompleted = newIsCompleted;
        correctAnswer = currentCorrectAnswer;
        switch(currentAnswerTypeId) {
            case 10:case 7:
                ans1.setText(answerString);
                break;
            case 8:
                break;
            default:
                ans1.setText(answerString);
                break;
        }
    }

}
