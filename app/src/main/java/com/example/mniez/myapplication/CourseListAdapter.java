package com.example.mniez.myapplication;

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

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mniez on 16.10.2017.
 */

public class CourseListAdapter extends RecyclerView.Adapter{

    private ArrayList<Course> mCourses;
    private Context mKontekst;
    private RecyclerView mRecyclerView;

    private class MyViewHolder extends RecyclerView.ViewHolder {


        CardView cv;
        public TextView courseName;
        public TextView levelName;
        public TextView teacherData;
        public TextView nativeLang;
        public TextView learnedLang;
        public ImageView avatarView;
        public Button enterButton;
        public Button detailButton;

        public MyViewHolder(View pItem) {
            super(pItem);
            cv = (CardView)pItem.findViewById(R.id.cardView1);
            courseName = (TextView) pItem.findViewById(R.id.textView2);
            levelName = (TextView) pItem.findViewById(R.id.textView4);
            teacherData = (TextView) pItem.findViewById(R.id.textView5);
            nativeLang = (TextView) pItem.findViewById(R.id.textView6);
            learnedLang = (TextView) pItem.findViewById(R.id.textView7);
            avatarView = (ImageView) pItem.findViewById(R.id.imageView2);
            enterButton = (Button) pItem.findViewById(R.id.button);
            detailButton = (Button) pItem.findViewById(R.id.button2);
        }
    }

    public CourseListAdapter(ArrayList<Course> pCourses, Context context, RecyclerView pRecyclerView) {
        mCourses = pCourses;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.courses_list_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        final Course course = mCourses.get(i);
        ((MyViewHolder) viewHolder).courseName.setText(course.getCourseName());
        ((MyViewHolder) viewHolder).levelName.setText(course.getLevelName());
        ((MyViewHolder) viewHolder).teacherData.setText(course.getTeacherName() + " " + course.getTeacherSurname());
        ((MyViewHolder) viewHolder).nativeLang.setText(course.getNativeLanguageName());
        ((MyViewHolder) viewHolder).learnedLang.setText(course.getLearnedLanguageName());
        String imageUrl = "http://10.0.2.2:8000" + course.getAvatar();
        final int courseId = course.getId();
        final String courseName = course.getCourseName();
        Picasso.with(mKontekst).load(imageUrl).fit().centerCrop().into(((MyViewHolder) viewHolder).avatarView);
        ((MyViewHolder) viewHolder).enterButton.setOnClickListener(new View.OnClickListener() {
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
