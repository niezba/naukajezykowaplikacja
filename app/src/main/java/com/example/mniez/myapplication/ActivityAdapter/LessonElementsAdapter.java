package com.example.mniez.myapplication.ActivityAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.ObjectHelper.LessonElement;
import com.example.mniez.myapplication.ObjectHelper.Test;
import com.example.mniez.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mniez on 05.11.2017.
 */

public class LessonElementsAdapter extends ArrayAdapter<LessonElement> {

    private final ArrayList<LessonElement> mLessonElements;
    private final Context mKontekst;

    public LessonElementsAdapter(Context mKontekst, ArrayList<LessonElement> mLessonElements) {
        super(mKontekst, -1, mLessonElements);
        this.mKontekst = mKontekst;
        this.mLessonElements = mLessonElements;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mKontekst.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.lesson_elements_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.testName);
        TextView textView2 = (TextView) rowView.findViewById(R.id.testScore);
        TextView textView3 = (TextView) rowView.findViewById(R.id.textView3);
        LessonElement singleLessonEl = mLessonElements.get(position);

        final int rowType = singleLessonEl.getLessonElementType();
        final int elId = singleLessonEl.getLessonElementId();

        textView.setText(singleLessonEl.getLessonElementName());
        if(rowType == 0) {
            textView3.setText("Test");
            textView3.setTextColor(R.color.colorPrimaryDark);
            textView2.setText(singleLessonEl.getLessonElementScoredPoints() + " / " + singleLessonEl.getLessonElementTotalPoints());
        }
        else if(rowType == 1) {
            textView3.setText("Wykład");
            textView3.setTextColor(R.color.colorAccent);
            textView2.setVisibility(View.GONE);
        }
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (rowType) {
                    case 0:
                        Toast.makeText(mKontekst, "Wybrano test id: " + elId, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(mKontekst, "Wybrano materiał id: " + elId, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        return rowView;
    }

    @Override
    public int getCount()
    {
        return (mLessonElements == null) ? 0 : mLessonElements.size();
    }
}
