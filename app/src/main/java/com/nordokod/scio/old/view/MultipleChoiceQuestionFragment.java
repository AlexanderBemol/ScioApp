package com.nordokod.scio.old.view;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.old.constants.QuestionUtils;
import com.nordokod.scio.old.entity.MultipleChoiceQuestion;
import com.nordokod.scio.old.model.StarsHistory;

import javax.annotation.Nullable;

public class MultipleChoiceQuestionFragment extends Fragment implements BasicFragment{
    private Context context;
    private MultipleChoiceQuestion multipleChoiceQuestion;
    private StudyGuideActivity studyGuideActivity;
    private Animation starEarnedAnimation;

    private boolean isFirstAnswerSelected   = false;
    private boolean isSecondAnswerSelected  = false;
    private boolean isThirdAnswerSelected   = false;
    private boolean isFourthAnswerSelected  = false;
    private boolean isAnswered = false;

    private int correctAnswersByUser = 0;
    private int totalCorrectAnswers = 0;
    private int incorrectAnswersByUser = 0;

    private AppCompatTextView[] answerCardsArray;

    public MultipleChoiceQuestionFragment() {}

    MultipleChoiceQuestionFragment(Context context, MultipleChoiceQuestion multipleChoiceQuestion, @Nullable StudyGuideActivity studyGuideActivity) {
        this.context = context;
        this.multipleChoiceQuestion = multipleChoiceQuestion;
        this.studyGuideActivity = studyGuideActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multiple_choice_question, container, false);

        initComponents(view);
        initListeners();
        initVariables();
        initAnimations();

