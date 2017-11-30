package com.example.mniez.myapplication.TeacherModule;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.LoginActivity;
import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.ObjectHelper.Exam;
import com.example.mniez.myapplication.ObjectHelper.Lesson;
import com.example.mniez.myapplication.ObjectHelper.UsersExam;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.ActivityAdapter.GradesListAdapter;
import com.example.mniez.myapplication.TeacherModule.ActivityAdapter.ExamGradesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ExamGradesActivity extends AppCompatActivity {

    Integer examId;
    Integer courseId;
    ArrayList<UsersExam> allGrades = new ArrayList<>();
    MobileDatabaseReader dbReader;
    private RecyclerView recyclerView;
    private ExamGradesAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        examId = extras.getInt("exam_id");
        courseId = extras.getInt("course_id");
        setContentView(R.layout.activity_exam_grades);
        dbReader = new MobileDatabaseReader(getApplicationContext());
        setTitle("Oceny z egzaminu " + dbReader.selectExamName(examId));
        recyclerView = (RecyclerView) findViewById(R.id.userExamsRecyclerView);
        allGrades = dbReader.selectUsersForExam(examId);
        mAdapter = new ExamGradesAdapter(allGrades, this, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
