package com.example.mniez.myapplication.ActivityAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
            cv = (CardView)pItem.findViewById(R.id.cardView4);
            avatarView = (ImageView) pItem.findViewById(R.id.imageView4);
            courseName = (TextView) pItem.findViewById(R.id.searchCoursesName);
            levelName = (TextView) pItem.findViewById(R.id.searchCoursesDescript);
            teacherData = (TextView) pItem.findViewById(R.id.searchCoursesLead);
            enterButton = (Button) pItem.findViewById(R.id.signButton);
        }
    }

    public SearchCoursesListAdapter(ArrayList<Course> pCourses, Context context, RecyclerView pRecyclerView) {
        mCourses = pCourses;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_course_item, viewGroup, false);
        return new SearchCoursesListAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        final Course course = mCourses.get(i);
        ((SearchCoursesListAdapter.MyViewHolder) viewHolder).courseName.setText(course.getCourseName());
        ((SearchCoursesListAdapter.MyViewHolder) viewHolder).levelName.setText(course.getLevelName());
        ((SearchCoursesListAdapter.MyViewHolder) viewHolder).teacherData.setText(course.getTeacherName());
        String imageUrl = "http://10.0.2.2:8000" + course.getAvatar();
        final int courseId = course.getId();
        final String courseName = course.getCourseName();
        final Boolean isParticipant = course.getParticipant();
        final Boolean isSecured = course.getSecured();
        System.out.print(isSecured);
        if(isParticipant == true) {
            ((SearchCoursesListAdapter.MyViewHolder) viewHolder).enterButton.setText("Przejdź");
        }
        else {
            ((SearchCoursesListAdapter.MyViewHolder) viewHolder).enterButton.setText("Zapisz się");
        }
        Picasso.with(mKontekst).load(imageUrl).fit().centerCrop().into(((SearchCoursesListAdapter.MyViewHolder) viewHolder).avatarView);
        ((SearchCoursesListAdapter.MyViewHolder) viewHolder).enterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isParticipant == true) {
                    Intent intent = new Intent(v.getContext(), CourseElementsActivity.class);
                    intent.putExtra("titleBar", courseName);
                    intent.putExtra("courseId", courseId);
                    System.out.println("CourseId: " + courseId);
                    v.getContext().startActivity(intent);
                }
                else {
                    if (isSecured == true) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mKontekst);
                        LayoutInflater li =(LayoutInflater) mKontekst.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = li.inflate(R.layout.access_code_input, null);
                        builder.setView(dialogView);
                        final EditText accessPasswd = (EditText) dialogView.findViewById(R.id.course_passwd_input);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String accessCode = accessPasswd.getText().toString();
                            }
                        });
                        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        builder.setMessage("Podaj hasło do kursu").setTitle("Ten kurs jest zabezpieczony");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else {

                    }
                }

            }
        });
    }

    public int getItemCount() {
        return mCourses.size();
    }

}
