package com.nordokod.scio.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nordokod.scio.R;
import com.nordokod.scio.controller.MainController;
import com.nordokod.scio.entity.Guide;

public class NewMultipleChoiceQuestionFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private MainController mainController;
    private Guide guide;

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

        return view;
    }

    @Override
    public void initComponents(View view) {

    }

    @Override
    public void initListeners() {

    }
}
