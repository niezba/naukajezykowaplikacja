package com.example.mniez.myapplication.StudentModule.ActivityAdapter;

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
import com.example.mniez.myapplication.ObjectHelper.Lecture;
import com.example.mniez.myapplication.ObjectHelper.Lesson;
import com.example.mniez.myapplication.ObjectHelper.LessonElement;
import com.example.mniez.myapplication.ObjectHelper.Test;
import com.example.mniez.myapplication.R;

import java.util.ArrayList;

/**
 * Created by mniez on 16.10.2017.
 */

public class CourseElementListAdapter extends RecyclerView.Adapter {

    private ArrayList<Lesson> mLessons;
    private ArrayList<Test> mTests;
    private ArrayList<Lecture> mLectures;
    private Context mKontekst;
    private RecyclerView mRecyclerView;
    private final int courseId;

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
            overallPoints = (TextView) pItem.findViewById(R.id.points);
            userPoints = (TextView) pItem.findViewById(R.id.userpoints);
            enterButton = (ImageView) pItem.findViewById(R.id.button);
            listView = (ListView) pItem.findViewById(R.id.listView1);
            listView.setVisibility(View.GONE);
        }
    }

    public CourseElementListAdapter(ArrayList<Lesson> pLessons, ArrayList<Test> pTests, ArrayList<Lecture> pLectures, Context context, RecyclerView pRecyclerView, int courseId) {
        mLessons = pLessons;
        mTests = pTests;
        mLectures = pLectures;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
        this.courseId = courseId;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_elements_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final int[] mExpanded = {0};
        MobileDatabaseReader dbReader =  new MobileDatabaseReader(mKontekst);
        Lesson lesson = mLessons.get(i);
        ((MyViewHolder) viewHolder).courseElementName.setText(lesson.getName());
        ((MyViewHolder) viewHolder).elementDescription.setText(lesson.getDescription());
        ((MyViewHolder) viewHolder).lessonNumber.setText("Lekcja " + lesson.getLessonNumber());
        ((MyViewHolder) viewHolder).overallPoints.setText(" " + lesson.getOverallPoints());
        ((MyViewHolder) viewHolder).userPoints.setText("Zdobyte punkty: " + lesson.getUserPoints());
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
        LessonElementsAdapter lesAdapter = new LessonElementsAdapter(mKontekst, allLessonElements, courseId);
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