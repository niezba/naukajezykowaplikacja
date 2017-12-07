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
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.TeacherModule.CourseDetailsActivity;
import com.example.mniez.myapplication.TeacherModule.CourseElementsActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mniez on 16.10.2017.
 */

public class CourseListAdapter extends RecyclerView.Adapter{

    private ArrayList<Course> mCourses;
    private Context mKontekst;
    private RecyclerView mRecyclerView;
    private Integer mIsOffline;

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

    public CourseListAdapter(ArrayList<Course> pCourses, Context context, RecyclerView pRecyclerView, Integer isOffline) {
        mCourses = pCourses;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
        mIsOffline = isOffline;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.teacher_courses_list_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final Course course = mCourses.get(i);
        ((MyViewHolder) viewHolder).courseName.setText(course.getCourseName());
        ((MyViewHolder) viewHolder).levelName.setText(course.getLevelName());
        ((MyViewHolder) viewHolder).nativeLang.setText(course.getNativeLanguageName());
        ((MyViewHolder) viewHolder).learnedLang.setText(course.getLearnedLanguageName());
        final String imageUrl;
        if(mIsOffline == 0) {
            imageUrl = "http://pzmmd.cba.pl/img/avatars/courses/" + course.getAvatar();
            System.out.println(imageUrl);
            Picasso.with(mKontekst).load(imageUrl).fit().centerCrop().into(((MyViewHolder) viewHolder).avatarView);
        }
        else {
            if (course.getIsAvatarLocal() == 1) {
                imageUrl = course.getAvatarLocal();
                System.out.println(imageUrl);
                File avatar = new File(mKontekst.getFilesDir() + "/Pictures");
                File avatarLocal = new File(avatar, course.getAvatarLocal());
                Picasso.with(mKontekst).load(avatarLocal).fit().centerCrop().into(((MyViewHolder) viewHolder).avatarView);
            }
            else {
                //dorobić obrazek z placeholderem
                imageUrl = "";
                Picasso.with(mKontekst).load(R.drawable.dummy).fit().centerCrop().into(((MyViewHolder) viewHolder).avatarView);
            }
        }
        final int courseId = course.getId();
        final String courseNameString = course.getCourseName();
        ((MyViewHolder) viewHolder).enterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CourseElementsActivity.class);
                intent.putExtra("titleBar", courseNameString);
                intent.putExtra("courseId", courseId);
                intent.putExtra("courseImage", imageUrl);
                intent.putExtra("imageTransition", ViewCompat.getTransitionName(((MyViewHolder) viewHolder).avatarView));
                System.out.println("CourseId: " + courseId);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mKontekst, ((MyViewHolder) viewHolder).avatarView, "courseImage");
                v.getContext().startActivity(intent, options.toBundle());
            }
        });
        ((MyViewHolder) viewHolder).detailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CourseDetailsActivity.class);
                intent.putExtra("titleBar", courseNameString);
                intent.putExtra("courseId", courseId);
                intent.putExtra("courseImage", imageUrl);
                intent.putExtra("imageTransition", ViewCompat.getTransitionName(((MyViewHolder) viewHolder).avatarView));
                System.out.println("CourseId: " + courseId);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mKontekst, ((MyViewHolder) viewHolder).avatarView, "courseImage");
                v.getContext().startActivity(intent, options.toBundle());
            }
        });
    }

    public int getItemCount() {
        return mCourses.size();
    }
}
