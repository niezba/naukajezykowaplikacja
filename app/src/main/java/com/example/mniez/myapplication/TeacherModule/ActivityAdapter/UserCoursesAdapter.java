package com.example.mniez.myapplication.TeacherModule.ActivityAdapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mniez.myapplication.ObjectHelper.UsersCourse;
import com.example.mniez.myapplication.ObjectHelper.UsersExam;
import com.example.mniez.myapplication.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mniez on 16.10.2017.
 */

public class UserCoursesAdapter extends RecyclerView.Adapter{

    private ArrayList<UsersCourse> mCourses;
    private Context mKontekst;
    private RecyclerView mRecyclerView;
    Integer mIsOffline;

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView studentCourseName;
        public TextView studentCourseDescription;
        public ImageView studentCourseAvatar;

        public MyViewHolder(View pItem) {
            super(pItem);
            studentCourseName = (TextView) pItem.findViewById(R.id.allCoursesName);
            studentCourseDescription = (TextView) pItem.findViewById(R.id.allCoursesDescript);
            studentCourseAvatar = (ImageView) pItem.findViewById(R.id.imageView3);
        }
    }

    public UserCoursesAdapter(ArrayList<UsersCourse> pCourses, Context context, RecyclerView pRecyclerView, Integer pIsOffline) {
        mCourses = pCourses;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
        mIsOffline = pIsOffline;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_courses_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final UsersCourse usersCourse = mCourses.get(i);
        ((MyViewHolder) viewHolder).studentCourseName.setText(usersCourse.getCourseName());
        ((MyViewHolder) viewHolder).studentCourseDescription.setText(usersCourse.getCourseDescription());
        if(mIsOffline == 0) {
            String imageUrl = "http://pzmmd.cba.pl/web/img/avatars/courses/" + usersCourse.getCourseAvatar();
            System.out.println(imageUrl);
            Picasso.with(mKontekst).load(imageUrl).fit().centerCrop().into(((MyViewHolder) viewHolder).studentCourseAvatar);
        }
        else {
            if (usersCourse.getIsCourseAvatarLocal() == 1) {
                String imageUrl = usersCourse.getCourseAvatarLocal();
                System.out.println(imageUrl);
                File avatar = new File(mKontekst.getFilesDir() + "/Pictures");
                File avatarLocal = new File(avatar, usersCourse.getCourseAvatarLocal());
                Picasso.with(mKontekst).load(avatarLocal).fit().centerCrop().into(((MyViewHolder) viewHolder).studentCourseAvatar);
            }
            else {
                //dorobić obrazek z placeholderem
                String imageUrl = "";
                Picasso.with(mKontekst).load(R.drawable.dummy).fit().centerCrop().into(((MyViewHolder) viewHolder).studentCourseAvatar);
            }
        }
        //String imageUrl = "http://pzmmd.cba.pl/web/img/avatars/courses/" + usersCourse.getCourseAvatar();
        //System.out.println("Image url: " + imageUrl);
        //Picasso.with(mKontekst).load(imageUrl).fit().centerCrop().into(((MyViewHolder) viewHolder).studentCourseAvatar);
    }

    public int getItemCount() {
        return mCourses.size();
    }
}
