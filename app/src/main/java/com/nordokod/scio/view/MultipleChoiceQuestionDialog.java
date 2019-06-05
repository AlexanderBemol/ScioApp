package com.nordokod.scio.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nordokod.scio.R;
import com.nordokod.scio.entity.MultipleChoiceQuestion;

import java.util.ArrayList;

public class MultipleChoiceQuestionDialog extends BroadcastReceiver implements BasicDialog {

    private Context context;
    private Dialog dialog;
    private MultipleChoiceQuestion question;

    private AppCompatImageView IV_Star_1, IV_Star_2, IV_Star_3, IV_Close;
    private AppCompatTextView TV_Question, TV_Category, TV_Topic, TV_First_Answer, TV_Second_Answer, TV_Third_Answer, TV_Fourth_Answer;

    private boolean wasAnswered = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        initDialog();
        initComponents();
        initListeners();
    }

    @Override
    public void initDialog() {
        dialog = new Dialog(context, R.style.Theme_AppCompat_Dialog_Alert);

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
        IV_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        /*
        TV_First_Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question.getCorrect_answer_index() == 0 && !wasAnswered) {
                    onCorrectAnswer(0);
                    changeStarState(0);
                } else if (!wasAnswered)
                    onWrongAnswer(0);
            }
        });

        TV_Second_Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question.getCorrect_answer_index() == 1 && !wasAnswered) {
                    onCorrectAnswer(1);
                    changeStarState(0);
                } else if (!wasAnswered)
                    onWrongAnswer(1);
            }
        });

        TV_Third_Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question.getCorrect_answer_index() == 2 && !wasAnswered) {
                    onCorrectAnswer(2);
                    changeStarState(0);
                } else if (!wasAnswered)
                    onWrongAnswer(2);
            }
        });

        TV_Fourth_Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question.getCorrect_answer_index() == 3 && !wasAnswered) {
                    onCorrectAnswer(3);
                    changeStarState(0);
                } else if (!wasAnswered)
                    onWrongAnswer(3);
            }
        });
        */
    }

    @Override
    public void showDialog() {
        if (!dialog.isShowing()) {
            TV_Category.setText(getCategoryResId(question.getCategory()));
            TV_Topic.setText(question.getTopic());
            TV_Question.setText(question.getQuestion());

            ArrayList<String> answers = question.getAnswer();
            TV_First_Answer.setText(answers.get(0));
            TV_Second_Answer.setText(answers.get(1));
            TV_Third_Answer.setText(answers.get(2));
            TV_Fourth_Answer.setText(answers.get(3));

            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
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

    @Override
    public void changeStarState(int number_of_stars) {
        IV_Star_1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
        IV_Star_2.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
        IV_Star_3.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
    }

    /**
     * Método que muestra el Pop Up.
     * @param multipleChoiceQuestion
     */
    public void setQuestion(MultipleChoiceQuestion multipleChoiceQuestion) {
        this.question = multipleChoiceQuestion;
        showDialog();
    }

    /**
     * Método para obtener el valor String de la categoría según el indice de la base de datos.
     *
     * @param category = indice de la categoría en la base de datos.
     * @return valor int del R.string.[nombre de la categoría]
     */
    private int getCategoryResId(int category) {
        //TODO Agregar los casos según el ID de cada categoría para devolver el R.string....
        switch (category) {
            case 0: return 0;
            default: return 0;
        }
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
