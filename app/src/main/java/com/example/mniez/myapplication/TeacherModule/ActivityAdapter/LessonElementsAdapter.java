package com.example.mniez.myapplication.TeacherModule.ActivityAdapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Lecture;
import com.example.mniez.myapplication.ObjectHelper.LessonElement;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.ExamActivity;
import com.example.mniez.myapplication.StudentModule.TestActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by mniez on 05.11.2017.
 */

public class LessonElementsAdapter extends ArrayAdapter<LessonElement> {

    private final ArrayList<LessonElement> mLessonElements;
    private final Context mKontekst;
    private final int courseId;
    private final int isOffline;
    private FetchLectureForLessonId mFetchTask = null;
    MobileDatabaseReader dbReader;

    public LessonElementsAdapter(Context mKontekst, ArrayList<LessonElement> mLessonElements, int courseId, int isOffline) {
        super(mKontekst, -1, mLessonElements);
        this.mKontekst = mKontekst;
        this.mLessonElements = mLessonElements;
        this.courseId = courseId;
        this.isOffline = isOffline;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mKontekst.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.lesson_elements_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.testName);
        Button textView2 = (Button) rowView.findViewById(R.id.testScore);
        TextView textView3 = (TextView) rowView.findViewById(R.id.textView3);
        final LessonElement singleLessonEl = mLessonElements.get(position);

        final int rowType = singleLessonEl.getLessonElementType();
        final int elId = singleLessonEl.getLessonElementId();

        textView.setText(singleLessonEl.getLessonElementName());
        if(rowType == 0) {
            textView3.setText("Test");
            //textView3.setTextColor(R.color.colorPrimaryDark);
            textView2.setText("Podgląd");
        }
        else if(rowType == 1) {
            textView3.setText("Wykład");
            //textView3.setTextColor(R.color.colorAccent);
            textView2.setText("Podgląd");
        }
        else if(rowType == 2) {
            textView3.setText("Sprawdzian");
            //textView3.setTextColor(R.color.colorAccent);
            textView2.setText("Podgląd");
        }
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                switch (rowType) {
                    case 0:
                                Intent intent = new Intent(view.getContext(), TestActivity.class);
                                intent.putExtra("test_id", singleLessonEl.getLessonElementId());
                                intent.putExtra("course_id", courseId);
                                view.getContext().startActivity(intent);
                        break;
                    case 1:
                        dbReader = new MobileDatabaseReader(mKontekst);
                        Lecture lecture = dbReader.selectSingleLecture(elId);
                        String lectureLocalString = lecture.getLectureLocal();
                        if (isOffline == 1 && lecture.getIsLectureLocal() == 1) {
                            File lectureDir = new File(mKontekst.getApplicationContext().getFilesDir() +  "/Documents");
                            File lectureFile = new File(lectureDir, lectureLocalString);
                            System.out.println("Czy plik istnieje: " + lectureFile.getAbsolutePath());
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            Uri uri = FileProvider.getUriForFile(mKontekst.getApplicationContext(), "com.example.mniez.myapplication.fileprovider", lectureFile);
                            target.setDataAndType(uri,"application/pdf");
                            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            Intent intent2 = Intent.createChooser(target, "Open File");
                            try {
                                mKontekst.startActivity(intent2);
                            } catch (ActivityNotFoundException e) {
                                // Instruct the user to install a PDF reader here, or something
                            }
                        }
                        else {
                            mFetchTask = new FetchLectureForLessonId(elId);
                            mFetchTask.execute();
                            //Toast.makeText(mKontekst, "Brak materiału offline dla wykładu " + elId, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                                    Intent intent3 = new Intent(view.getContext(), ExamActivity.class);
                                    intent3.putExtra("test_id", singleLessonEl.getLessonElementId());
                                    intent3.putExtra("course_id", courseId);
                                    view.getContext().startActivity(intent3);
                        break;
                    default:
                        break;
                }
            }
        });
        return rowView;
    }

    @Override
    public int getCount()
    {
        return (mLessonElements == null) ? 0 : mLessonElements.size();
    }

    public class FetchLectureForLessonId extends AsyncTask<Void, Void, Boolean> {

        Integer lectureId;
        File myFile;

        public FetchLectureForLessonId(Integer lectureId) {
            this.lectureId = lectureId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Lecture onlineLecture = dbReader.selectSingleLecture(lectureId);
            String onlineLectureLocalString = onlineLecture.getLectureLocal();
            String tempFileName = "lecture_" + lectureId + "_temp" + ".pdf";
            URL lectureEndpoint = null;
            try {
                lectureEndpoint = new URL("http://pzmmd.cba.pl/media/documents/" + onlineLecture.getLectureUrl());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                URLConnection lectureConnection = lectureEndpoint.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InputStream inputStream = new BufferedInputStream(lectureEndpoint.openStream(), 10240);
                File myDir = new File(mKontekst.getCacheDir() + "/Documents");
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }
                myFile = new File(myDir, tempFileName);
                FileOutputStream outputStream = new FileOutputStream(myFile);

                byte buffer[] = new byte[1024];
                int dataSize;
                int loadedSize = 0;
                while ((dataSize = inputStream.read(buffer)) != -1) {
                    loadedSize += dataSize;
                    outputStream.write(buffer, 0, dataSize);
                }
                outputStream.close();
                System.out.println("Lecture " + tempFileName + " downloaded");
                return true;
            } catch (IOException e) {

            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mFetchTask = null;

            if (success) {
                Intent target = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(mKontekst, "com.example.mniez.myapplication.fileprovider", myFile);
                target.setDataAndType(uri, "application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    mKontekst.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                }
            } else {

            }
        }
    }
}
