package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.MainController;
import com.nordokod.scio.entity.Guide;

public class NewTrueFalseQuestionFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private MainController mainController;
    private Guide guide;

    private AppCompatEditText ET_Question;
    private AppCompatTextView TV_True_Answer, TV_False_Answer;
    private AppCompatButton BTN_Cancel, BTN_Create;
    private Animation press;

    private boolean answer;
    private int preview_answer;

    public NewTrueFalseQuestionFragment() { }

    @SuppressLint("ValidFragment")
    public NewTrueFalseQuestionFragment(Context context, MainController mainController, Guide guide) {
        this.context = context;
        this.mainController = mainController;
        this.guide = guide;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_true_false_question, container, false);

        initComponents(view);
        initListeners();
        initAnimations();

        return view;
    }

    @Override
    public void initComponents(View view) {
        ET_Question     = view.findViewById(R.id.FNewTFQuestion_ET_Question);

        TV_True_Answer  = view.findViewById(R.id.FNewTFQuestion_TV_True);
        TV_False_Answer = view.findViewById(R.id.FNewTFQuestion_TV_False);

        BTN_Cancel      = view.findViewById(R.id.FNewTFQuestion_BTN_Cancel);
        BTN_Create      = view.findViewById(R.id.FNewTFQuestion_BTN_Create);

        ET_Question.setText("");
    }

    @Override
    public void initListeners() {
        TV_True_Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = true;
                onChooseAnswer();
            }
        });

        TV_False_Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = false;
                onChooseAnswer();
            }
        });

        BTN_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Cancel.startAnimation(press);
                mainController.onCloseFragment("New TrueFalse");
            }
        });

        BTN_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Create.startAnimation(press);
                mainController.onSaveTrueFalseQuestion(guide, ET_Question.getText().toString(), answer);
            }
        });
    }

    private void initAnimations(){
        press = AnimationUtils.loadAnimation(context, R.anim.press);
    }

    @SuppressLint("ResourceType")
    private void onChooseAnswer() {
        if (answer && preview_answer != 1) {
            TV_True_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_selected_answer));
            TV_True_Answer.setTextAppearance(context, R.style.optionSelectedTextView);

            TV_False_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_card_round));
            TV_False_Answer.setTextAppearance(context, R.style.optionTextView);

            preview_answer = 1;
        } else if (!answer && preview_answer != 2) {
            TV_False_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_selected_answer));
            TV_False_Answer.setTextAppearance(context, R.style.optionSelectedTextView);

            TV_True_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_card_round));
            TV_True_Answer.setTextAppearance(context, R.style.optionTextView);

            preview_answer = 2;
        }

        TV_True_Answer.setPadding(
                (int) context.getResources().getDimension(R.dimen.option_textview_padding),
                (int) context.getResources().getDimension(R.dimen.option_textview_padding),
                (int) context.getResources().getDimension(R.dimen.option_textview_padding),
                (int) context.getResources().getDimension(R.dimen.option_textview_padding));

        TV_False_Answer.setPadding(
                (int) context.getResources().getDimension(R.dimen.option_textview_padding),
                (int) context.getResources().getDimension(R.dimen.option_textview_padding),
                (int) context.getResources().getDimension(R.dimen.option_textview_padding),
                (int) context.getResources().getDimension(R.dimen.option_textview_padding));
    }
}
