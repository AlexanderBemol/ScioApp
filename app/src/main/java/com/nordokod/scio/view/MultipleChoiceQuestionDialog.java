package com.nordokod.scio.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.nordokod.scio.R;
import com.nordokod.scio.constants.Utilities;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.MultipleChoiceQuestion;

import java.util.Objects;

public class MultipleChoiceQuestionDialog implements BasicDialog {

    private Context context;
    private Dialog dialog;
    private MultipleChoiceQuestion question;
    private Guide guide;

    private AppCompatImageView IV_Star_1, IV_Star_2, IV_Star_3, IV_Close;
    private AppCompatTextView TV_Question, TV_Category, TV_Topic, TV_First_Answer, TV_Second_Answer, TV_Third_Answer, TV_Fourth_Answer;

    private boolean wasAnswered = false;


    public MultipleChoiceQuestionDialog(Context context) {
        this.context = context;
        initDialog();
        initComponents();
        initListeners();
    }

    @Override
    public void initDialog() {
        dialog = new Dialog(context, R.style.NightTheme);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_multiple_choice_question);
    }

    @Override
    public void initComponents() {
        IV_Star_1       = dialog.findViewById(R.id.MCQuestion_IV_Star_1);
        IV_Star_2       = dialog.findViewById(R.id.MCQuestion_IV_Star_2);
        IV_Star_3       = dialog.findViewById(R.id.MCQuestion_IV_Star_3);

        IV_Close        = dialog.findViewById(R.id.MCQuestion_IV_Close);

        TV_Category     = dialog.findViewById(R.id.MCQuestion_TV_Category);
        TV_Topic        = dialog.findViewById(R.id.MCQuestion_TV_Topic);
        TV_Question     = dialog.findViewById(R.id.MCQuestion_TV_Question);

        TV_First_Answer     = dialog.findViewById(R.id.MCQuestion_TV_First_Answer);
        TV_Second_Answer    = dialog.findViewById(R.id.MCQuestion_TV_Second_Answer);
        TV_Third_Answer     = dialog.findViewById(R.id.MCQuestion_TV_Third_Answer);
        TV_Fourth_Answer    = dialog.findViewById(R.id.MCQuestion_TV_Fourth_Answer);
    }

    @Override
    public void initListeners() {
        IV_Close.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void showDialog() {
        if (!dialog.isShowing()) {
            TV_Category.setText(Utilities.getCategoryStringResource(guide.getCategory()));
            TV_Topic.setText(guide.getTopic());
            TV_Question.setText(question.getQuestion());


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <Build.VERSION_CODES.O){
                Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_TOAST);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }else{
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


    public void changeStarState(int number_of_stars) {
        IV_Star_1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
        IV_Star_2.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
        IV_Star_3.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
    }

    /**
     * Método que muestra el Pop Up.
     * @param multipleChoiceQuestion question
     */
    public void setQuestion(MultipleChoiceQuestion multipleChoiceQuestion, Guide guide) {
        this.question = multipleChoiceQuestion;
        this.guide = guide;



        showDialog();
    }

    /**
     * Método para resaltar en verde la respuesta correcta.
     * @param correct_answer = indice de la respuesta correcta elegida.
     **/
    private void onCorrectAnswer(int correct_answer) {
        switch (correct_answer) {
            case 0:
                TV_First_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
                TV_First_Answer.setTextAppearance(context, R.style.correctAnswer);
                break;
            case 1:
                TV_Second_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
                TV_Second_Answer.setTextAppearance(context, R.style.correctAnswer);
                break;
            case 2:
                TV_Third_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
                TV_Third_Answer.setTextAppearance(context, R.style.correctAnswer);
                break;
            case 3:
                TV_Fourth_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
                TV_Fourth_Answer.setTextAppearance(context, R.style.correctAnswer);
                break;
        }

        wasAnswered = true;
    }

    /**
     * Método para resaltar en rojo la respuesta incorrecta elegida por el usuario
     * y resaltar en verde la respuesta correcta.
     *
     * @param wrong_answer_selected = indice de la respuesta incorrecta elegida.
     */
    private void onWrongAnswer(int wrong_answer_selected) {
        switch (wrong_answer_selected) {
            case 0:
                TV_First_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_wrong_answer));
                TV_First_Answer.setTextAppearance(context, R.style.wrongAnswer);
                break;
            case 1:
                TV_Second_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_wrong_answer));
                TV_Second_Answer.setTextAppearance(context, R.style.wrongAnswer);
                break;
            case 2:
                TV_Third_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_wrong_answer));
                TV_Third_Answer.setTextAppearance(context, R.style.wrongAnswer);
                break;
            case 3:
                TV_Fourth_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_wrong_answer));
                TV_Fourth_Answer.setTextAppearance(context, R.style.wrongAnswer);
                break;
        }

        //onCorrectAnswer(question.getCorrect_answer_index());
    }
}
