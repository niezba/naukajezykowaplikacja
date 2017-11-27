package com.example.mniez.myapplication.StudentModule.ActivityAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.ObjectHelper.Exam;
import com.example.mniez.myapplication.ObjectHelper.Lesson;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.Others.GradeCoursesViewHolder;
import com.example.mniez.myapplication.StudentModule.Others.GradeElementsViewHolder;
import com.example.mniez.myapplication.StudentModule.Others.GradesViewHolder;

import java.util.ArrayList;

/**
 * Created by mniez on 19.11.2017.
 */

public class GradesListAdapter extends RecyclerView.Adapter {

    private ArrayList<Object> mScoredElements;
    private Context mKontekst;
    private RecyclerView mRecyclerView;

    private final int TEST = 1, COURSE = 3, LESSON = 4;

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView gradeText;
        public TextView gradeResult;
        public CardView cv;
        public ListView lv;

        public MyViewHolder(View pItem) {
            super(pItem);
            gradeText = (TextView) pItem.findViewById(R.id.gradeText);
            gradeResult = (TextView) pItem.findViewById(R.id.gradeResult);
            cv = (CardView) pItem.findViewById(R.id.gradeCardView);
        }
    }

    public int getItemViewType(int position) {
        if (mScoredElements.get(position) instanceof Course) {
            return COURSE;
        } else if (mScoredElements.get(position) instanceof Lesson) {
            return LESSON;
        } else if (mScoredElements.get(position) instanceof Exam) {
            return TEST;
        }
        return -1;
    }

    public GradesListAdapter(ArrayList<Object> pScoredElements, Context context, RecyclerView pRecyclerView) {
        mScoredElements = pScoredElements;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case COURSE:
                View v1 = inflater.inflate(R.layout.grade_courses_item, viewGroup, false);
                viewHolder = new GradeCoursesViewHolder(v1);
                break;
            case LESSON:
                View v2 = inflater.inflate(R.layout.grade_elements_item, viewGroup, false);
                viewHolder = new GradeElementsViewHolder(v2);
                break;
            case TEST:
                View v3 = inflater.inflate(R.layout.grade_list_item, viewGroup, false);
                viewHolder = new GradesViewHolder(v3);
                break;
            default:
                View v4 = inflater.inflate(R.layout.grade_list_item, viewGroup, false);
                viewHolder = new GradesViewHolder(v4);
                break;
        }
        return viewHolder;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int posiiton) {
        switch (viewHolder.getItemViewType()) {
            case COURSE:
                GradeCoursesViewHolder vh1 = (GradeCoursesViewHolder) viewHolder;
                configureCourseViewHolder(vh1, posiiton);
                break;
            case LESSON:
                GradeElementsViewHolder vh2 = (GradeElementsViewHolder) viewHolder;
                configureLessonViewHolder(vh2, posiiton);
                break;
            case TEST:
                GradesViewHolder vh3 = (GradesViewHolder) viewHolder;
                configureTestExamViewHolder(vh3, posiiton);
                break;
            default:
                GradesViewHolder vh4 = (GradesViewHolder) viewHolder;
                break;
        }
    }

    public int getItemCount() {
        return mScoredElements.size();
    }

    private void configureCourseViewHolder(GradeCoursesViewHolder vh1, int position) {
        Course course = (Course) mScoredElements.get(position);
        if(course != null) {
            vh1.getTv2().setText(course.getCourseName());
        }
    }

    private void configureLessonViewHolder(GradeElementsViewHolder vh2, int position) {
        Lesson lesson = (Lesson) mScoredElements.get(position);
        if (lesson != null) {
            vh2.getLessonName().setText(lesson.getName());
        }
    }

    private void configureTestExamViewHolder(GradesViewHolder vh3, int position) {
        Exam scoredElement = (Exam) mScoredElements.get(position);
        if (scoredElement != null) {
                    vh3.getGradesText().setText("Sprawdzian");
                    vh3.getGradesResult().setText("Ocena: " + scoredElement.getGrade().toString());
        }
    }
}
