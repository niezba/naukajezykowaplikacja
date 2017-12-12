package com.example.mniez.myapplication.TeacherModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.User;
import com.example.mniez.myapplication.ObjectHelper.UsersCourse;
import com.example.mniez.myapplication.ObjectHelper.UsersExam;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.TeacherModule.ActivityAdapter.ExamGradesAdapter;
import com.example.mniez.myapplication.TeacherModule.ActivityAdapter.UserCoursesAdapter;
import com.example.mniez.myapplication.TeacherModule.ActivityAdapter.UserGradesAdapter;
import com.example.mniez.myapplication.TeacherModule.ActivityAdapter.UserListAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    Integer userId;
    ArrayList<UsersCourse> allCourses = new ArrayList<>();
    ArrayList<UsersExam> allGrades = new ArrayList<>();
    MobileDatabaseReader dbReader;
    private RecyclerView recyclerView, recyclerView2;
    private UserGradesAdapter mAdapter;
    private UserCoursesAdapter mAdapter2;
    private static final String MY_PREFERENCES = "DummyLangPreferences";
    private static final String PREFERENCES_OFFLINE = "isOffline";
    TextView userNameView, userIdView;
    ImageView avatar;
    Integer isOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        userId = extras.getInt("user_id");
        setContentView(R.layout.activity_users);

        TabHost host = (TabHost) findViewById(R.id.tabhost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Kursy");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Kursy");
        host.addTab(spec);

        spec = host.newTabSpec("Sprawdziany");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Sprawdziany");
        host.addTab(spec);
        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        isOffline = sharedpreferences.getInt(PREFERENCES_OFFLINE, 0);
        dbReader = new MobileDatabaseReader(getApplicationContext());
        setTitle("Karta ucznia");
        User currentUser = dbReader.selectUser(userId);
        userNameView = (TextView) findViewById(R.id.usersName);
        userIdView = (TextView) findViewById(R.id.usersId);
        avatar = (ImageView) findViewById(R.id.usersAvatar);
        userNameView.setText(currentUser.getUserName() + " " + currentUser.getUserSurname());
        userIdView.setText("Id ucznia: " + currentUser.getUserId().toString());
        if(isOffline == 0) {
            if (currentUser.getAvatar() != null) {
                Picasso.with(this).load("http://pzmmd.cba.pl/img/avatars/users/" + currentUser.getAvatar()).fit().centerInside().into(avatar);
            } else {
                Picasso.with(this).load(R.drawable.dummy_kopia).fit().centerInside().into(avatar);
            }
        }
        else {
            if (currentUser.getIsAvatarLocal() == 1) {
                String imageUrl = currentUser.getAvatarLocal();
                System.out.println(imageUrl);
                File localavatar = new File(this.getFilesDir() + "/Pictures");
                File avatarLocal = new File(localavatar, currentUser.getAvatarLocal());
                Picasso.with(this).load(avatarLocal).fit().centerCrop().into(avatar);
            }
            else {
                Picasso.with(this).load(R.drawable.dummy_kopia).fit().centerCrop().into(avatar);
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.userGradesRecyclerView);
        allGrades = dbReader.selectAllUsersExam(userId);
        allCourses = dbReader.selectAllUsersCourses(userId);
        mAdapter = new UserGradesAdapter(allGrades, this, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if(allGrades.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            LinearLayout noElements = (LinearLayout) findViewById(R.id.grade_no_elements_view);
            noElements.setVisibility(View.VISIBLE);
        }
        recyclerView2 = (RecyclerView) findViewById(R.id.userCoursesRecyclerView);
        mAdapter2 = new UserCoursesAdapter(allCourses, this, recyclerView2, isOffline);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setAdapter(mAdapter2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        if(allCourses.size() == 0) {
            recyclerView2.setVisibility(View.GONE);
            LinearLayout noElements = (LinearLayout) findViewById(R.id.course_no_elements_view);
            noElements.setVisibility(View.VISIBLE);
        }
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
