package com.example.mniez.myapplication.StudentModule.Others;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mniez.myapplication.R;

/**
 * Created by mniez on 20.11.2017.
 */

public class GradeElementsViewHolder extends RecyclerView.ViewHolder {

    private TextView lessonName, description;

    public GradeElementsViewHolder(View itemView) {
        super(itemView);
        lessonName = (TextView) itemView.findViewById(R.id.lessonName);
        description = (TextView) itemView.findViewById(R.id.description);
    }

    public TextView getLessonName() {
        return lessonName;
    }

    public void setLessonName(TextView lessonName) {
        this.lessonName = lessonName;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }
}