        return view;
    }

    @Override
    public void initComponents(View view) {
        TV_Question         = view.findViewById(R.id.MCFragment_TV_Question);

        IV_Star_1           = view.findViewById(R.id.MCFragment_IV_Star_1);
        IV_Star_2           = view.findViewById(R.id.MCFragment_IV_Star_2);
        IV_Star_3           = view.findViewById(R.id.MCFragment_IV_Star_3);

        TV_First_Answer     = view.findViewById(R.id.MCFragment_TV_First_Answer);
        TV_Second_Answer    = view.findViewById(R.id.MCFragment_TV_Second_Answer);
        TV_Third_Answer     = view.findViewById(R.id.MCFragment_TV_Third_Answer);
        TV_Fourth_Answer    = view.findViewById(R.id.MCFragment_TV_Fourth_Answer);
    }

    @Override
    public void initListeners() {
        TV_First_Answer.setOnClickListener(v -> {
            if (!isAnswered) {
                onAnswerSelectedOrUnselected(!isFirstAnswerSelected, TV_First_Answer);
                isFirstAnswerSelected = !isFirstAnswerSelected;
            }
        });

        TV_Second_Answer.setOnClickListener(v -> {
            if (!isAnswered) {
                onAnswerSelectedOrUnselected(!isSecondAnswerSelected, TV_Second_Answer);
                isSecondAnswerSelected = !isSecondAnswerSelected;
            }
        });

        TV_Third_Answer.setOnClickListener(v -> {
            if (!isAnswered) {
                onAnswerSelectedOrUnselected(!isThirdAnswerSelected, TV_Third_Answer);
                isThirdAnswerSelected = !isThirdAnswerSelected;
            }
        });

        TV_Fourth_Answer.setOnClickListener(v -> {
            if (!isAnswered) {
                onAnswerSelectedOrUnselected(!isFourthAnswerSelected, TV_Fourth_Answer);
                isFourthAnswerSelected = !isFourthAnswerSelected;
            }
        });
    }

    @Override
    public void initVariables() {
        answerCardsArray = new AppCompatTextView[]{TV_First_Answer, TV_Second_Answer, TV_Third_Answer, TV_Fourth_Answer};
    }

    @Override
    public void initAnimations() {
        starEarnedAnimation = AnimationUtils.loadAnimation(context, R.anim.star_earned);
    }

    @Override
    public void onStart() {
        super.onStart();

        TV_Question.setText(multipleChoiceQuestion.getQuestion());

        setCardsWhitItsAnswer();
    }

    private void setCardsWhitItsAnswer() {
        for (int i = 0; i < 4; i++) {
            if (i < multipleChoiceQuestion.getAnswers().size()) {
                answerCardsArray[i].setText(multipleChoiceQuestion.getAnswers().get(i).getAnswer());
                if (multipleChoiceQuestion.getAnswers().get(i).isCorrect()) totalCorrectAnswers++;
            } else
                answerCardsArray[i].setVisibility(View.GONE);
        }
    }

    /**
     * Cambiar el color de la card en función de si se seleccionó o se deseleccionó.
     *
     * @param isAnswerSelected      -> {boolean} indica si la card se seleccionó o se deseleccionó.
     * @param TV_Answer_Selected    -> {AppCompatTextView} card con la que se esta interactuando.
     */
    private void onAnswerSelectedOrUnselected(boolean isAnswerSelected, AppCompatTextView TV_Answer_Selected) {
        if (isAnswerSelected) {
            TV_Answer_Selected.setBackgroundDrawable(context.getDrawable(R.drawable.background_selected_answer));
            TV_Answer_Selected.setTextAppearance(context, R.style.cardSelected);
        } else {
            TV_Answer_Selected.setBackgroundDrawable(context.getDrawable(R.drawable.background_card_round));
            TV_Answer_Selected.setTextAppearance(context, R.style.answerCard);
        }
    }

    /**
     * Metodo para calificar la pregunta y mostrar la cantidad de estrellas ganadas.
     */
    void onAnswerQuestion() {
        verifyAnswer(TV_First_Answer,   0, isFirstAnswerSelected);
        verifyAnswer(TV_Second_Answer,  1, isSecondAnswerSelected);
        verifyAnswer(TV_Third_Answer,   2, isThirdAnswerSelected);
        verifyAnswer(TV_Fourth_Answer,  3, isFourthAnswerSelected);

        QuestionUtils.fillStarsEarned(context, getAmountOfStarsEarned(), IV_Star_1, IV_Star_2, IV_Star_3, starEarnedAnimation);

        // En caso de estar en Modo Estudio
        if (studyGuideActivity != null) studyGuideActivity.updateStarsEarnedAmount(getAmountOfStarsEarned());

        isAnswered = true;

        //insertar en log
        StarsHistory starsHistory = new StarsHistory(context);
        starsHistory.createStarHistory(multipleChoiceQuestion.getGuide(),multipleChoiceQuestion.getId(),getAmountOfStarsEarned(),1);
    }

    /**
     * Verificar si las opciones que el usuario seleccionó y no seleccionó son correctas o incorrectas.
     * Mostrando visualmente si se equivoocó o no.
     *
     * @param TV_Answer_Selected    -> {AppCompatTextView} card seleccionada por el usuario.
     * @param indexAnswerSelected   -> {int} index para acceder a la respuesta dentro de la lista.
     * @param isAnswerSelected      -> {boolean} indica si la card fue seleccionada por el usuario.
     */
    private void verifyAnswer(AppCompatTextView TV_Answer_Selected, int indexAnswerSelected, boolean isAnswerSelected) {
        if (indexAnswerSelected < multipleChoiceQuestion.getAnswers().size()) {
            if (multipleChoiceQuestion.getAnswers().get(indexAnswerSelected).isCorrect()) {
                if (isAnswerSelected) {
                    QuestionUtils.showCorrectAnswerCard(context, TV_Answer_Selected);
                    correctAnswersByUser++;
                } else {
                    QuestionUtils.showCorrectAnswerCard(context, TV_Answer_Selected);
                }
            } else {
                if (isAnswerSelected) {
                    QuestionUtils.showIncorrectAnswerCard(context, TV_Answer_Selected);
                    incorrectAnswersByUser++;
                }
            }
        }
    }

    /**
     * Obtener la cantidad de estrellas ganadas para las preguntas de opcion multiple segun:
     *
     * correctAnswersByUser -> Cantidad de respuestas correctas por el usuario.
     * totalCorrectAnswers  -> Cantidad total de respuestas correctas que tiene la pregunta.
     *
     * @author Alexis Ozaeta
     * @return -> Cantidad de estrellas ganadas.
     */
    private int getAmountOfStarsEarned() {
        return (correctAnswersByUser == 0)
                ? 0
                : Math.round(((float) correctAnswersByUser * (float) 3) / (float) totalCorrectAnswers) - incorrectAnswersByUser;
    }

    ////////////////////////////////////////////////////////////////////////// Objects from the view
    private AppCompatImageView IV_Star_1;
    private AppCompatImageView IV_Star_2;
    private AppCompatImageView IV_Star_3;
    private AppCompatTextView TV_Question;
    private AppCompatTextView TV_First_Answer;
    private AppCompatTextView TV_Second_Answer;
    private AppCompatTextView TV_Third_Answer;
    private AppCompatTextView TV_Fourth_Answer;
}