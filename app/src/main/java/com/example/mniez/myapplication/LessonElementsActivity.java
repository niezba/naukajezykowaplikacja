package com.example.mniez.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;

public class LessonElementsActivity extends AppCompatActivity {

    MobileDatabaseReader dbReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

    }
}
