package com.example.mniez.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import com.example.mniez.myapplication.ActivityAdapter.CourseListAdapter;
import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Course;

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

public class MainActivity extends BaseDrawerActivity {

    SharedPreferences sharedpreferences;
    private static final String MY_PREFERENCES = "DummyLangPreferences";
    private static final String PREFERENCES_USERNAME = "loggedUserLogin";
    private static final String PREFERENCES_PASSWORD = "loggedUserPassword";
    private static final String PREFERENCES_NAMESURNAME = "loggedUserNameSurname";
    private static final String PREFERENCES_ROLE = "loggedUserMainRole";
    private static final String PREFERENCES_ID = "loggedUserId";
    private static final String ADMIN_ROLE_NAME = "Administrator";
    private static final String TEACHER_ROLE_NAME = "Nauczyciel";
    private static final String STUDENT_ROLE_NAME = "Uczeń";

    private RecyclerView recyclerView;
    private CourseListAdapter mAdapter;

    private View mProgressView;
    String currentId;
    String currentRole;

    ArrayList<Course> courseList = new ArrayList<Course>();
    private CourseFetchTask mFetchTask = null;

    MobileDatabaseReader dbReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.content_main, frameLayout);

        mProgressView = findViewById(R.id.login_progress);
        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String currentUsername = sharedpreferences.getString(PREFERENCES_USERNAME, "");
        String currentPassword = sharedpreferences.getString(PREFERENCES_PASSWORD, "");
        String currentNameSurname = sharedpreferences.getString(PREFERENCES_NAMESURNAME, "");
        currentId = sharedpreferences.getString(PREFERENCES_ID, "");
        currentRole = sharedpreferences.getString(PREFERENCES_ROLE, "");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView= navigationView.getHeaderView(0);
        TextView navUsername = (TextView) navHeaderView.findViewById(R.id.loggedUsername);
        TextView navFullname = (TextView) navHeaderView.findViewById(R.id.loggedNameSurname);
        setTitle("Moje kursy");
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
        dbReader = new MobileDatabaseReader(getApplicationContext());
        dbReader.getWritableDatabase();
        showProgress(true);
        mFetchTask = new CourseFetchTask(currentId);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCourse);
        recyclerView.setHasFixedSize(true);
        mAdapter = new CourseListAdapter(courseList, this, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mFetchTask.execute((Void) null);
        System.out.println(mAdapter.getItemCount());
        dbReader.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            /*mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });*/

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class CourseFetchTask extends AsyncTask<Void, Void, Boolean> {

        private final String userId;

        CourseFetchTask(String currentId) {
            userId = currentId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                URL webpageEndpoint = new URL("http://10.0.2.2:8000/api/courses");
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

                try {
                    JSONArray jsonObject = new JSONArray(jsonString);
                    String jsonObjectString = jsonObject.toString();
                    System.out.println(jsonObjectString);
                    myConnection.disconnect();
                    int coursesCount = jsonObject.length();
                    for (int i = 0; i < coursesCount; i++){
                        Course newCourse = new Course();
                        JSONObject singleCourse = jsonObject.getJSONObject(i);
                        String courseId = singleCourse.get("id").toString();
                        Integer courseIdInteger = Integer.parseInt(courseId);
                        newCourse.setId(courseIdInteger);
                        String courseName = singleCourse.get("coursename").toString();
                        newCourse.setCourseName(courseName);
                        String description = singleCourse.get("description").toString();
                        newCourse.setDescription(description);
                        String createdAt = singleCourse.get("createdAt").toString();
                        newCourse.setCreatedAt(createdAt);
                        String levelName = singleCourse.get("levelName").toString();
                        newCourse.setLevelName(levelName);
                        String avatar = singleCourse.get("avatar").toString();
                        newCourse.setAvatar(avatar);
                        String teacherFirstName = singleCourse.get("teacherFirstName").toString();
                        newCourse.setTeacherName(teacherFirstName);
                        String teacherLastName = singleCourse.get("teacherLastName").toString();
                        newCourse.setTeacherSurname(teacherLastName);
                        String nativeLanguage = singleCourse.get("nativeLanguage").toString();
                        newCourse.setNativeLanguageName(nativeLanguage);
                        String learningLanguage = singleCourse.get("learningLanguage").toString();
                        newCourse.setLearnedLanguageName(learningLanguage);
                        dbReader.insertCourse(newCourse);
                        System.out.println(courseIdInteger + " " + courseName + " " + description + " " + createdAt + " " + levelName
                                + " " + teacherFirstName + " " + teacherLastName + " " + nativeLanguage + " " + learningLanguage);
                    }
                    return true;
                } catch (JSONException e) {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String errCode = jsonObject.get("error_code").toString();
                    System.out.println("Error code: " + errCode);
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
            courseList.addAll(dbReader.selectAllCourses());
            mAdapter.notifyDataSetChanged();
            mAdapter.getItemCount();
            System.out.println(courseList);
            mFetchTask = null;
            showProgress(false);


            if (success) {

            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mFetchTask = null;
            showProgress(false);
        }


    }

}
