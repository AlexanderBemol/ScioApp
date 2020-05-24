package com.nordokod.scio.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.constants.QuestionUtils;
import com.nordokod.scio.constants.Utilities;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.MultipleChoiceQuestion;
import com.nordokod.scio.model.StarsHistory;

import java.util.Objects;

public class MultipleChoiceQuestionDialog implements BasicDialog {
    private Context context;
    private Dialog dialog;
    private MultipleChoiceQuestion multipleChoiceQuestion;
    private Guide guide;
    private Animation starEarnedAnimation;
    private Animation buttonPressedAnimation;

    private boolean isFirstAnswerSelected = false;
    private boolean isSecondAnswerSelected = false;
    private boolean isThirdAnswerSelected = false;
    private boolean isFourthAnswerSelected = false;
    private boolean isAnswered = false;

    private int correctAnswersByUser = 0;
    private int totalCorrectAnswers = 0;
    private int incorrectAnswersByUser = 0;

    private AppCompatTextView[] answerCardsArray;

    public MultipleChoiceQuestionDialog(Context context) {
        this.context = context;
        initDialog();
        initComponents();
        initListeners();
        initAnimations();
        initVariables();
    }

    @Override
    public void initDialog() {
        dialog = new Dialog(context, R.style.DefaultTheme);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_multiple_choice_question);
    }

    @Override
    public void initComponents() {
        IV_Star_1 = dialog.findViewById(R.id.MCQuestion_IV_Star_1);
        IV_Star_2 = dialog.findViewById(R.id.MCQuestion_IV_Star_2);
        IV_Star_3 = dialog.findViewById(R.id.MCQuestion_IV_Star_3);

        IV_Close = dialog.findViewById(R.id.MCQuestion_IV_Close);

        TV_Category = dialog.findViewById(R.id.MCQuestion_TV_Category);
        TV_Topic = dialog.findViewById(R.id.MCQuestion_TV_Topic);
        TV_Question = dialog.findViewById(R.id.MCQuestion_TV_Question);

        TV_First_Answer = dialog.findViewById(R.id.MCQuestion_TV_First_Answer);
        TV_Second_Answer = dialog.findViewById(R.id.MCQuestion_TV_Second_Answer);
        TV_Third_Answer = dialog.findViewById(R.id.MCQuestion_TV_Third_Answer);
        TV_Fourth_Answer = dialog.findViewById(R.id.MCQuestion_TV_Fourth_Answer);

        BTN_Skip = dialog.findViewById(R.id.MCQuestion_BTN_Skip);
        BTN_Answer = dialog.findViewById(R.id.MCQuestion_BTN_Answer);
        BTN_Close = dialog.findViewById(R.id.MCQuestion_BTN_Close);
    }

    @Override
    public void initListeners() {
        IV_Close.setOnClickListener(v -> dialog.dismiss());

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

        BTN_Skip.setOnClickListener(v -> {
            BTN_Skip.startAnimation(buttonPressedAnimation);
            dialog.dismiss();
        });

        BTN_Answer.setOnClickListener(v -> {
            BTN_Answer.startAnimation(buttonPressedAnimation);
            onAnswerQuestion();
            BTN_Skip.setVisibility(View.GONE);
            BTN_Answer.setVisibility(View.GONE);
            BTN_Close.setVisibility(View.VISIBLE);
        });

        BTN_Close.setOnClickListener(v -> {
            BTN_Close.startAnimation(buttonPressedAnimation);
            dialog.dismiss();
        });
    }

    public void initAnimations() {
        starEarnedAnimation = AnimationUtils.loadAnimation(context, R.anim.star_earned);
        buttonPressedAnimation = AnimationUtils.loadAnimation(context, R.anim.press);
    }

    private void initVariables() {
        answerCardsArray = new AppCompatTextView[]{TV_First_Answer, TV_Second_Answer, TV_Third_Answer, TV_Fourth_Answer};
    }

    @Override
    public void showDialog() {
        if (!dialog.isShowing()) {
            TV_Category.setText(Utilities.getCategoryStringResource(guide.getCategory()));
            TV_Topic.setText(guide.getTopic());
            TV_Question.setText(multipleChoiceQuestion.getQuestion());

            setCardsWhitItsAnswer();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_TOAST);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_PHONE);
            }

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();

            layoutParams.copyFrom(window.getAttributes());

            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

            window.setAttributes(layoutParams);

            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    /**
     * Cambiar el color de la card en función de si se seleccionó o se deseleccionó.
     *
     * @param isAnswerSelected   -> {boolean} indica si la card se seleccionó o se deseleccionó.
     * @param TV_Answer_Selected -> {AppCompatTextView} card con la que se esta interactuando.
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

    private void onAnswerQuestion() {
        verifyAnswer(TV_First_Answer, 0, isFirstAnswerSelected);
        verifyAnswer(TV_Second_Answer, 1, isSecondAnswerSelected);
        verifyAnswer(TV_Third_Answer, 2, isThirdAnswerSelected);
        verifyAnswer(TV_Fourth_Answer, 3, isFourthAnswerSelected);

        int amountOfStars = getAmountOfStarsEarned();
        QuestionUtils.fillStarsEarned(context, amountOfStars , IV_Star_1, IV_Star_2, IV_Star_3, starEarnedAnimation);


        isAnswered = true;

        StarsHistory starsHistory = new StarsHistory(context);
        starsHistory.createStarHistory(multipleChoiceQuestion.getGuide(),multipleChoiceQuestion.getId(),amountOfStars,2);
    }

    /**
     * Método que muestra el Pop Up.
     *
     * @param multipleChoiceQuestion question
     */
    public void setQuestion(MultipleChoiceQuestion multipleChoiceQuestion, Guide guide) {
        this.multipleChoiceQuestion = multipleChoiceQuestion;
        this.guide = guide;

        showDialog();
    }

    /**
     * Verificar si las opciones que el usuario seleccionó y no seleccionó son correctas o incorrectas.
     * Mostrando visualmente si se equivoocó o no.
     *
     * @param TV_Answer_Selected  -> {AppCompatTextView} card seleccionada por el usuario.
     * @param indexAnswerSelected -> {int} index para acceder a la respuesta dentro de la lista.
     * @param isAnswerSelected    -> {boolean} indica si la card fue seleccionada por el usuario.
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

    private void changeStarState(int number_of_stars) {
        if (number_of_stars == 1)
            IV_Star_1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
        if (number_of_stars == 2)
            IV_Star_2.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
        if (number_of_stars == 3)
            IV_Star_3.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));

        //if (number_of_stars < 3) showWrongAnswer();

        isAnswered = true;
        BTN_Skip.setVisibility(View.GONE);
    }

    /**
     * Obtener la cantidad de estrellas ganadas para las preguntas de opcion multiple segun:
     * <p>
     * correctAnswersByUser -> Cantidad de respuestas correctas por el usuario.
     * totalCorrectAnswers  -> Cantidad total de respuestas correctas que tiene la pregunta.
     *
     * @return -> Cantidad de estrellas ganadas.
     * @author Alexis Ozaeta
     */
    private int getAmountOfStarsEarned() {
        return (correctAnswersByUser == 0)
                ? 0
                : Math.round(((float) correctAnswersByUser * (float) 3) / (float) totalCorrectAnswers) - incorrectAnswersByUser;
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

    ////////////////////////////////////////////////////////////////////////// Objects from the view
    private AppCompatImageView IV_Star_1;
    private AppCompatImageView IV_Star_2;
    private AppCompatImageView IV_Star_3;
    private AppCompatImageView IV_Close;

    private AppCompatTextView TV_Question;
    private AppCompatTextView TV_Category;
    private AppCompatTextView TV_Topic;
    private AppCompatTextView TV_First_Answer;
    private AppCompatTextView TV_Second_Answer;
    private AppCompatTextView TV_Third_Answer;
    private AppCompatTextView TV_Fourth_Answer;

    private AppCompatButton BTN_Skip;
    private AppCompatButton BTN_Answer;
    private AppCompatButton BTN_Close;
}
