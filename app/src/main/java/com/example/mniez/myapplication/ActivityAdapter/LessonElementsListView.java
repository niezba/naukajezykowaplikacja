package com.example.mniez.myapplication.ActivityAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by mniez on 05.11.2017.
 */

public class LessonElementsListView extends ListView {

    public LessonElementsListView  (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LessonElementsListView  (Context context) {
        super(context);
    }

    public LessonElementsListView  (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}