package com.example.mniez.myapplication.TeacherModule.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mniez.myapplication.R;

/**
 * Created by mniez on 06.11.2017.
 */

public class NumberFragment extends Fragment {

    TextView numberView;
    int startQuestionNumber;
    int questionCount;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getArguments();
        questionCount = b.getInt("question_sum");
        startQuestionNumber = b.getInt("question_num");
        View rootView = inflater.inflate(R.layout.number_fragment, container, false);
        numberView = (TextView) rootView.findViewById(R.id.questionNumber);
        numberView.setText("Pytanie " + startQuestionNumber + " z " + questionCount);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setNumberOfQuestion(int questionNumber) {
        startQuestionNumber = questionNumber;
        numberView.setText("Pytanie " + startQuestionNumber + " z " + questionCount);
    }
}
