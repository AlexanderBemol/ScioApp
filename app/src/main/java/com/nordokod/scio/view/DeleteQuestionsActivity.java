package com.nordokod.scio.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.nordokod.scio.R;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.Question;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DeleteQuestionsActivity extends AppCompatActivity implements BasicActivity {
    private Animation button_pressed_animation;
    private List<Question> questionList;
    private List<Integer> selectedQuestionIndexList;
    private com.nordokod.scio.model.Question questionModel;
    private AppCompatDialog dialog;
    private Guide guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.NightTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_questions);

        initComponents();
        initListeners();
        initAnimations();
    }

    @Override
    public void initComponents() {
        BTN_Cancel = findViewById(R.id.ADeleteQ_BTN_Cancel);
        BTN_Delete = findViewById(R.id.ADeleteQ_BTN_Delete);

        IV_Close = findViewById(R.id.ADeleteQ_IV_Close);

        RV_Questions = findViewById(R.id.ADeleteQ_RV_Questions);
    }

    @Override
    public void initListeners() {
        IV_Close.setOnClickListener(v -> {
            IV_Close.startAnimation(button_pressed_animation);
            closeAndGoToMainActivity();
        });

        BTN_Cancel.setOnClickListener(v -> {
            BTN_Cancel.startAnimation(button_pressed_animation);
            closeAndGoToMainActivity();
        });

        BTN_Delete.setOnClickListener(v -> {
            BTN_Delete.startAnimation(button_pressed_animation);
            selectedQuestionIndexList = ((QuestionsRecyclerViewAdapter) Objects.requireNonNull(RV_Questions.getAdapter())).getSelectedQuestionsList();

            dialog = new AppCompatDialog(this);
            showLoadingDialog(dialog, this);
        });
    }

    @Override
    public void initAnimations() {
        button_pressed_animation = AnimationUtils.loadAnimation(this, R.anim.press);
    }

    @Override
    protected void onStart() {
        super.onStart();

        layoutManager = new LinearLayoutManager(this);

        RV_Questions.setLayoutManager(layoutManager);

        questionList = getQuestionList();
        adapter = new QuestionsRecyclerViewAdapter(this, this, questionList);

        RV_Questions.setAdapter(adapter);

        questionModel = new com.nordokod.scio.model.Question();
    }

    @Override
    public void onBackPressed() {

    }

    private void deleteQuestionsSelected() {
        HashMap<String, Object> questionMap = new HashMap<>();
        for (Question question : questionList) questionMap.put(String.valueOf(questionMap.size()), question);

        questionModel.deleteQuestions(guide, questionMap)
                .addOnSuccessListener(v -> closeLoadingDialog(dialog))
                .addOnFailureListener(this::showError);
    }

    private List<Question> getQuestionList() {
        Intent intent = getIntent();
        com.nordokod.scio.model.Question questionModel = new com.nordokod.scio.model.Question();
        guide = (Guide) Objects.requireNonNull(intent.getSerializableExtra("GUIDE"));

        return questionModel.getQuestionsFromGuide(guide);
    }

    private void toggleQuestionSelected(Integer questionIndexSelected) {
        if (!selectedQuestionIndexList.contains(questionIndexSelected))
            selectedQuestionIndexList.add(questionIndexSelected);
        else
            selectedQuestionIndexList.remove(questionIndexSelected);
    }

    private void closeAndGoToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showError(Exception e) {

    }

    ////////////////////////////////////////////////////////////////////////// Objects from the view
    private AppCompatButton BTN_Cancel;
    private AppCompatButton BTN_Delete;
    private AppCompatImageView IV_Close;
    private RecyclerView RV_Questions;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
}
