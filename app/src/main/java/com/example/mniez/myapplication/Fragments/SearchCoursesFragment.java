package com.example.mniez.myapplication.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mniez.myapplication.ActivityAdapter.AllCoursesListAdapter;
import com.example.mniez.myapplication.ActivityAdapter.SearchCoursesListAdapter;
import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.R;

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
import java.util.Iterator;

/**
 * Created by mniez on 25.10.2017.
 */

public class SearchCoursesFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchCoursesListAdapter mAdapter;

    String currentId;
    String currentRole;
    String phrase;
    String language;
    String level;

    ArrayList<Course> courseList = new ArrayList<Course>();
    private SearchCoursesFragment.CourseFetchTask mFetchTask = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        mAdapter = new SearchCoursesListAdapter(courseList, this.getActivity(), recyclerView);
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

            try {
                URL webpageEndpoint = new URL("http://10.0.2.2:8000/api/search?phrase="+searchPhrase+"&language="+searchLang+"&level="+searchLevel);
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
                    while(keys.hasNext()) {
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
                                newCourse.setParticipant(false);
                            }
                            else {
                                newCourse.setParticipant(true);
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
            mAdapter.notifyDataSetChanged();
            mAdapter.getItemCount();
            System.out.println(mAdapter.getItemCount());
            System.out.println(courseList);
            mFetchTask = null;

            if (success) {

            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mFetchTask = null;
        }


    }
}
