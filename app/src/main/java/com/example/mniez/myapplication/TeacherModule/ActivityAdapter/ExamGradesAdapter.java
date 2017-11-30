package com.example.mniez.myapplication.TeacherModule.ActivityAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.ObjectHelper.UsersExam;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.TeacherModule.CourseDetailsActivity;
import com.example.mniez.myapplication.TeacherModule.CourseElementsActivity;
import com.example.mniez.myapplication.TeacherModule.StudentActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mniez on 16.10.2017.
 */

public class ExamGradesAdapter extends RecyclerView.Adapter{

    private ArrayList<UsersExam> mGrades;
    private Context mKontekst;
    private RecyclerView mRecyclerView;

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView cv;
        public TextView studentName;
        public TextView grade;
        public Button detailButton;

        public MyViewHolder(View pItem) {
            super(pItem);
            studentName = (TextView) pItem.findViewById(R.id.examStudentName);
            grade = (TextView) pItem.findViewById(R.id.examStudentGrade);
            cv = (CardView) pItem.findViewById(R.id.gradesCardView);
            detailButton = (Button) pItem.findViewById(R.id.userDetailButton);
        }
    }

    public ExamGradesAdapter(ArrayList<UsersExam> pGrades, Context context, RecyclerView pRecyclerView) {
        mGrades = pGrades;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exam_grades_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final UsersExam usersExam = mGrades.get(i);
        ((MyViewHolder) viewHolder).studentName.setText(usersExam.getUserName() + " " + usersExam.getUserSurname());
        ((MyViewHolder) viewHolder).grade.setText("OCENA: " + usersExam.getGrade().toString());
        final int studentId = usersExam.getUserId();
        ((MyViewHolder) viewHolder).detailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StudentActivity.class);
                intent.putExtra("user_id", studentId);
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return mGrades.size();
    }
}
