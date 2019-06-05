package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.MainController;
import com.nordokod.scio.entity.Guide;

public class NewQuestionFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private MainController mainController;
    private Guide guide;

    private AppCompatImageView IV_Multiple_Choice, IV_Open_Answer, IV_True_False;

    public NewQuestionFragment() { }

    @SuppressLint("ValidFragment")
    public NewQuestionFragment(Context context, MainController mainController, Guide guide) {
        this.context = context;
        this.mainController = mainController;
        this.guide = guide;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_question, container, false);

        initComponents(view);
        initListeners();

        return view;
    }

    @Override
    public void initComponents(View view) {
        IV_Multiple_Choice  = view.findViewById(R.id.FNewQuestion_IV_Multiple);
        IV_True_False       = view.findViewById(R.id.FNewQuestion_IV_TrueFalse);
        IV_Open_Answer      = view.findViewById(R.id.FNewQuestion_IV_Open);
    }

    @Override
    public void initListeners() {
        IV_Multiple_Choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.onCloseFragment("New Question");
                mainController.onNewMultipleChoiceQuestionDialog(guide);
            }
        });

        IV_True_False.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.onCloseFragment("New Question");
                mainController.onNewTrueFalseQuestionDialog(guide);
            }
        });

        IV_Open_Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.onCloseFragment("New Question");
                mainController.onNewOpenAnswerQuestionDialog(guide);
            }
        });
    }
}
