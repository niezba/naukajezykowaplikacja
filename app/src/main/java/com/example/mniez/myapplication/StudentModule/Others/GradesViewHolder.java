package com.example.mniez.myapplication.StudentModule.Others;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mniez.myapplication.R;

/**
 * Created by mniez on 20.11.2017.
 */

public class GradesViewHolder extends RecyclerView.ViewHolder {

    TextView gradesText, gradesResult;

    public GradesViewHolder(View itemView) {
        super(itemView);
        gradesText = (TextView) itemView.findViewById(R.id.gradeText);
        gradesResult = (TextView) itemView.findViewById(R.id.gradeResult);
    }

    public TextView getGradesText() {
        return gradesText;
    }

    public void setGradesText(TextView gradesText) {
        this.gradesText = gradesText;
    }

    public TextView getGradesResult() {
        return gradesResult;
    }

    public void setGradesResult(TextView gradesResult) {
        this.gradesResult = gradesResult;
    }
}
