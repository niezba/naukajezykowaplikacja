package com.example.mniez.myapplication.StudentModule.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.ActivityAdapter.CourseListAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by mniez on 06.11.2017.
 */

public class QuestionFragment extends Fragment {

    String questionToAsk;
    int questionType;
    int answerId;
    MobileDatabaseReader dbReader;
    TextView questionView;
    TextView questionToAskView;
    ImageView questionImage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        questionToAsk = b.getString("quesstionToAsk");
        questionType = b.getInt("questionType");
        answerId = b.getInt("questionWord");
        System.out.println(questionToAsk + " " + questionType + " " + answerId);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.question_fragment, container, false);
        questionToAskView = (TextView) rootView.findViewById(R.id.questionToAskView);
        questionView = (TextView) rootView.findViewById(R.id.questionWordView);
        questionImage = (ImageView) rootView.findViewById(R.id.questionImage);
        questionView.setVisibility(View.GONE);
        questionImage.setVisibility(View.GONE);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbReader = new MobileDatabaseReader(getActivity().getApplicationContext());
        setQuestionBasedOnType(questionType, answerId, questionToAsk);
        dbReader.close();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setQuestionBasedOnType(int questionTypeId, int questionWordId, String questionToAsk) {
        Word questionWord = dbReader.getParticularWordData(questionWordId);
        System.out.println(questionWord.getNativeWord());
        switch(questionTypeId) {
            case 7:
                questionToAskView.setText(questionToAsk);
                questionView.setText(questionWord.getNativeWord());
                questionView.setVisibility(View.VISIBLE);
                questionImage.setVisibility(View.GONE);
                break;
            case 8:
                questionToAskView.setText(questionToAsk);
                Picasso.with(QuestionFragment.this.getActivity()).load("http://10.0.2.2:8000/media/imgs/" + questionWord.getPicture()).fit().centerCrop().into(questionImage);
                questionImage.setVisibility(View.VISIBLE);
                questionView.setVisibility(View.GONE);
            default:
                break;
        }
    }

}
