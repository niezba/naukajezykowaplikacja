package com.example.mniez.myapplication.StudentModule.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by mniez on 06.11.2017.
 */

public class ImageAnswersFragment extends Fragment {

    OnAnswerSelectedListener mCallback;
    private ImageButton ans1, ans2, ans3, ans4;
    protected int[] answerIds;
    protected int currentAnswerTypeId;
    protected String[] answerString;
    int setAnswerId;
    MobileDatabaseReader dbReader;
    ArrayList<Word> wordList;

    public interface OnAnswerSelectedListener {
        public void onAnswerSelected(int answerId);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        answerIds = b.getIntArray("answerIds");
        answerString = b.getStringArray("answerString");
        currentAnswerTypeId = b.getInt("answerType");

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAnswerSelectedListener) {
            mCallback = (OnAnswerSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAnswerSelectedListener" );
        }
    }

    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_answers_fragment, container, false);
        ans1 = (ImageButton) rootView.findViewById(R.id.answerOneImg);
        ans2 = (ImageButton) rootView.findViewById(R.id.answerTwoImg);
        ans3 = (ImageButton) rootView.findViewById(R.id.answerThreeImg);
        ans4 = (ImageButton) rootView.findViewById(R.id.answerFourImg);
        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              ans1.setColorFilter(Color.argb(150, 125, 125, 125));
                ans2.setColorFilter(null);
                ans3.setColorFilter(null);
                ans4.setColorFilter(null);
                mCallback.onAnswerSelected(answerIds[0]);
            }
        });
        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans1.setColorFilter(null);
                ans2.setColorFilter(Color.argb(150, 125, 125, 125));
                ans3.setColorFilter(null);
                ans4.setColorFilter(null);
                mCallback.onAnswerSelected(answerIds[1]);
            }
        });
        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans1.setColorFilter(null);
                ans2.setColorFilter(null);
                ans3.setColorFilter(Color.argb(150, 125, 125, 125));
                ans4.setColorFilter(null);
                mCallback.onAnswerSelected(answerIds[2]);
            }
        });
        ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ans1.setColorFilter(null);
                ans2.setColorFilter(null);
                ans3.setColorFilter(null);
                ans4.setColorFilter(Color.argb(150, 125, 125, 125));
                mCallback.onAnswerSelected(answerIds[3]);
            }
        });
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initiateAnswers(answerString, currentAnswerTypeId, 0, answerIds);
    }

    public void initiateAnswers(String[] newAnswerString, int newQuestionType, int newSetAnswerId, int[] newAnswerIds) {
        answerString = newAnswerString;
        currentAnswerTypeId = newQuestionType;
        answerIds = newAnswerIds;
        setAnswerId = newSetAnswerId;
        System.out.println("Typ pytania: " + newQuestionType);
        switch(currentAnswerTypeId) {
            case 8:
                dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                wordList = new ArrayList<>();
                for(int i = 0; i<4; i++) {
                    wordList.add(dbReader.getParticularWordData(answerIds[i]));
                }
                Picasso.with(getActivity()).load("http://10.0.2.2:8000/media/imgs/" + wordList.get(0).getPicture()).fit().into(ans1);
                System.out.println(wordList.get(0).getPicture());
                Picasso.with(getActivity()).load("http://10.0.2.2:8000/media/imgs/" + wordList.get(1).getPicture()).fit().into(ans2);
                System.out.println(wordList.get(1).getPicture());
                Picasso.with(getActivity()).load("http://10.0.2.2:8000/media/imgs/" + wordList.get(2).getPicture()).fit().into(ans3);
                System.out.println(wordList.get(2).getPicture());
                Picasso.with(getActivity()).load("http://10.0.2.2:8000/media/imgs/" + wordList.get(3).getPicture()).fit().into(ans4);
                System.out.println(wordList.get(3).getPicture());
                if (answerIds[0] == newSetAnswerId) {
                    ans1.performClick();
                }
                else if (answerIds[1] == newSetAnswerId) {
                    ans2.performClick();
                }
                else if (answerIds[2] == newSetAnswerId) {
                    ans3.performClick();
                }
                else if (answerIds[3] == newSetAnswerId) {
                    ans4.performClick();
                }
                else {
                    ans1.setColorFilter(null);
                    ans2.setColorFilter(null);
                    ans3.setColorFilter(null);
                    ans4.setColorFilter(null);
                }
                break;
            default:
                dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
                wordList = new ArrayList<>();
                for(int i = 0; i<4; i++) {
                    wordList.add(dbReader.getParticularWordData(answerIds[i]));
                }
                Picasso.with(getActivity()).load("http://10.0.2.2:8000/media/imgs/" + wordList.get(0).getPicture()).fit().into(ans1);
                Picasso.with(getActivity()).load("http://10.0.2.2:8000/media/imgs/" + wordList.get(1).getPicture()).fit().into(ans2);
                Picasso.with(getActivity()).load("http://10.0.2.2:8000/media/imgs/" + wordList.get(2).getPicture()).fit().into(ans3);
                Picasso.with(getActivity()).load("http://10.0.2.2:8000/media/imgs/" + wordList.get(3).getPicture()).fit().into(ans4);
                ans1.setColorFilter(null);
                ans2.setColorFilter(null);
                ans3.setColorFilter(null);
                ans4.setColorFilter(null);
                break;
        }
    }

}
