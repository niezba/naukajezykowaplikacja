package com.example.mniez.myapplication.TeacherModule.ActivityAdapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Exam;
import com.example.mniez.myapplication.ObjectHelper.Lecture;
import com.example.mniez.myapplication.ObjectHelper.Lesson;
import com.example.mniez.myapplication.ObjectHelper.LessonElement;
import com.example.mniez.myapplication.ObjectHelper.Test;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.TeacherModule.ActivityAdapter.LessonElementsAdapter;

import java.util.ArrayList;

/**
 * Created by mniez on 16.10.2017.
 */

public class CourseElementListAdapter extends RecyclerView.Adapter {

    private ArrayList<Lesson> mLessons;
    private ArrayList<Test> mTests;
    private ArrayList<Lecture> mLectures;
    private ArrayList<Exam> mExams;
    private Context mKontekst;
    private RecyclerView mRecyclerView;
    private final int courseId;
    private final int isOffline;

    private class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        public TextView courseElementName;
        public TextView elementDescription;
        public TextView lessonNumber;
        public TextView overallPoints;
        public TextView userPoints;
        public ImageView enterButton;
        public ListView listView;

        public MyViewHolder(View pItem) {
            super(pItem);
            cv = (CardView)pItem.findViewById(R.id.cardView2);
            courseElementName = (TextView) pItem.findViewById(R.id.lessonName);
            elementDescription = (TextView) pItem.findViewById(R.id.description);
            lessonNumber = (TextView) pItem.findViewById(R.id.number);
            enterButton = (ImageView) pItem.findViewById(R.id.button);
            listView = (ListView) pItem.findViewById(R.id.listView1);
            listView.setVisibility(View.GONE);
        }
    }

    public CourseElementListAdapter(ArrayList<Lesson> pLessons, ArrayList<Test> pTests, ArrayList<Lecture> pLectures, ArrayList<Exam> pExams, Context context, RecyclerView pRecyclerView, int courseId, int isOffline) {
        mLessons = pLessons;
        mTests = pTests;
        mLectures = pLectures;
        mExams = pExams;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
        this.courseId = courseId;
        this.isOffline = isOffline;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.teacher_course_elements_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final int[] mExpanded = {0};
        MobileDatabaseReader dbReader =  new MobileDatabaseReader(mKontekst);
        Lesson lesson = mLessons.get(i);
        ((MyViewHolder) viewHolder).courseElementName.setText(lesson.getName());
        ((MyViewHolder) viewHolder).elementDescription.setText(lesson.getDescription());
        ((MyViewHolder) viewHolder).lessonNumber.setText("Lekcja " + lesson.getLessonNumber());
        ArrayList<LessonElement> allLessonElements = new ArrayList<>();
        for (Lecture l : mLectures) {
            if(l.getLessonId() == lesson.getLessonId()) {
                int elType = 1;
                int elId = l.getId();
                String elName = l.getName();
                LessonElement newEl = new LessonElement(elType, elId, elName);
                allLessonElements.add(newEl);
            }
        }
        for (Test t : mTests) {
            if(t.getLessonId() == lesson.getLessonId()) {
                int elType = 0;
                int elId = t.getId();
                String elName = t.getDescription();
                int scoredPoints = t.getScore();
                int totalPoints = dbReader.calculateTotalPointsForTest(elId);
                LessonElement newEl = new LessonElement(elType, elId, elName, totalPoints, scoredPoints);
                allLessonElements.add(newEl);
            }
        }
        for (Exam e : mExams) {
            if(e.getLessonId() == lesson.getLessonId()) {
                int elType = 2;
                int elId = e.getId();
                String elName = e.getDescription();
                int scoredPoints = e.getScore();
                int totalPoints = dbReader.calculateTotalPointsForExam(elId);
                int grade = e.getGrade();
                int isPassed = e.getIsPassed();
                System.out.println("Exam lesson element: " + e.getGrade());
                LessonElement newEl = new LessonElement(elType, elId, elName, totalPoints, scoredPoints, grade, isPassed);
                allLessonElements.add(newEl);
            }
        }
        LessonElementsAdapter lesAdapter = new LessonElementsAdapter(mKontekst, allLessonElements, courseId, isOffline);
        ((MyViewHolder) viewHolder).listView.setAdapter(lesAdapter);
        System.out.println("Lekcja: " + lesson.getLessonId() + ", Ilość elementów: " + allLessonElements.size());

        ((MyViewHolder) viewHolder).cv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mExpanded[0] == 0) {
                    ((MyViewHolder) viewHolder).enterButton.animate().rotation((float) 90.0);
                    ((MyViewHolder) viewHolder).listView.setVisibility(View.VISIBLE);
                    mExpanded[0] = 1;
                }
                else if (mExpanded[0] == 1) {
                    ((MyViewHolder) viewHolder).enterButton.animate().rotation((float) 0.0);
                    ((MyViewHolder) viewHolder).listView.setVisibility(View.GONE);
                    mExpanded[0] = 0;
                }
            }
        });
    }

    public int getItemCount() {
        return mLessons.size();
    }
}
