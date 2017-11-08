package com.example.mniez.myapplication.StudentModule.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.mniez.myapplication.R;

/**
 * Created by mniez on 06.11.2017.
 */

public class ProgressFragment extends Fragment {

    ProgressBar testProgressBar;
    Double progress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        progress = b.getDouble("progress");

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.progress_fragment, container, false);
        testProgressBar = (ProgressBar) rootView.findViewById(R.id.testProgressBar);
        testProgressBar.setProgress(progress.intValue());
        testProgressBar.setScaleY(3f);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void setProgress(Double newProgress) {
        progress = newProgress;
        testProgressBar.setProgress(progress.intValue());
    }
}
