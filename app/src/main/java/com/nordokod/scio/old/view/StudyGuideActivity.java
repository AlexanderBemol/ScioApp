package com.nordokod.scio.old.view;

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
import com.nordokod.scio.old.constants.KindOfQuestion;
import com.nordokod.scio.old.entity.Guide;
import com.nordokod.scio.old.entity.MultipleChoiceQuestion;
import com.nordokod.scio.old.entity.OpenQuestion;
import com.nordokod.scio.old.entity.Question;
import com.nordokod.scio.old.entity.TrueFalseQuestion;
import com.nordokod.scio.old.process.UserMessage;

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

    private UserMessage userMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            amountOfQuestionsSkipped++;

            if (currentQuestion < questionList.size() - 1) showNextQuestion();
            else onAnswerLastQuestionShowAndHideButtonsLogic();

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
            showStats(amountOfStarsEarned, amountOfQuestionsSkipped);
        });

        IV_Close.setOnClickListener(v -> {
            IV_Close.startAnimation(buttonPressedAnimation);
            showStats(amountOfStarsEarned, amountOfQuestionsSkipped);
        });
    }

    @Override
    public void initAnimations() {
        buttonPressedAnimation = AnimationUtils.loadAnimation(this, R.anim.press);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        com.nordokod.scio.old.model.Question questionModel = new com.nordokod.scio.old.model.Question();
        Guide guide = (Guide) Objects.requireNonNull(intent.getSerializableExtra("GUIDE"));
        questionList = questionModel.getQuestionsFromGuide(guide);

        totalOfQuestions = questionList.size();

        TV_Question_Number.setText(getResources().getString(R.string.current_question_total_questions, (currentQuestion + 1), totalOfQuestions));

        switch (questionList.get(currentQuestion).getKindOfQuestion()) {
            case 1: showMultipleChoiceQuestion((MultipleChoiceQuestion) questionList.get(currentQuestion)); break;
            case 2: showTrueFalseQuestion((TrueFalseQuestion) questionList.get(currentQuestion)); break;
            case 3: showOpenAnswerQuestion((OpenQuestion) questionList.get(currentQuestion)); break;
        }

        setQuestionShowAndHideButtonsLogic(kindOfCurrentQuestion);
    }

    private void showNextQuestion() {
        currentQuestion++;

        switch (questionList.get(currentQuestion).getKindOfQuestion()) {
            case 1: showMultipleChoiceQuestion((MultipleChoiceQuestion) questionList.get(currentQuestion)); break;
            case 2: showTrueFalseQuestion((TrueFalseQuestion) questionList.get(currentQuestion)); break;
            case 3: showOpenAnswerQuestion((OpenQuestion) questionList.get(currentQuestion)); break;
        }

        TV_Question_Number.setText(getResources().getString(R.string.current_question_total_questions, (currentQuestion + 1), totalOfQuestions));

        setQuestionShowAndHideButtonsLogic(kindOfCurrentQuestion);
    }

    private void showMultipleChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion) {
        multipleChoiceQuestionFragment = new MultipleChoiceQuestionFragment(this, multipleChoiceQuestion, this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.StudyGuide_FL_Question_Container, multipleChoiceQuestionFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        kindOfCurrentQuestion = KindOfQuestion.MULTIPLE_CHOICE.getCode();
    }

    private void showTrueFalseQuestion(TrueFalseQuestion trueFalseQuestion) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.StudyGuide_FL_Question_Container, new TrueFalseQuestionFragment(this, trueFalseQuestion, this));
        transaction.addToBackStack(null);
        transaction.commit();

        kindOfCurrentQuestion = KindOfQuestion.TRUE_FALSE.getCode();
    }

    private void showOpenAnswerQuestion(OpenQuestion openQuestion) {
        openQuestionFragment = new OpenQuestionFragment(this, openQuestion, this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.StudyGuide_FL_Question_Container, openQuestionFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        kindOfCurrentQuestion = KindOfQuestion.OPEN.getCode();
    }

    protected void closeAndGoToMainActivity() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        this.finish();
    }

    /**
     * Update global stars earned amount.
     *
     * @param starsEarned -> (int) Amount of stars earned in a question.
     */
    protected void updateStarsEarnedAmount(int starsEarned) {
        amountOfStarsEarned += starsEarned;
        TV_Stars_Amount.setText(String.valueOf(amountOfStarsEarned));

        if (currentQuestion == questionList.size() - 1)
            onAnswerLastQuestionShowAndHideButtonsLogic();
        else
            onAnswerQuestionShowAndHideButtonsLogic();
    }

    /**
     * Open the dialog fragment to show the statistics of this study session.
     *
     * @param starsEarned       -> (int) Amount of stars earned in this study session.
     * @param questionsSkipped  -> (int) Amount of questions skipped in this study session.
     */
    private void showStats(int starsEarned, int questionsSkipped) {
        dialogFragment = new StudyGuideStatsFragment(this, this, starsEarned, questionsSkipped);
        dialogFragment.show(getSupportFragmentManager(), "Study Stats");
    }

    /**
     * Show an message error if is necessary.
     */
    private void showError() {

    }

    /**
     * Logic to show and hide buttons when set a question.
     *
     * @param kindOfQuestion
     */
    private void  setQuestionShowAndHideButtonsLogic(int kindOfQuestion) {
        switch (kindOfQuestion) {
            case 1: case 3:     // Multiple Choice and Open Question
                BTN_Skip.setVisibility(View.VISIBLE);
                BTN_Next.setVisibility(View.GONE);
                BTN_Answer.setVisibility(View.VISIBLE);
                BTN_Finish.setVisibility(View.GONE);
                break;
            case 2:             // True False Question
                BTN_Skip.setVisibility(View.VISIBLE);
                BTN_Next.setVisibility(View.GONE);
                BTN_Answer.setVisibility(View.GONE);
                BTN_Finish.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * Logic to show and hide buttons when a questions is answered.
     * Is the same logic for all kind of questions.
     */
    private void onAnswerQuestionShowAndHideButtonsLogic() {
        BTN_Skip.setVisibility(View.GONE);
        BTN_Next.setVisibility(View.VISIBLE);
        BTN_Answer.setVisibility(View.GONE);
        BTN_Finish.setVisibility(View.GONE);
    }

    /**
     * Logic to show and hide buttons when the last question is answered.
     * Is the same logic for all kind of questions.
     */
    private void onAnswerLastQuestionShowAndHideButtonsLogic() {
        BTN_Skip.setVisibility(View.GONE);
        BTN_Next.setVisibility(View.GONE);
        BTN_Answer.setVisibility(View.GONE);
        BTN_Finish.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        // Empty
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
