package com.example.mniez.myapplication.StudentModule;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.ObjectHelper.ScoredElement;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.ActivityAdapter.GradesListAdapter;

import java.util.ArrayList;

public class GradeLessonsActivity extends BaseDrawerActivity {

    SharedPreferences sharedpreferences;
    private static final String MY_PREFERENCES = "DummyLangPreferences";
    private static final String PREFERENCES_USERNAME = "loggedUserLogin";
    private static final String PREFERENCES_PASSWORD = "loggedUserPassword";
    private static final String PREFERENCES_NAMESURNAME = "loggedUserNameSurname";
    private static final String PREFERENCES_ROLE = "loggedUserMainRole";
    private static final String PREFERENCES_ID = "loggedUserId";
    private static final String PREFERENCES_OFFLINE = "isOffline";
    private static final String ADMIN_ROLE_NAME = "Administrator";
    private static final String TEACHER_ROLE_NAME = "Nauczyciel";
    private static final String STUDENT_ROLE_NAME = "Uczeń";

    Integer isOffline;
    ArrayList<Object> allScoredEls = new ArrayList<>();
    MobileDatabaseReader dbReader;
    GradesFetchTask mFetchTask = null;
    private RecyclerView recyclerView;
    private GradesListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.content_grades, frameLayout);
        setTitle("Moje oceny");
        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String currentUsername = sharedpreferences.getString(PREFERENCES_USERNAME, "");
        String currentPassword = sharedpreferences.getString(PREFERENCES_PASSWORD, "");
        String currentNameSurname = sharedpreferences.getString(PREFERENCES_NAMESURNAME, "");
        String currentId = sharedpreferences.getString(PREFERENCES_ID, "");
        String currentRole = sharedpreferences.getString(PREFERENCES_ROLE, "");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView= navigationView.getHeaderView(0);
        TextView navUsername = (TextView) navHeaderView.findViewById(R.id.loggedUsername);
        TextView navFullname = (TextView) navHeaderView.findViewById(R.id.loggedNameSurname);
        String userRole;
        switch(currentRole) {
            case "ROLE_ADMIN":
                userRole = ADMIN_ROLE_NAME;
                break;
            case "ROLE_TEACHER":
                userRole = TEACHER_ROLE_NAME;
                break;
            case "ROLE_STUDENT":
                userRole = STUDENT_ROLE_NAME;
                break;
            default:
                userRole = "Rola niezdefiniowana";
                break;
        }
        navUsername.setText(userRole);
        navFullname.setText(currentNameSurname);
        isOffline = sharedpreferences.getInt("isOffline", 0);
        dbReader = new MobileDatabaseReader(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.gradesRecyclerView);
        recyclerView.setHasFixedSize(true);
        mAdapter = new GradesListAdapter(allScoredEls, this, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mFetchTask = new GradesFetchTask();
        mFetchTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offline, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(isOffline == 1) {
            menu.findItem(R.id.action_offline).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_offline:
                if(item.isChecked() == true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GradeLessonsActivity.this);
                    builder.setMessage("To potrwa chwilkę")
                            .setTitle("Wykonać synchronizację?");
                    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(GradeLessonsActivity.this, SynchronizationActivity.class);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putInt(PREFERENCES_OFFLINE, 0);
                            editor.commit();
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putInt(PREFERENCES_OFFLINE, 0);
                            editor.commit();
                            item.setChecked(false);
                            isOffline = 0;
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(item.isChecked() == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GradeLessonsActivity.this);
                    builder.setMessage("Program wykona wówczas pełną synchronizację danych, może to troszkę potrwać.")
                            .setTitle("Chcesz pracować w trybie offline?");
                    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(GradeLessonsActivity.this, FullSynchronizationActivity.class);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putInt(PREFERENCES_OFFLINE, 1);
                            editor.commit();
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(2).setChecked(true);
    }

    public class GradesFetchTask extends AsyncTask<Void, Void, Boolean> {


        GradesFetchTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ArrayList<Course> allCourses = dbReader.selectAllCourses();
            for(Course cs : allCourses) {
                allScoredEls.add(new ScoredElement(cs.getId(), cs.getCourseName(), 3));
                /*ArrayList<Lesson> allLessonsForCourse = dbReader.selectAllLessonsForCourse(cs.getId());
                for(Lesson ls : allLessonsForCourse) {
                    allScoredEls.add(new ScoredElement(cs.getId(), cs.getCourseName(), ls.getLessonId(), ls.getName(), 4));
                    allScoredEls.addAll(dbReader.getAllExamsAndTestsScoresForCourseAndLesson(cs.getId(), ls.getLessonId()));
                }*/
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                mAdapter.notifyDataSetChanged();
                mAdapter.getItemCount();
                mFetchTask = null;
            } else {
                mFetchTask = null;
            }
        }

        @Override
        protected void onCancelled() {
            mFetchTask = null;
        }


    }
}
