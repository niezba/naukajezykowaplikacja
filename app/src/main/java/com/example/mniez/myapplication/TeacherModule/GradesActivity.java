package com.example.mniez.myapplication.TeacherModule;

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
import com.example.mniez.myapplication.LoginActivity;
import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.ObjectHelper.Exam;
import com.example.mniez.myapplication.ObjectHelper.Lesson;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.ActivityAdapter.GradesListAdapter;
import com.example.mniez.myapplication.TeacherModule.TeacherBaseDrawerActivity;
import com.example.mniez.myapplication.TeacherModule.FullSynchronizationActivity;
import com.example.mniez.myapplication.TeacherModule.SynchronizationActivity;

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

public class GradesActivity extends TeacherBaseDrawerActivity {

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
    private UserLoginTask mAuthTask = null;
    private RecyclerView recyclerView;
    private GradesListAdapter mAdapter;
    String currentUsername;
    String currentPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.content_grades, frameLayout);
        setTitle("Moi uczniowie");
        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        currentUsername = sharedpreferences.getString(PREFERENCES_USERNAME, "");
        currentPassword = sharedpreferences.getString(PREFERENCES_PASSWORD, "");
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
        //recyclerView.setHasFixedSize(true);
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
            menu.findItem(R.id.action_synch).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_offline:
                if(item.isChecked() == true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GradesActivity.this);
                    builder.setMessage("To potrwa chwilkę")
                            .setTitle("Wykonać synchronizację?");
                    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(GradesActivity.this, SynchronizationActivity.class);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(GradesActivity.this);
                    builder.setMessage("Program wykona wówczas pełną synchronizację danych, może to troszkę potrwać.")
                            .setTitle("Chcesz pracować w trybie offline?");
                    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(GradesActivity.this, FullSynchronizationActivity.class);
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
                return super.onOptionsItemSelected(item);
            case R.id.action_synch:
                    AlertDialog.Builder builder = new AlertDialog.Builder(GradesActivity.this);
                    builder.setMessage("To może trochę potrwać.")
                            .setTitle("Wykonać pełną synchronizację?");
                    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(GradesActivity.this, FullSynchronizationActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    public class GradesFetchTask extends AsyncTask<Void, Void, Boolean> {


        GradesFetchTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(isOffline == 0) {
                try {
                    URL webpageEndpoint = new URL("http://pzmmd.cba.pl/api/userGrades");
                    HttpURLConnection myConnection = (HttpURLConnection) webpageEndpoint.openConnection();
                    myConnection.setRequestMethod("GET");
                    myConnection.setDoOutput(true);
                    myConnection.connect();

                    BufferedReader br = new BufferedReader(new InputStreamReader(webpageEndpoint.openStream()));
                    StringBuilder sb = new StringBuilder();

                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    String jsonString = sb.toString();
                    System.out.println("JSON Grades data downloaded");
                    try {
                        JSONArray jsonObject = new JSONArray(jsonString);
                        String jsonObjectString = jsonObject.toString();
                        System.out.println(jsonObjectString);
                        myConnection.disconnect();
                        int coursesCount = jsonObject.length();
                        for (int i = 0; i< coursesCount; i++) {
                            Course cs = new Course();
                            JSONObject singleCourse = jsonObject.getJSONObject(i);
                            String courseName = singleCourse.get("coursename").toString();
                            cs.setCourseName(courseName);
                            allScoredEls.add(cs);
                            JSONArray courseLessons = singleCourse.getJSONArray("lessons");
                            int lessonsCount = courseLessons.length();
                            for(int j = 0; j< lessonsCount; j++) {
                                Lesson ls = new Lesson();
                                JSONObject singleLesson = courseLessons.getJSONObject(j);
                                String lessonName = singleLesson.get("name").toString();
                                ls.setName(lessonName);
                                allScoredEls.add(ls);
                                JSONArray lessonExams = singleLesson.getJSONArray("exams");
                                int examsCount = lessonExams.length();
                                for(int k = 0; k< examsCount; k++) {
                                    Exam ex = new Exam();
                                    JSONObject singleExam = lessonExams.getJSONObject(k);
                                    String examName = singleExam.get("name").toString();
                                    ex.setName(examName);
                                    String examGrade = singleExam.getJSONArray("users").getJSONObject(0).get("grade").toString();
                                    Integer examGradeInteger = Integer.parseInt(examGrade);
                                    ex.setGrade(examGradeInteger);
                                    allScoredEls.add(ex);
                                }
                            }
                        }
                    }
                    catch (JSONException e) {
                            e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ArrayList<Course> allCourses = dbReader.selectAllCourses();
                for (Course cs : allCourses) {
                    ArrayList<Lesson> allLessonsForCourse = dbReader.selectAllLessonsForCourse(cs.getId());
                    for(Lesson ls : allLessonsForCourse) {
                        ArrayList<Exam> allExams = dbReader.selectAllExamsForLesson(ls.getLessonId());
                        if(allExams.size() > 0) {
                            allScoredEls.add(cs);
                            allScoredEls.add(ls);
                            for (Exam ex : allExams) {
                                allScoredEls.add(ex);
                            }
                        }
                    }
                }
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
                mAuthTask = new GradesActivity.UserLoginTask(currentUsername, currentPassword);
                mAuthTask.execute();
            }
        }

        @Override
        protected void onCancelled() {
            mFetchTask = null;
        }

    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                URL webpageEndpoint = new URL("http://10.0.2.2:8000/api/login?username="+mEmail+"&password="+mPassword);
                HttpURLConnection myConnection = (HttpURLConnection) webpageEndpoint.openConnection();
                myConnection.setRequestMethod("GET");
                myConnection.setDoOutput(true);
                myConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(webpageEndpoint.openStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                String jsonString = sb.toString();
                System.out.println("JSON: " + jsonString);

                JSONObject jsonObject = new JSONObject(jsonString);
                String errCode = jsonObject.get("error_code").toString();
                myConnection.disconnect();
                System.out.println("Error code: " + errCode);
                if(errCode.equals("0")) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    String appUserName = jsonObject.get("username").toString();
                    String appNameSurname = jsonObject.get("firstName").toString() + " " + jsonObject.get("lastName").toString();
                    String appUserId = jsonObject.get("id").toString();
                    JSONArray appRoles = jsonObject.getJSONArray("roles");
                    String appRole = appRoles.getString(0);
                    editor.putString(PREFERENCES_USERNAME, appUserName);
                    editor.putString(PREFERENCES_NAMESURNAME, appNameSurname);
                    editor.putString(PREFERENCES_PASSWORD, mPassword);
                    editor.putString(PREFERENCES_ID, appUserId);
                    editor.putString(PREFERENCES_ROLE, appRole);
                    editor.commit();
                    return true;
                }
                else {
                    return false;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } /*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (JSONException e) {
                e.printStackTrace();
            }


            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                mFetchTask = new GradesFetchTask();
                mFetchTask.execute();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(GradesActivity.this);
                builder.setMessage("Nastąpił problem z uwierzytelnieniem. Zaloguj się ponownie.")
                        .setTitle("Ups...").setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                        sharedpreferences.edit().clear().commit();
                        GradesActivity.this.deleteDatabase("dummyDatabase");
                        Intent intent = new Intent(GradesActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }


    }
}
