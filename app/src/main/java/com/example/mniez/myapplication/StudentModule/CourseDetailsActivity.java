package com.example.mniez.myapplication.StudentModule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class CourseDetailsActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    private static final String MY_PREFERENCES = "DummyLangPreferences";
    private static final String PREFERENCES_OFFLINE = "isOffline";
    Integer courseId;
    TextView tex1, tex2, tex3, tex4, tex5, tex6;
    MobileDatabaseReader dbReader;
    Course currentCourse;
    String titleBar;
    String courseImage;
    String imageTransition;
    Integer isOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        courseId = 0;
        courseId = extras.getInt("courseId", 0);
        dbReader = new MobileDatabaseReader(getApplicationContext());
        supportPostponeEnterTransition();
        courseImage = extras.getString("courseImage");
        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        isOffline = sharedpreferences.getInt(PREFERENCES_OFFLINE, 0);
        final ImageView imageView = (ImageView) findViewById(R.id.imageViewDetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageTransition = extras.getString("imageTransition");
            imageView.setTransitionName(imageTransition);
        }
        if (isOffline == 0) {
            Picasso.with(this)
                    .load(courseImage)
                    .noFade()
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                        }
                    });
        }
        else {
            String avatarLocalFile = dbReader.selectCourse(courseId).getAvatarLocal();
            courseImage = avatarLocalFile;
            File avatar = new File(CourseDetailsActivity.this.getFilesDir() + "/Pictures");
            File avatarLocal = new File(avatar, avatarLocalFile);

            Picasso.with(this)
                    .load(avatarLocal)
                    .noFade()
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                        }
                    });
        }
        titleBar = intent.getStringExtra("titleBar");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tex1 = (TextView) findViewById(R.id.textView10);
        tex2 = (TextView) findViewById(R.id.textView12);
        tex3 = (TextView) findViewById(R.id.textView14);
        tex4 = (TextView) findViewById(R.id.textView16);
        tex5 = (TextView) findViewById(R.id.textView18);
        tex6 = (TextView) findViewById(R.id.textView20);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CourseElementsActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("courseImage", courseImage);
                intent.putExtra("imageTransition", ViewCompat.getTransitionName(imageView));
                intent.putExtra("titleBar", titleBar);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(CourseDetailsActivity.this, imageView, "courseImage");
                view.getContext().startActivity(intent, options.toBundle());
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateDataForCourse();
        setTitle(titleBar);
    }

    protected void populateDataForCourse() {
        currentCourse = dbReader.selectCourse(courseId);
        tex1.setText(currentCourse.getNativeLanguageName());
        tex2.setText(currentCourse.getLearnedLanguageName());
        tex3.setText(currentCourse.getTeacherName() + " " + currentCourse.getTeacherSurname());
        tex4.setText(currentCourse.getDescription());
        tex5.setText(currentCourse.getLevelName());
        tex6.setText(currentCourse.getCreatedAt().substring(0,10));
    }
}
