package com.example.mniez.myapplication.TeacherModule.ActivityAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mniez.myapplication.ObjectHelper.UsersExam;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.TeacherModule.StudentActivity;

import java.util.ArrayList;

/**
 * Created by mniez on 16.10.2017.
 */

public class UserGradesAdapter extends RecyclerView.Adapter{

    private ArrayList<UsersExam> mGrades;
    private Context mKontekst;
    private RecyclerView mRecyclerView;

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView cv;
        public TextView studentCourseName;
        public TextView studentLessonName;
        public TextView grade;

        public MyViewHolder(View pItem) {
            super(pItem);
            studentCourseName = (TextView) pItem.findViewById(R.id.studentCourseName);
            studentLessonName = (TextView) pItem.findViewById(R.id.studentLessonName);
            grade = (TextView) pItem.findViewById(R.id.examStudentGrade);
            cv = (CardView) pItem.findViewById(R.id.gradesCardView);
        }
    }

    public UserGradesAdapter(ArrayList<UsersExam> pGrades, Context context, RecyclerView pRecyclerView) {
        mGrades = pGrades;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_grades_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final UsersExam usersExam = mGrades.get(i);
        ((MyViewHolder) viewHolder).studentCourseName.setText(usersExam.getCourseName());
        ((MyViewHolder) viewHolder).studentLessonName.setText(usersExam.getLessonName());
        String textToSet = new String();
        if (usersExam.getGrade() == 0) {
            textToSet = "Brak podejścia";
        }
        else {
            textToSet = usersExam.getGrade().toString();
        }
        ((MyViewHolder) viewHolder).grade.setText("OCENA: " + textToSet);
    }

    public int getItemCount() {
        return mGrades.size();
    }
}
