package com.example.mniez.myapplication.TeacherModule.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mniez on 06.11.2017.
 */

public class ImageAnswersFragment extends Fragment {

    private ImageButton ans1;
    protected int answerIds;
    protected int currentAnswerTypeId;
    protected String answerString;
    int setAnswerId;
    MobileDatabaseReader dbReader;
    ArrayList<Word> wordList;
    Integer isOffline;
    int isCompleted;
    int correctAnswer;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        answerIds = b.getInt("answerIds");
        answerString = b.getString("answerString");
        currentAnswerTypeId = b.getInt("answerType");
        isOffline = b.getInt("isOffline", 0);

    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onDetach() {
        super.onDetach();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_answers_fragment_teacher, container, false);
        ans1 = (ImageButton) rootView.findViewById(R.id.answerOneImg);
        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initiateAnswers(answerString, currentAnswerTypeId, 0, answerIds, 0, 0);
    }

    public void initiateAnswers(String newAnswerString, int newQuestionType, int newSetAnswerId, int newAnswerIds, int newIsCompleted, int currentCorrectAnswer) {
        answerString = newAnswerString;
        currentAnswerTypeId = newQuestionType;
        answerIds = newAnswerIds;
        setAnswerId = newSetAnswerId;
        isCompleted = newIsCompleted;
        correctAnswer = currentCorrectAnswer;
        System.out.println("Typ pytania: " + newQuestionType);
        switch(currentAnswerTypeId) {
            case 8:
                    dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                    wordList = new ArrayList<>();
                    wordList.add(dbReader.getParticularWordData(answerIds));
                    if (isOffline == 0) {
                        Picasso.with(getActivity()).load("http://pzmmd.cba.pl/media/imgs/" + wordList.get(0).getPicture()).fit().into(ans1);
                        System.out.println(wordList.get(0).getPicture());
                    } else {
                        File avatar = new File(ImageAnswersFragment.this.getActivity().getFilesDir() + "/Pictures");
                        if (wordList.get(0).getIsPictureLocal() == 1) {
                            File avatar1Local = new File(avatar, wordList.get(0).getPictureLocal());
                            Picasso.with(ImageAnswersFragment.this.getActivity()).load(avatar1Local).fit().centerCrop().into(ans1);
                        }
                    }
                break;
            default:
                dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                wordList = new ArrayList<>();
                    wordList.add(dbReader.getParticularWordData(answerIds));
                break;
        }
    }

}
