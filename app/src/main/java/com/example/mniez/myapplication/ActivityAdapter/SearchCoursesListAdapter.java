package com.example.mniez.myapplication.ActivityAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mniez.myapplication.CourseBrowseActivity;
import com.example.mniez.myapplication.CourseElementsActivity;
import com.example.mniez.myapplication.Fragments.AllCoursesFragment;
import com.example.mniez.myapplication.Fragments.SearchCoursesFragment;
import com.example.mniez.myapplication.ObjectHelper.Course;
import com.example.mniez.myapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.RunnableFuture;

/**
 * Created by mniez on 25.10.2017.
 */

public class SearchCoursesListAdapter extends RecyclerView.Adapter {
    private ArrayList<Course> mCourses;
    private Context mKontekst;
    private RecyclerView mRecyclerView;
    private SearchCoursesListAdapter.SignInTask mFetchTask = null;
    String responseMessage;
    private ArrayList<Course> mNewCourses;

    private class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        public TextView courseName;
        public TextView levelName;
        public TextView teacherData;
        public ImageView avatarView;
        public Button enterButton;

        public MyViewHolder(View pItem) {
            super(pItem);
            cv = (CardView)pItem.findViewById(R.id.cardView4);
            avatarView = (ImageView) pItem.findViewById(R.id.imageView2);
            courseName = (TextView) pItem.findViewById(R.id.searchCoursesName);
            levelName = (TextView) pItem.findViewById(R.id.searchCoursesDescript);
            teacherData = (TextView) pItem.findViewById(R.id.searchCoursesLead);
            enterButton = (Button) pItem.findViewById(R.id.signButton);
        }
    }

    public SearchCoursesListAdapter(ArrayList<Course> pCourses, Context context, RecyclerView pRecyclerView) {
        mCourses = pCourses;
        mKontekst = context;
        mRecyclerView = pRecyclerView;
        mNewCourses = mCourses;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_course_item, viewGroup, false);
        return new SearchCoursesListAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final Course course = mCourses.get(i);
        ((SearchCoursesListAdapter.MyViewHolder) viewHolder).courseName.setText(course.getCourseName());
        ((SearchCoursesListAdapter.MyViewHolder) viewHolder).levelName.setText(course.getLevelName());
        ((SearchCoursesListAdapter.MyViewHolder) viewHolder).teacherData.setText(course.getTeacherName());
        final String imageUrl = "http://10.0.2.2:8000" + course.getAvatar();
        final int courseId = course.getId();
        final String courseName = course.getCourseName();
        final Boolean isParticipant = course.getParticipant();
        final Boolean isSecured = course.getSecured();
        if(isParticipant == true) {
            ((SearchCoursesListAdapter.MyViewHolder) viewHolder).enterButton.setText("Przejdź");
        }
        else {
            ((SearchCoursesListAdapter.MyViewHolder) viewHolder).enterButton.setText("Zapisz się");
        }
        Picasso.with(mKontekst).load(imageUrl).fit().centerCrop().into(((SearchCoursesListAdapter.MyViewHolder) viewHolder).avatarView);
        ((SearchCoursesListAdapter.MyViewHolder) viewHolder).enterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("position:" + i);
                if(isParticipant == true) {
                    Intent intent = new Intent(v.getContext(), CourseElementsActivity.class);
                    intent.putExtra("titleBar", courseName);
                    intent.putExtra("courseId", courseId);
                    intent.putExtra("courseImage", imageUrl);
                    intent.putExtra("imageTransition", ViewCompat.getTransitionName(((SearchCoursesListAdapter.MyViewHolder) viewHolder).avatarView));
                    System.out.println("CourseId: " + courseId);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) mKontekst, ((SearchCoursesListAdapter.MyViewHolder) viewHolder).avatarView, "courseImage");
                    v.getContext().startActivity(intent, options.toBundle());
                }
                else {
                    if (isSecured == true) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mKontekst);
                        LayoutInflater li =(LayoutInflater) mKontekst.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = li.inflate(R.layout.access_code_input, null);
                        builder.setView(dialogView);
                        final EditText accessPasswd = (EditText) dialogView.findViewById(R.id.course_passwd_input);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                String accessCode = accessPasswd.getText().toString();
                                mFetchTask = new SearchCoursesListAdapter.SignInTask(courseId, accessCode, i);
                                mFetchTask.execute((Void) null);
                            }
                        });
                        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {

                            }
                        });

                        builder.setMessage("Podaj hasło do kursu").setTitle("Ten kurs jest zabezpieczony");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else {
                        mFetchTask = new SearchCoursesListAdapter.SignInTask(courseId, "", i);
                        mFetchTask.execute((Void) null);
                    }
                }

            }
        });
    }

    public int getItemCount() {
        return mCourses.size();
    }

    public class SignInTask extends AsyncTask<Void, Void, Boolean> {

        private final int courseId;
        private final String accessCode;
        private final int courseI;

        SignInTask(int courseId, String accessCode, int courseToSetI) {
            System.out.println("CourseToSetI: " + courseToSetI );
            this.courseId = courseId;
            this.accessCode = accessCode;
            this.courseI = courseToSetI;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                URL webpageEndpoint = new URL("http://10.0.2.2:8000/api/joinCourse");
                HttpURLConnection myConnection = (HttpURLConnection) webpageEndpoint.openConnection();
                myConnection.setRequestMethod("POST");
                myConnection.setDoOutput(true);
                myConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                String request;
                if(accessCode.length() == 0 || accessCode.isEmpty()) {
                    request = "courseId=" + courseId;
                }
                else {
                    request = "courseId=" + courseId + "&accessCode=" + accessCode;
                }
                PrintWriter out = new PrintWriter(myConnection.getOutputStream());
                out.print(request);
                out.close();
                myConnection.connect();
                System.out.print(out);
                if (myConnection.getResponseCode() == 200) {
                    responseMessage = "Zapisano do kursu";
                    System.out.println(courseI);
                    mNewCourses.get(courseI).setParticipant(true);
                    System.out.println("Participant: " + mNewCourses.get(courseI).getParticipant());
                }
                else {
                    responseMessage = "Ups, coś poszło nie tak...";
                }

                myConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            } /*catch (JSONException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }*/


            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Toast.makeText(mKontekst, responseMessage, Toast.LENGTH_SHORT).show();
                mCourses.clear();
                mCourses.addAll(mNewCourses);
                ((CourseBrowseActivity)mKontekst).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
                mFetchTask = null;
            } else {
                Toast.makeText(mKontekst, responseMessage, Toast.LENGTH_SHORT).show();
                mFetchTask = null;
            }
        }

        @Override
        protected void onCancelled() {
            mFetchTask = null;
        }
    }
}
