package com.nordokod.scio.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.MainController;
import com.nordokod.scio.entity.Guide;

import java.util.Objects;

public class NewMultipleChoiceQuestionFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private MainController mainController;
    private Guide guide;

    private AppCompatEditText ET_Question, ET_Option_1, ET_Option_2, ET_Option_3, ET_Option_4;
    private AppCompatButton BTN_Cancel, BTN_Create;
    private SwitchCompat Switch_Option_1, Switch_Option_2, Switch_Option_3, Switch_Option_4;

    private Animation press;

    public NewMultipleChoiceQuestionFragment() { }

    @SuppressLint("ValidFragment")
    public NewMultipleChoiceQuestionFragment(Context context, MainController mainController, Guide guide) {
        this.context = context;
        this.mainController = mainController;
        this.guide = guide;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_multiple_choice_question, container, false);

        initComponents(view);
        initListeners();
        initAnimations();

        return view;
    }

    @Override
    public void initComponents(View view) {
        ET_Question     = view.findViewById(R.id.FNewMCQuestion_ET_Question);

        ET_Option_1     = view.findViewById(R.id.FNewMCQuestion_ET_Option_1);
        ET_Option_2     = view.findViewById(R.id.FNewMCQuestion_ET_Option_2);
        ET_Option_3     = view.findViewById(R.id.FNewMCQuestion_ET_Option_3);
        ET_Option_4     = view.findViewById(R.id.FNewMCQuestion_ET_Option_4);

        Switch_Option_1 = view.findViewById(R.id.FNewMCQuestion_Switch_Option_1);
        Switch_Option_2 = view.findViewById(R.id.FNewMCQuestion_Switch_Option_2);
        Switch_Option_3 = view.findViewById(R.id.FNewMCQuestion_Switch_Option_3);
        Switch_Option_4 = view.findViewById(R.id.FNewMCQuestion_Switch_Option_4);

        BTN_Cancel      = view.findViewById(R.id.FNewMCQuestion_BTN_Cancel);
        BTN_Create      = view.findViewById(R.id.FNewMCQuestion_BTN_Create);
    }

    @Override
    public void initListeners() {
        BTN_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Cancel.startAnimation(press);
                mainController.onCloseFragment("New Multiple Choice");
            }
        });

        BTN_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Create.startAnimation(press);
                mainController.onSaveMultipleChoiceQuestion(
                        guide,
                        Objects.requireNonNull(ET_Question.getText()).toString(),
                        Objects.requireNonNull(ET_Option_1.getText()).toString(),
                        Objects.requireNonNull(ET_Option_2.getText()).toString(),
                        Objects.requireNonNull(ET_Option_3.getText()).toString(),
                        Objects.requireNonNull(ET_Option_4.getText()).toString(),
                        Switch_Option_1.isChecked(),
                        Switch_Option_2.isChecked(),
                        Switch_Option_3.isChecked(),
                        Switch_Option_4.isChecked()
                );
            }
        });
    }

    private void initAnimations(){
        press = AnimationUtils.loadAnimation(context, R.anim.press);
    }
}
