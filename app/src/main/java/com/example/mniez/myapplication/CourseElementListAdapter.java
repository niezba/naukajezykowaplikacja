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

public class CourseElementListAdapter extends RecyclerView.Adapter {

    private ArrayList<Lesson> mLessons;
    private Context mKontekst;
    private RecyclerView mRecyclerView;

    private class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        public TextView courseElementName;
        public TextView elementDescription;
        public TextView lessonNumber;
        public TextView overallPoints;
        public TextView userPoints;
        public Button enterButton;

        public MyViewHolder(View pItem) {
            super(pItem);
            cv = (CardView)pItem.findViewById(R.id.cardView2);
            courseElementName = (TextView) pItem.findViewById(R.id.lessonName);
            elementDescription = (TextView) pItem.findViewById(R.id.description);
            lessonNumber = (TextView) pItem.findViewById(R.id.number);
            overallPoints = (TextView) pItem.findViewById(R.id.points);
            userPoints = (TextView) pItem.findViewById(R.id.userpoints);
            enterButton = (Button) pItem.findViewById(R.id.button);

            enterButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), CourseElementsActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public CourseElementListAdapter(ArrayList<Lesson> pLessons, Context context, RecyclerView pRecyclerView) {
        mLessons = pLessons;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_elements_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        Lesson lesson = mLessons.get(i);
        ((MyViewHolder) viewHolder).courseElementName.setText(lesson.getName());
        ((MyViewHolder) viewHolder).elementDescription.setText(lesson.getDescription());
        ((MyViewHolder) viewHolder).lessonNumber.setText("Lekcja " + lesson.getLessonNumber());
        ((MyViewHolder) viewHolder).overallPoints.setText(" " + lesson.getOverallPoints());
        ((MyViewHolder) viewHolder).userPoints.setText("Zdobyte punkty: " + lesson.getUserPoints());
    }

    public int getItemCount() {
        return mLessons.size();
    }
}
