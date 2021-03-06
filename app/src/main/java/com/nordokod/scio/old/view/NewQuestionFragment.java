package com.nordokod.scio.old.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.appcompat.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;
import com.nordokod.scio.old.entity.Guide;

public class NewQuestionFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private MainActivity activity;
    private Guide guide;

    private AppCompatImageView IV_Multiple_Choice, IV_Open_Answer, IV_True_False;

    public NewQuestionFragment() { }

    @SuppressLint("ValidFragment")
    public NewQuestionFragment(Context context, MainActivity activity, Guide guide) {
        this.context = context;
        this.activity = activity;
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
        IV_Multiple_Choice.setOnClickListener(v -> {
            activity.onCloseFragment("New Question");
            activity.showNewMultipleChoiceQuestionDialog(guide);
        });

        IV_True_False.setOnClickListener(v -> {
            activity.onCloseFragment("New Question");
            activity.showNewTrueFalseQuestionDialog(guide);
        });

        IV_Open_Answer.setOnClickListener(v -> {
            activity.onCloseFragment("New Question");
            activity.showNewOpenAnswerQuestionDialog(guide);
        });
    }
}
