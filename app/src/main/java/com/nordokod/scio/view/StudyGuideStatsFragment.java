package com.nordokod.scio.view;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nordokod.scio.R;

public class StudyGuideStatsFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private StudyGuideActivity activity;
    private Animation button_pressed;

    private int amountOfStarsEarned = 0;
    private int amountOfQuestionsSkipped = 0;

    public StudyGuideStatsFragment() {}

    public StudyGuideStatsFragment(Context context, StudyGuideActivity activity, int amountOfStarsEarned, int amountOfQuestionsSkipped) {
        this.context = context;
        this.activity = activity;
        this.amountOfStarsEarned = amountOfStarsEarned;
        this.amountOfQuestionsSkipped = amountOfQuestionsSkipped;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_guide_stats, container, false);

        initComponents(view);
        initListeners();

        return view;
    }

    @Override
    public void initComponents(View view) {
        TV_Stars_Amount = view.findViewById(R.id.FStats_TV_Stars_Amount);
        TV_Skipped_Amount = view.findViewById(R.id.FStats_TV_Skipped_Amount);

        BTN_Finish = view.findViewById(R.id.FStats_BTN_Finish);
    }

    @Override
    public void initListeners() {
        BTN_Finish.setOnClickListener(v -> {
            BTN_Finish.setAnimation(button_pressed);
            goToFinishActivity();
        });
    }

    @Override
    public void initAnimations() {
        button_pressed = AnimationUtils.loadAnimation(context, R.anim.press);
    }

    @Override
    public void goToFinishActivity() {
        activity.closeAndGoToMainActivity();
    }

    @Override
    public void onStart() {
        super.onStart();

        TV_Stars_Amount.setText(String.valueOf(amountOfStarsEarned));
        TV_Skipped_Amount.setText(String.valueOf(amountOfQuestionsSkipped));
    }

    ////////////////////////////////////////////////////////////////////////// Objects from the view
    private AppCompatTextView TV_Stars_Amount;
    private AppCompatTextView TV_Skipped_Amount;
    private AppCompatButton BTN_Finish;
}
