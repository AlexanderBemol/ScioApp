package com.nordokod.scio.view;


import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.constants.QuestionUtils;
import com.nordokod.scio.entity.TrueFalseQuestion;
import com.nordokod.scio.model.StarsHistory;

import java.util.Objects;

public class TrueFalseQuestionFragment extends Fragment implements BasicFragment{

    private Context context;
    private TrueFalseQuestion trueFalseQuestion;
    private StudyGuideActivity studyGuideActivity;

    private int starsEarned = 0;
    private boolean isAnswered = false;

    private Animation pressCard;
    private Animation starEarnedAnimation;

    public TrueFalseQuestionFragment() {}

    public TrueFalseQuestionFragment(Context context, TrueFalseQuestion trueFalseQuestion, StudyGuideActivity studyGuideActivity) {
        this.context = context;
        this.trueFalseQuestion = trueFalseQuestion;
        this.studyGuideActivity = studyGuideActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_true_false_question, container, false);

        initComponents(view);
        initListeners();
        initAnimations();

        return view;
    }

    @Override
    public void initComponents(View view) {
        TV_Question                     = view.findViewById(R.id.TFFragment_TV_Question);

        IV_Star_1                       = view.findViewById(R.id.TFFragment_IV_Star_1);
        IV_Star_2                       = view.findViewById(R.id.TFFragment_IV_Star_2);
        IV_Star_3                       = view.findViewById(R.id.TFFragment_IV_Star_3);

        TV_True_Answer                  = view.findViewById(R.id.TFFragment_True_Answer);
        TV_False_Answer                 = view.findViewById(R.id.TFFragment_False_Answer);
    }

    @Override
    public void initListeners() {
        TV_True_Answer.setOnClickListener(v -> {
            TV_True_Answer.startAnimation(pressCard);

            if (!isAnswered) {
                if (trueFalseQuestion.isAnswer()) {
                    QuestionUtils.showCorrectAnswerCard(context, TV_True_Answer);
                    starsEarned = 3;
                } else {
                    QuestionUtils.showCorrectAnswerCard(context, TV_True_Answer);
                    QuestionUtils.showIncorrectAnswerCard(context, TV_False_Answer);
                }

                if (starsEarned > 0)
                    QuestionUtils.fillStarsEarned(context, starsEarned, IV_Star_1, IV_Star_2, IV_Star_3, starEarnedAnimation);

                if (studyGuideActivity != null)
                    studyGuideActivity.updateStarsEarnedAmount(starsEarned);

                isAnswered = true;

                //insert in Log
                StarsHistory starsHistory = new StarsHistory(context);
                starsHistory.createStarHistory(trueFalseQuestion.getGuide(),trueFalseQuestion.getId(),starsEarned,1);
            }
        });

        TV_False_Answer.setOnClickListener(v -> {
            TV_False_Answer.startAnimation(pressCard);

            if (!isAnswered) {
                if (!trueFalseQuestion.isAnswer()) {
                    QuestionUtils.showCorrectAnswerCard(context, TV_False_Answer);
                    starsEarned = 3;
                } else {
                    QuestionUtils.showCorrectAnswerCard(context, TV_False_Answer);
                    QuestionUtils.showIncorrectAnswerCard(context, TV_True_Answer);
                }

                if (starsEarned > 0)
                    QuestionUtils.fillStarsEarned(context, starsEarned, IV_Star_1, IV_Star_2, IV_Star_3, starEarnedAnimation);

                if (studyGuideActivity != null)
                    studyGuideActivity.updateStarsEarnedAmount(starsEarned);

                isAnswered = true;
            }
        });
    }

    @Override
    public void initAnimations() {
        pressCard               = AnimationUtils.loadAnimation(context, R.anim.press);
        starEarnedAnimation     = AnimationUtils.loadAnimation(context, R.anim.star_earned);
    }

    @Override
    public void onStart() {
        super.onStart();

        TV_Question.setText(trueFalseQuestion.getQuestion());
    }

    ////////////////////////////////////////////////////////////////////////// Objects from the view
    private AppCompatImageView IV_Star_1;
    private AppCompatImageView IV_Star_2;
    private AppCompatImageView IV_Star_3;
    private AppCompatTextView TV_Question;
    private AppCompatTextView TV_True_Answer;
    private AppCompatTextView TV_False_Answer;
}
