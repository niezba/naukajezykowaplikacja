package com.example.mniez.myapplication.TeacherModule;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
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
import com.example.mniez.myapplication.ObjectHelper.User;
import com.example.mniez.myapplication.ObjectHelper.UsersCourse;
import com.example.mniez.myapplication.ObjectHelper.UsersExam;
import com.example.mniez.myapplication.ObjectHelper.UsersLesson;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.ActivityAdapter.GradesListAdapter;
import com.example.mniez.myapplication.TeacherModule.ActivityAdapter.UserListAdapter;

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

public class UsersActivity extends TeacherBaseDrawerActivity {

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
    ArrayList<User> allUsers = new ArrayList<>();
    MobileDatabaseReader dbReader;
    GradesFetchTask mFetchTask = null;
    private UserLoginTask mAuthTask = null;
    private RecyclerView recyclerView;
    private UserListAdapter mAdapter;
    String currentUsername;
    String currentPassword;
    private View mProgressView;


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
        recyclerView.setHasFixedSize(true);
        mAdapter = new UserListAdapter(allUsers, this, recyclerView);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
                    builder.setMessage("To potrwa chwilkę")
                            .setTitle("Wykonać synchronizację?");
                    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(UsersActivity.this, SynchronizationActivity.class);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
                    builder.setMessage("Program wykona wówczas pełną synchronizację danych, może to troszkę potrwać.")
                            .setTitle("Chcesz pracować w trybie offline?");
                    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(UsersActivity.this, FullSynchronizationActivity.class);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
                    builder.setMessage("To może trochę potrwać.")
                            .setTitle("Wykonać pełną synchronizację?");
                    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(UsersActivity.this, FullSynchronizationActivity.class);
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
                    URL webpageEndpoint = new URL("http://pzmmd.cba.pl/api/courses");
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
                        for (int i = 0; i < coursesCount; i++) {
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
                            String nativeLanguage = singleCourse.get("nativeLanguage").toString();
                            newCourse.setNativeLanguageName(nativeLanguage);
                            String learningLanguage = singleCourse.get("learningLanguage").toString();
                            newCourse.setLearnedLanguageName(learningLanguage);
                            dbReader.insertCourse(newCourse);
                            dbReader.updateCourse(newCourse);

                            URL participantsEndpoint = new URL("http://pzmmd.cba.pl/api/teacher/courseParticipants/" + courseId);
                            HttpURLConnection participantsConnection = (HttpURLConnection) participantsEndpoint.openConnection();
                            participantsConnection.setRequestMethod("GET");
                            participantsConnection.setDoOutput(true);
                            participantsConnection.connect();

                            BufferedReader bp = new BufferedReader(new InputStreamReader(participantsEndpoint.openStream()));
                            StringBuilder sp = new StringBuilder();

                            String linep;
                            while ((linep = bp.readLine()) != null) {
                                sp.append(linep + "\n");
                            }
                            bp.close();

                            String jsonParticipantsString = sp.toString();
                            System.out.println("JSON: " + jsonParticipantsString);
                            JSONArray participantsArray = new JSONArray(jsonParticipantsString);
                            participantsConnection.disconnect();
                            int participantsCount = participantsArray.length();
                            for (int p = 0; p < participantsCount; p++) {
                                JSONObject singleParticipant = participantsArray.getJSONObject(p);
                                User singleUser = new User();
                                singleUser.setUserId(singleParticipant.getInt("id"));
                                singleUser.setUserName(singleParticipant.getString("firstName"));
                                singleUser.setUserSurname(singleParticipant.getString("lastName"));
                                if (singleParticipant.has("avatar")) {
                                    singleUser.setAvatar(singleParticipant.getString("avatar"));
                                }
                                singleUser.setIsAvatarLocal(0);
                                UsersCourse singleUsersCourse = new UsersCourse(singleUser.getUserId(), singleUser.getUserName(), singleUser.getUserSurname(), courseIdInteger);
                                dbReader.insertUser(singleUser);
                                dbReader.insertUserCourse(singleUsersCourse);
                                JSONArray userLessons = singleParticipant.getJSONArray("grades");
                                for (int l = 0; l < userLessons.length(); l++) {
                                    JSONObject singleUsersLessonJson = userLessons.getJSONObject(l);
                                    UsersLesson singleUsersLesson = new UsersLesson(singleUsersCourse.getUserId(), singleUsersCourse.getUserName(), singleUsersCourse.getUserSurname(), singleUsersCourse.getCourseId());
                                    singleUsersLesson.setLessonId(singleUsersLessonJson.getInt("id"));
                                    dbReader.insertUserLesson(singleUsersLesson);
                                    JSONArray userExamsArray = singleUsersLessonJson.getJSONArray("exams");
                                    for (int x = 0; x < userExamsArray.length(); x++) {
                                        JSONObject singleUsersExamJson = userExamsArray.getJSONObject(x);
                                        UsersExam singleUsersExam = new UsersExam(singleUsersLesson.getUserId(), singleUsersLesson.getUserName(), singleUsersLesson.getUserSurname(), singleUsersLesson.getCourseId(), singleUsersLesson.getLessonId());
                                        singleUsersExam.setExamId(singleUsersExamJson.getInt("id"));
                                        singleUsersExam.setGrade(singleUsersExamJson.getJSONArray("users").getJSONObject(0).getInt("grade"));
                                        dbReader.insertUserExam(singleUsersExam);
                                    }
                                }
                            }
                        }
                        System.out.println("Users downloaded");
                        return true;
                    }
                    catch (JSONException e) {
                            e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                allUsers.addAll(dbReader.selectAllUsers());
                System.out.println("UsersCount: " + allUsers.size());
                mAdapter.notifyDataSetChanged();
                mAdapter.getItemCount();
                mFetchTask = null;
            } else {
                mAuthTask = new UsersActivity.UserLoginTask(currentUsername, currentPassword);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
                builder.setMessage("Nastąpił problem z uwierzytelnieniem. Zaloguj się ponownie.")
                        .setTitle("Ups...").setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                        sharedpreferences.edit().clear().commit();
                        UsersActivity.this.deleteDatabase("dummyDatabase");
                        Intent intent = new Intent(UsersActivity.this, LoginActivity.class);
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
