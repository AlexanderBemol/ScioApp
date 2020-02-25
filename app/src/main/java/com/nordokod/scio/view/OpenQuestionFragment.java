package com.nordokod.scio.view;


import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.constants.QuestionUtils;
import com.nordokod.scio.entity.OpenQuestion;
import com.nordokod.scio.process.QualifyMethod;

import java.util.Objects;

public class OpenQuestionFragment extends Fragment implements BasicFragment{

    private Context context;
    private OpenQuestion openQuestion;
    private StudyGuideActivity studyGuideActivity;

    private Animation starEarnedAnimation;

    public OpenQuestionFragment() {}

    public OpenQuestionFragment(Context context, OpenQuestion openQuestion, StudyGuideActivity studyGuideActivity) {
        this.context = context;
        this.openQuestion = openQuestion;
        this.studyGuideActivity = studyGuideActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_question, container, false);

        initComponents(view);
        initListeners();
        initAnimations();

        return  view;
    }

    @Override
    public void initComponents(View view) {
        TV_Question                     = view.findViewById(R.id.OQFragment_TV_Question);

        IV_Star_1                       = view.findViewById(R.id.OQFragment_IV_Star_1);
        IV_Star_2                       = view.findViewById(R.id.OQFragment_IV_Star_2);
        IV_Star_3                       = view.findViewById(R.id.OQFragment_IV_Star_3);

        ET_Answer                       = view.findViewById(R.id.OQFragment_ET_Answer);

        CL_Correct_Answer               = view.findViewById(R.id.OQFragment_CL_Correct_Answer);
        TV_Correct_Answer               = view.findViewById(R.id.OQFragment_TV_Correct_Answer);
    }

    @Override
    public void initListeners() {
        // Empty
    }

    @Override
    public void initAnimations() {
        starEarnedAnimation = AnimationUtils.loadAnimation(context, R.anim.star_earned);
    }

    @Override
    public void onStart() {
        super.onStart();

        TV_Question.setText(openQuestion.getQuestion());
        TV_Correct_Answer.setText(openQuestion.getAnswer());
    }

    private void showCorrectAnswer() {
        CL_Correct_Answer.setVisibility(View.VISIBLE);
    }

    protected void onAnswerQuestion() {
        int starsEarned = QualifyMethod.getStars(openQuestion.getAnswer(), Objects.requireNonNull(ET_Answer.getText()).toString());

        if (starsEarned > 0) QuestionUtils.fillStarsEarned(context, starsEarned, IV_Star_1, IV_Star_2, IV_Star_3, starEarnedAnimation);
        showCorrectAnswer();

        if (studyGuideActivity != null) studyGuideActivity.updateStarsEarnedAmount(starsEarned);
    }

    ////////////////////////////////////////////////////////////////////////// Objects from the view
    private AppCompatImageView IV_Star_1;
    private AppCompatImageView IV_Star_2;
    private AppCompatImageView IV_Star_3;
    private AppCompatTextView TV_Question;
    private AppCompatEditText ET_Answer;
    private ConstraintLayout CL_Correct_Answer;
    private AppCompatTextView TV_Correct_Answer;
}
