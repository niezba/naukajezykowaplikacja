package com.example.mniez.myapplication.StudentModule.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.mniez.myapplication.LoginActivity;
import com.example.mniez.myapplication.StudentModule.ActivityAdapter.SearchCoursesListAdapter;
import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.CourseBrowseActivity;
import com.example.mniez.myapplication.StudentModule.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mniez on 25.10.2017.
 */

public class SearchCoursesFragment extends Fragment {

    public RecyclerView recyclerView;
    public SearchCoursesListAdapter mAdapter;

    SharedPreferences sharedpreferences;
    private static final String MY_PREFERENCES = "DummyLangPreferences";
    private static final String PREFERENCES_USERNAME = "loggedUserLogin";
    private static final String PREFERENCES_PASSWORD = "loggedUserPassword";

    String currentId;
    String currentRole;
    String phrase;
    String language;
    String level;
    Integer isOffline;
    String currentUsername;
    String currentPassword;

    ArrayList<Course> courseList = new ArrayList<Course>();
    private UserLoginTask mAuthTask = null;
    public SearchCoursesFragment.CourseFetchTask mFetchTask = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = this.getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        isOffline = sharedpreferences.getInt("isOffline", 0);
        currentUsername = sharedpreferences.getString(PREFERENCES_USERNAME, "");
        currentPassword = sharedpreferences.getString(PREFERENCES_PASSWORD, "");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_courses_fragment, container, false);
        final EditText phraseInput = (EditText) rootView.findViewById(R.id.phraseInput);
        final EditText langInput = (EditText) rootView.findViewById(R.id.langInput);
        final EditText levInput = (EditText) rootView.findViewById(R.id.levelInput);
        Button searchButton = (Button) rootView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phrase = phraseInput.getText().toString();
                language = langInput.getText().toString();
                level = levInput.getText().toString();
                mFetchTask = new SearchCoursesFragment.CourseFetchTask(currentId, phrase, language, level);
                mFetchTask.execute((Void) null);
            }
        });
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerViewSearchCourses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SearchCoursesListAdapter(courseList, this.getActivity(), recyclerView, this, null);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public class CourseFetchTask extends AsyncTask<Void, Void, Boolean> {

        private final String userId;
        private final String searchPhrase;
        private final String searchLang;
        private final String searchLevel;

        CourseFetchTask(String currentId, String phrase, String language, String level) {
            userId = currentId;
            searchPhrase = phrase;
            searchLang = language;
            searchLevel = level;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            if(isOffline == 0) {
                try {
                    URL webpageEndpoint = new URL("http://pzmmd.cba.pl/api/search?phrase=" + searchPhrase + "&language=" + searchLang + "&level=" + searchLevel);
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
                    courseList.clear();

                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String jsonObjectString = jsonObject.toString();
                        System.out.println(jsonObjectString);
                        myConnection.disconnect();
                        Iterator<?> keys = jsonObject.keys();
                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            if (jsonObject.get(key) instanceof JSONObject) {
                                Course newCourse = new Course();
                                Integer courseIdInteger = Integer.parseInt(key);
                                newCourse.setId(courseIdInteger);
                                String courseName = ((JSONObject) jsonObject.get(key)).get("coursename").toString();
                                newCourse.setCourseName(courseName);
                                String description = ((JSONObject) jsonObject.get(key)).get("description").toString();
                                newCourse.setDescription(description);
                                String createdAt = ((JSONObject) jsonObject.get(key)).get("createdAt").toString();
                                newCourse.setCreatedAt(createdAt);
                                String levelName = ((JSONObject) jsonObject.get(key)).get("level").toString();
                                newCourse.setLevelName(levelName);
                                String avatar = ((JSONObject) jsonObject.get(key)).get("avatar").toString();
                                newCourse.setAvatar(avatar);
                                String teacherFirstName = ((JSONObject) jsonObject.get(key)).get("teacher").toString();
                                newCourse.setTeacherName(teacherFirstName);
                                System.out.println(teacherFirstName);
                                String learningLanguage = ((JSONObject) jsonObject.get(key)).get("language").toString();
                                newCourse.setLearnedLanguageName(learningLanguage);
                                Boolean isParticipant = ((JSONObject) jsonObject.get(key)).getBoolean("isParticipant");
                                if (isParticipant == true) {
                                    newCourse.setParticipant(true);
                                } else {
                                    newCourse.setParticipant(false);
                                }
                                Boolean secured = ((JSONObject) jsonObject.get(key)).getBoolean("secured");
                                newCourse.setSecured(secured);
                                courseList.add(newCourse);
                                System.out.println(courseIdInteger + " " + courseName + " " + description + " " + createdAt + " " + levelName
                                        + " " + teacherFirstName + " " + learningLanguage);
                            }
                        }
                    /*int coursesCount = jsonObject.length();
                    for (int i = 0; i < coursesCount; i++){

                    }*/
                        return true;
                    } catch (JSONException e) {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String errCode = jsonObject.get("error_code").toString();
                        System.out.println("Error code: " + errCode);
                        return true;
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } /*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (JSONException e) {
                    e.printStackTrace();
                    return true;
                }

            }
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                mAdapter.notifyDataSetChanged();
                if(mAdapter.getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    LinearLayout noElements = (LinearLayout) getActivity().findViewById(R.id.search_no_elements_view);
                    noElements.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    LinearLayout noElements = (LinearLayout) getActivity().findViewById(R.id.search_no_elements_view);
                    noElements.setVisibility(View.GONE);
                }
                System.out.println(mAdapter.getItemCount());
                System.out.println(courseList);
                mFetchTask = null;
            } else {
                recyclerView.setVisibility(View.GONE);
                LinearLayout noElements = (LinearLayout) getActivity().findViewById(R.id.search_no_elements_view);
                noElements.setVisibility(View.VISIBLE);
                mFetchTask = null;
                mAuthTask = new UserLoginTask(currentUsername, currentPassword);
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
                URL webpageEndpoint = new URL("http://pzmmd.cba.pl/api/login?username="+mEmail+"&password="+mPassword);
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
                mFetchTask = new CourseFetchTask(currentId, phrase, language, level);
                mFetchTask.execute();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Nastąpił problem z uwierzytelnieniem. Zaloguj się ponownie.")
                        .setTitle("Ups...").setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sharedpreferences = getActivity().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                        sharedpreferences.edit().clear().commit();
                        getActivity().deleteDatabase("dummyDatabase");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
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
