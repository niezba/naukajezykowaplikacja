package com.example.mniez.myapplication.TeacherModule.ActivityAdapter;

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

import com.example.mniez.myapplication.ObjectHelper.User;
import com.example.mniez.myapplication.ObjectHelper.UsersExam;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.TeacherModule.StudentActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mniez on 16.10.2017.
 */

public class UserListAdapter extends RecyclerView.Adapter{

    private ArrayList<User> mUsers;
    private Context mKontekst;
    private RecyclerView mRecyclerView;
    private Integer mIsOffline;

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView cv;
        public TextView studentName;
        public ImageView studentCourseAvatar;

        public MyViewHolder(View pItem) {
            super(pItem);
            studentName = (TextView) pItem.findViewById(R.id.usersName);
            cv = (CardView) pItem.findViewById(R.id.cardView5);
            studentCourseAvatar = (ImageView) pItem.findViewById(R.id.usersAvatar);
        }
    }

    public UserListAdapter(ArrayList<User> pUsers, Context context, RecyclerView pRecyclerView, Integer pIsOffline) {
        mUsers = pUsers;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
        mIsOffline = pIsOffline;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final User user = mUsers.get(i);
        ((MyViewHolder) viewHolder).studentName.setText(user.getUserName() + " " + user.getUserSurname());
        final int studentId = user.getUserId();
        if(mIsOffline == 0) {
            if (user.getAvatar() != null) {
                String imageUrl = "http://pzmmd.cba.pl/img/avatars/users/" + user.getAvatar();
                System.out.println("Image url: " + imageUrl);
                Picasso.with(mKontekst).load(imageUrl).fit().centerCrop().into(((MyViewHolder) viewHolder).studentCourseAvatar);
            } else {
                Picasso.with(mKontekst).load(R.drawable.dummy_kopia).fit().centerCrop().into(((MyViewHolder) viewHolder).studentCourseAvatar);
            }
        }
        else {
            if (user.getIsAvatarLocal() == 1) {
                String imageUrl = user.getAvatarLocal();
                System.out.println(imageUrl);
                File avatar = new File(mKontekst.getFilesDir() + "/Pictures");
                File avatarLocal = new File(avatar, user.getAvatarLocal());
                Picasso.with(mKontekst).load(avatarLocal).fit().centerCrop().into(((MyViewHolder) viewHolder).studentCourseAvatar);
            }
            else {
                Picasso.with(mKontekst).load(R.drawable.dummy_kopia).fit().centerCrop().into(((MyViewHolder) viewHolder).studentCourseAvatar);
            }
        }

        ((MyViewHolder) viewHolder).cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StudentActivity.class);
                intent.putExtra("user_id", studentId);
                view.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return mUsers.size();
    }
}
