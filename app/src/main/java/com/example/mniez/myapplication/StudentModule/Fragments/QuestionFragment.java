package com.example.mniez.myapplication.StudentModule.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mniez.myapplication.DatabaseAccess.MobileDatabaseReader;
import com.example.mniez.myapplication.ObjectHelper.Word;
import com.example.mniez.myapplication.R;
import com.example.mniez.myapplication.StudentModule.ActivityAdapter.CourseListAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;

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
    Button questionSoundButton;
    MediaPlayer mediaPlayer = new MediaPlayer();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        questionToAsk = b.getString("quesstionToAsk");
        questionType = b.getInt("questionType");
        answerId = b.getInt("questionWord");
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                resetText();
            }
        });
        System.out.println(questionToAsk + " " + questionType + " " + answerId);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.question_fragment, container, false);
        questionToAskView = (TextView) rootView.findViewById(R.id.questionToAskView);
        questionView = (TextView) rootView.findViewById(R.id.questionWordView);
        questionImage = (ImageView) rootView.findViewById(R.id.questionImage);
        questionSoundButton = (Button) rootView.findViewById(R.id.questionSound);
        questionView.setVisibility(View.GONE);
        questionImage.setVisibility(View.GONE);
        questionSoundButton.setVisibility(View.GONE);
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
        final Word questionWord = dbReader.getParticularWordData(questionWordId);
        System.out.println(questionWord.getNativeWord());
        switch(questionTypeId) {
            case 7:
                if (questionToAsk.length()!=0) {
                    questionToAskView.setText(questionToAsk);
                }
                else {
                    questionToAskView.setVisibility(View.GONE);
                }
                questionView.setText(questionWord.getNativeWord());
                questionView.setVisibility(View.VISIBLE);
                questionSoundButton.setVisibility(View.GONE);
                questionImage.setVisibility(View.GONE);
                break;
            case 8:
                if (questionToAsk.length()!=0) {
                    questionToAskView.setText(questionToAsk);
                }
                else {
                    questionToAskView.setVisibility(View.GONE);
                }
                Picasso.with(QuestionFragment.this.getActivity()).load("http://10.0.2.2:8000/media/imgs/" + questionWord.getPicture()).fit().centerCrop().into(questionImage);
                questionImage.setVisibility(View.VISIBLE);
                questionView.setVisibility(View.GONE);
                questionSoundButton.setVisibility(View.GONE);
                break;
            case 9:
                if (questionToAsk.length()!=0) {
                    questionToAskView.setText(questionToAsk);
                }
                else {
                    questionToAskView.setVisibility(View.GONE);
                }
                questionSoundButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            resetText();
                            questionSoundButton.setText("...");
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource("http://10.0.2.2:8000/media/sounds/" + questionWord.getTranslatedSound());
                            mediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                questionSoundButton.setVisibility(View.VISIBLE);
                questionView.setVisibility(View.GONE);
                questionImage.setVisibility(View.GONE);
                break;
            case 10:
                if (questionToAsk.length()!=0) {
                    questionToAskView.setText(questionToAsk);
                }
                else {
                    questionToAskView.setVisibility(View.GONE);
                }
                questionView.setText(questionWord.getTranslatedWord());
                questionView.setVisibility(View.VISIBLE);
                questionSoundButton.setVisibility(View.GONE);
                questionImage.setVisibility(View.GONE);
                break;
            case 12:
                if (questionToAsk.length()!=0) {
                    questionToAskView.setText(questionToAsk);
                }
                else {
                    questionToAskView.setVisibility(View.GONE);
                }
                questionView.setText(questionWord.getNativeDefinition());
                questionView.setVisibility(View.VISIBLE);
                questionSoundButton.setVisibility(View.GONE);
                questionImage.setVisibility(View.GONE);
                break;
            case 13:
                if (questionToAsk.length()!=0) {
                    questionToAskView.setText(questionToAsk);
                }
                else {
                    questionToAskView.setVisibility(View.GONE);
                }
                questionView.setText(questionWord.getTranslatedDefinition());
                questionView.setVisibility(View.VISIBLE);
                questionSoundButton.setVisibility(View.GONE);
                questionImage.setVisibility(View.GONE);
                break;
            default:
                if (questionToAsk.length()!=0) {
                    questionToAskView.setText(questionToAsk);
                }
                else {
                    questionToAskView.setVisibility(View.GONE);
                }
                questionView.setText(questionWord.getNativeWord());
                questionView.setVisibility(View.VISIBLE);
                questionSoundButton.setVisibility(View.GONE);
                questionImage.setVisibility(View.GONE);
                break;
        }
    }

    public void resetText() {
        questionSoundButton.setText("Odtwórz");
    }

}
