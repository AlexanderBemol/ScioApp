package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.MainController;
import com.nordokod.scio.entity.Guide;

public class NewOpenAnswerQuestionFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private MainController mainController;
    private Guide guide;

    private AppCompatEditText ET_Question, ET_Answer;
    private AppCompatButton BTN_Cancel, BTN_Create;

    private Animation press;

    public NewOpenAnswerQuestionFragment() { }

    @SuppressLint("ValidFragment")
    public NewOpenAnswerQuestionFragment(Context context, MainController mainController, Guide guide) {
        this.context = context;
        this.mainController = mainController;
        this.guide = guide;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_open_answer_question, container, false);

        initComponents(view);
        initListeners();
        initAnimations();

        return view;
    }

    @Override
    public void initComponents(View view) {
        ET_Question = view.findViewById(R.id.FNewOpenQuestion_ET_Question);
        ET_Answer   = view.findViewById(R.id.FNewOpenQuestion_ET_Answer);

        BTN_Cancel  = view.findViewById(R.id.FNewOpenQuestion_BTN_Cancel);
        BTN_Create  = view.findViewById(R.id.FNewOpenQuestion_BTN_Create);
    }

    @Override
    public void initListeners() {
        BTN_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Cancel.startAnimation(press);
                mainController.onCloseFragment("New Open Answer");
            }
        });

        BTN_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Create.startAnimation(press);
                mainController.onSaveOpenAnswerQuestion(guide, ET_Question.getText().toString(), ET_Answer.getText().toString());
            }
        });
    }

    private void initAnimations(){
        press = AnimationUtils.loadAnimation(context, R.anim.press);
    }
}
