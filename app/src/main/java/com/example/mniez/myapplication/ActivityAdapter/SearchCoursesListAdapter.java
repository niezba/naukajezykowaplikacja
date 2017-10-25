package com.example.mniez.myapplication.ActivityAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mniez.myapplication.CourseElementsActivity;
import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mniez on 25.10.2017.
 */

public class SearchCoursesListAdapter extends RecyclerView.Adapter {
    private ArrayList<Course> mCourses;
    private Context mKontekst;
    private RecyclerView mRecyclerView;

    private class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        public TextView courseName;
        public TextView levelName;
        public TextView teacherData;
        public ImageView avatarView;
        public Button enterButton;

        public MyViewHolder(View pItem) {
            super(pItem);
            cv = (CardView)pItem.findViewById(R.id.cardView3);
            avatarView = (ImageView) pItem.findViewById(R.id.imageView3);
            courseName = (TextView) pItem.findViewById(R.id.allCoursesName);
            levelName = (TextView) pItem.findViewById(R.id.allCoursesDescript);
            teacherData = (TextView) pItem.findViewById(R.id.allCoursesLead);
            enterButton = (Button) pItem.findViewById(R.id.enterButton);
        }
    }

    public SearchCoursesListAdapter(ArrayList<Course> pCourses, Context context, RecyclerView pRecyclerView) {
        mCourses = pCourses;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_courses_item, viewGroup, false);
        return new SearchCoursesListAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        final Course course = mCourses.get(i);
        ((SearchCoursesListAdapter.MyViewHolder) viewHolder).courseName.setText(course.getCourseName());
        ((SearchCoursesListAdapter.MyViewHolder) viewHolder).levelName.setText(course.getLevelName());
        ((SearchCoursesListAdapter.MyViewHolder) viewHolder).teacherData.setText(course.getTeacherName() + " " + course.getTeacherSurname());
        String imageUrl = "http://10.0.2.2:8000" + course.getAvatar();
        final int courseId = course.getId();
        final String courseName = course.getCourseName();
        Picasso.with(mKontekst).load(imageUrl).fit().centerCrop().into(((SearchCoursesListAdapter.MyViewHolder) viewHolder).avatarView);
        ((SearchCoursesListAdapter.MyViewHolder) viewHolder).enterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CourseElementsActivity.class);
                intent.putExtra("titleBar", courseName);
                intent.putExtra("courseId", courseId);
                System.out.println("CourseId: " + courseId);
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return mCourses.size();
    }

}
