package com.example.mniez.myapplication.StudentModule.Others;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mniez.myapplication.R;

/**
 * Created by mniez on 20.11.2017.
 */

public class GradeCoursesViewHolder extends RecyclerView.ViewHolder {

    private TextView tv2, tv4, tv6, tv7;

    public GradeCoursesViewHolder(View itemView) {
        super(itemView);
        tv2 = (TextView) itemView.findViewById(R.id.textView2);
    }

    public TextView getTv2() {
        return tv2;
    }

    public void setTv2(TextView tv2) {
        this.tv2 = tv2;
    }

    public TextView getTv4() {
        return tv4;
    }

    public void setTv4(TextView tv4) {
        this.tv4 = tv4;
    }

    public TextView getTv6() {
        return tv6;
    }

    public void setTv6(TextView tv6) {
        this.tv6 = tv6;
    }

    public TextView getTv7() {
        return tv7;
    }

    public void setTv7(TextView tv7) {
        this.tv7 = tv7;
    }
}
