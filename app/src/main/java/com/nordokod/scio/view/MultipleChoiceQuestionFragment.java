package com.nordokod.scio.view;

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
import com.nordokod.scio.constants.QuestionUtils;
import com.nordokod.scio.entity.MultipleChoiceQuestion;

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

    private int correctAnswersByUser = 0;
    private int totalCorrectAnswers = 0;
    private int incorrectAnswersByUser = 0;

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
        initAnimations();

        return view;
    }

    @Override
    public void initComponents(View view) {
        TV_Question                     = view.findViewById(R.id.MCFragment_TV_Question);

        IV_Star_1                       = view.findViewById(R.id.MCFragment_IV_Star_1);
        IV_Star_2                       = view.findViewById(R.id.MCFragment_IV_Star_2);
        IV_Star_3                       = view.findViewById(R.id.MCFragment_IV_Star_3);

        TV_First_Answer                 = view.findViewById(R.id.MCFragment_TV_First_Answer);
        TV_Second_Answer                = view.findViewById(R.id.MCFragment_TV_Second_Answer);
        TV_Third_Answer                 = view.findViewById(R.id.MCFragment_TV_Third_Answer);
        TV_Fourth_Answer                = view.findViewById(R.id.MCFragment_TV_Fourth_Answer);
    }

    @Override
    public void initListeners() {
        TV_First_Answer.setOnClickListener(v -> {
            onAnswerSelectedOrUnselected(!isFirstAnswerSelected, TV_First_Answer);
            isFirstAnswerSelected = !isFirstAnswerSelected;
        });

        TV_Second_Answer.setOnClickListener(v -> {
            onAnswerSelectedOrUnselected(!isSecondAnswerSelected, TV_Second_Answer);
            isSecondAnswerSelected = !isSecondAnswerSelected;
        });

        TV_Third_Answer.setOnClickListener(v -> {
            onAnswerSelectedOrUnselected(!isThirdAnswerSelected, TV_Third_Answer);
            isThirdAnswerSelected = !isThirdAnswerSelected;
        });

        TV_Fourth_Answer.setOnClickListener(v -> {
            onAnswerSelectedOrUnselected(!isFourthAnswerSelected, TV_Fourth_Answer);
            isFourthAnswerSelected = !isFourthAnswerSelected;
        });
    }

    @Override
    public void initAnimations() {
        starEarnedAnimation = AnimationUtils.loadAnimation(context, R.anim.star_earned);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        TV_Question.setText(multipleChoiceQuestion.getQuestion());

        String s = multipleChoiceQuestion.getAnswers().get(0).getAnswer();

        TV_First_Answer.setText(multipleChoiceQuestion.getAnswers().get(0).getAnswer());
        if (multipleChoiceQuestion.getAnswers().get(0).isCorrect()) totalCorrectAnswers++;
        TV_Second_Answer.setText(multipleChoiceQuestion.getAnswers().get(1).getAnswer());
        if (multipleChoiceQuestion.getAnswers().get(1).isCorrect()) totalCorrectAnswers++;
        TV_Third_Answer.setText(multipleChoiceQuestion.getAnswers().get(2).getAnswer());
        if (multipleChoiceQuestion.getAnswers().get(2).isCorrect()) totalCorrectAnswers++;
        TV_Fourth_Answer.setText(multipleChoiceQuestion.getAnswers().get(3).getAnswer());
        if (multipleChoiceQuestion.getAnswers().get(3).isCorrect()) totalCorrectAnswers++;
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
        if (multipleChoiceQuestion.getAnswers().get(indexAnswerSelected).isCorrect()) {
            if (isAnswerSelected) {
                QuestionUtils.showCorrectAnswerCard(context, TV_Answer_Selected);
                correctAnswersByUser++;
            } else {
                QuestionUtils.showCorrectAnswerCard(context, TV_Answer_Selected);
                incorrectAnswersByUser++;
            }
        } else {
            if (isAnswerSelected) {
                QuestionUtils.showIncorrectAnswerCard(context, TV_Answer_Selected);
                incorrectAnswersByUser++;
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
        return Math.round(((float) correctAnswersByUser * (float) 3) / (float) totalCorrectAnswers) - incorrectAnswersByUser;
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