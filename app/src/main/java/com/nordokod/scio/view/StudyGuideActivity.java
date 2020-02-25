package com.nordokod.scio.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.constants.KindOfQuestion;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.MultipleChoiceQuestion;
import com.nordokod.scio.entity.OpenQuestion;
import com.nordokod.scio.entity.Question;
import com.nordokod.scio.entity.TrueFalseQuestion;

import java.util.List;
import java.util.Objects;

public class StudyGuideActivity extends AppCompatActivity implements BasicActivity {
    private int amountOfStarsEarned = 0;
    private int amountOfQuestionsSkipped = 0;
    private int totalOfQuestions = 0; // Index value
    private int currentQuestion = 0; // Index value
    private int kindOfCurrentQuestion = 0;

    private MultipleChoiceQuestionFragment multipleChoiceQuestionFragment;
    private OpenQuestionFragment openQuestionFragment;
    private DialogFragment dialogFragment;

    private List<Question> questionList;

    private Animation buttonPressedAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.NightTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_guide);

        initComponents();
        initListeners();
        initAnimations();
    }

    @Override
    public void initComponents() {
        TV_Question_Number  = findViewById(R.id.StudyGuide_TV_Question_Number);

        TV_Stars_Amount     = findViewById(R.id.StudyGuide_TV_Stars_Amount);

        IV_Close            = findViewById(R.id.StudyGuide_IV_Close);

        BTN_Skip            = findViewById(R.id.StudyGuide_BTN_Skip);
        BTN_Next            = findViewById(R.id.StudyGuide_BTN_Next);
        BTN_Answer          = findViewById(R.id.StudyGuide_BTN_Answer);
        BTN_Finish          = findViewById(R.id.StudyGuide_BTN_Finish);
    }

    @Override
    public void initListeners() {
        BTN_Skip.setOnClickListener(v -> {
            BTN_Skip.startAnimation(buttonPressedAnimation);
            showNextQuestion();
            amountOfQuestionsSkipped++;
        });

        BTN_Next.setOnClickListener(v -> {
            BTN_Next.startAnimation(buttonPressedAnimation);
            showNextQuestion();
        });

        BTN_Answer.setOnClickListener(v -> {
            BTN_Answer.startAnimation(buttonPressedAnimation);

            if (kindOfCurrentQuestion == 1)
                multipleChoiceQuestionFragment.onAnswerQuestion();
            else if (kindOfCurrentQuestion == 3)
                openQuestionFragment.onAnswerQuestion();
        });

        BTN_Finish.setOnClickListener(v -> {
            BTN_Finish.setAnimation(buttonPressedAnimation);
            showStats();
        });

        IV_Close.setOnClickListener(v -> {
            IV_Close.startAnimation(buttonPressedAnimation);
            showStats();
        });
    }

    private void initAnimations() {
        buttonPressedAnimation = AnimationUtils.loadAnimation(this, R.anim.press);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        com.nordokod.scio.model.Question questionModel = new com.nordokod.scio.model.Question();
        Guide guide = (Guide) Objects.requireNonNull(intent.getSerializableExtra("GUIDE"));
        questionList = questionModel.getQuestionsFromGuide(guide);
/*
        questionList = new ArrayList<>();
        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion("1", "Question 1", 1);
        multipleChoiceQuestion.addAnswer("Answer 1", false);
        multipleChoiceQuestion.addAnswer("Answer 2", false);
        multipleChoiceQuestion.addAnswer("Answer 3", true);
        multipleChoiceQuestion.addAnswer("Answer 4", false);
        questionList.add(multipleChoiceQuestion);
        TrueFalseQuestion trueFalseQuestion = new TrueFalseQuestion("2", "Question 2", 2, false);
        questionList.add(trueFalseQuestion);
        OpenQuestion openQuestion = new OpenQuestion("3", "Question 3", 3, "Answer");
        questionList.add(openQuestion);
*/
        totalOfQuestions = questionList.size();

        switch (questionList.get(currentQuestion).getKindOfQuestion()) {
            case 1: showMultipleChoiceQuestion((MultipleChoiceQuestion) questionList.get(currentQuestion)); break;
            case 2: showTrueFalseQuestion((TrueFalseQuestion) questionList.get(currentQuestion)); break;
            case 3: showOpenAnswerQuestion((OpenQuestion) questionList.get(currentQuestion)); break;
        }
    }

    private void showNextQuestion() {
        currentQuestion++;

        switch (questionList.get(currentQuestion).getKindOfQuestion()) {
            case 1: showMultipleChoiceQuestion((MultipleChoiceQuestion) questionList.get(currentQuestion)); break;
            case 2: showTrueFalseQuestion((TrueFalseQuestion) questionList.get(currentQuestion)); break;
            case 3: showOpenAnswerQuestion((OpenQuestion) questionList.get(currentQuestion)); break;
        }

        TV_Question_Number.setText(getResources().getString(R.string.current_question_total_questions, (currentQuestion + 1), totalOfQuestions));

        if (currentQuestion == questionList.size())
            BTN_Next.setActivated(false);
    }

    private void showMultipleChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion) {
        multipleChoiceQuestionFragment = new MultipleChoiceQuestionFragment(this, multipleChoiceQuestion, this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.StudyGuide_FL_Question_Container, multipleChoiceQuestionFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        BTN_Answer.setVisibility(View.VISIBLE);
        BTN_Next.setVisibility(View.GONE);

        kindOfCurrentQuestion = KindOfQuestion.MULTIPLE_CHOICE.getCode();
    }

    private void showTrueFalseQuestion(TrueFalseQuestion trueFalseQuestion) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.StudyGuide_FL_Question_Container, new TrueFalseQuestionFragment(this, trueFalseQuestion, this));
        transaction.addToBackStack(null);
        transaction.commit();

        BTN_Answer.setVisibility(View.GONE);
        BTN_Next.setVisibility(View.VISIBLE);
        BTN_Next.setActivated(false);

        kindOfCurrentQuestion = KindOfQuestion.TRUE_FALSE.getCode();
    }

    private void showOpenAnswerQuestion(OpenQuestion openQuestion) {
        openQuestionFragment = new OpenQuestionFragment(this, openQuestion, this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.StudyGuide_FL_Question_Container, openQuestionFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        BTN_Answer.setVisibility(View.VISIBLE);
        BTN_Next.setVisibility(View.GONE);

        kindOfCurrentQuestion = KindOfQuestion.OPEN.getCode();
    }

    protected void closeAndGoToMainActivity() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        this.finish();
    }

    protected void updateStarsEarnedAmount(int amountOfStarsEarned) {
        this.amountOfStarsEarned += amountOfStarsEarned;
        TV_Stars_Amount.setText(String.valueOf(this.amountOfStarsEarned));

        BTN_Answer.setVisibility(View.GONE);
        BTN_Next.setVisibility(View.VISIBLE);
        BTN_Next.setActivated(true);

        if (currentQuestion == questionList.size()) showStats();
    }

    private void showStats() {
        dialogFragment = new StudyGuideStatsFragment(this, this, amountOfStarsEarned, amountOfQuestionsSkipped);
        dialogFragment.show(getSupportFragmentManager(), "Study Stats");
    }

    private void showError() {

    }

    ////////////////////////////////////////////////////////////////////////// Objects from the view
    private AppCompatTextView TV_Question_Number;
    private AppCompatTextView TV_Stars_Amount;
    private AppCompatImageView IV_Close;
    private AppCompatButton BTN_Skip;
    private AppCompatButton BTN_Next;
    private AppCompatButton BTN_Answer;
    private AppCompatButton BTN_Finish;
}
