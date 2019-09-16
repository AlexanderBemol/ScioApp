package com.nordokod.scio.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.entity.OpenQuestion;

public class OpenQuestionDialog extends BroadcastReceiver implements BasicDialog {

    private Context context;
    private Dialog dialog;
    private OpenQuestion question;

    private AppCompatImageView IV_Star_1, IV_Star_2, IV_Star_3, IV_Close;
    private AppCompatTextView TV_Question, TV_Category, TV_Topic, TV_Correct_Answer, TV_Correct_Answer_Title;
    private AppCompatEditText ET_Answer;
    private AppCompatButton BTN_Skip, BTN_Answer;

    private Animation press;

    private boolean wasAnswered = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        initDialog();
        initComponents();
        initListeners();
        initAnimations();
    }

    @Override
    public void initDialog() {
        dialog = new Dialog(context, R.style.Theme_AppCompat_Dialog_Alert);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_true_false_question);
    }

    @Override
    public void initComponents() {
        IV_Star_1           = dialog.findViewById(R.id.OQuestion_IV_Star_1);
        IV_Star_2           = dialog.findViewById(R.id.OQuestion_IV_Star_2);
        IV_Star_3           = dialog.findViewById(R.id.OQuestion_IV_Star_3);

        IV_Close            = dialog.findViewById(R.id.OQuestion_IV_Close);

        TV_Category         = dialog.findViewById(R.id.OQuestion_TV_Category);
        TV_Topic            = dialog.findViewById(R.id.OQuestion_TV_Topic);
        TV_Question         = dialog.findViewById(R.id.OQuestion_TV_Question);

        TV_Correct_Answer           = dialog.findViewById(R.id.OQuestion_TV_Correct_Answer);
        TV_Correct_Answer_Title     = dialog.findViewById(R.id.OQuestion_TV_Correct_Answer_Title);

        ET_Answer           = dialog.findViewById(R.id.OQuestion_ET_Answer);

        BTN_Answer          = dialog.findViewById(R.id.OQuestion_BTN_Answer);
        BTN_Skip            = dialog.findViewById(R.id.OQuestion_BTN_Skip);
    }

    @Override
    public void initListeners() {
        IV_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        BTN_Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Answer.startAnimation(press);
                if (!wasAnswered) {
                    // TODO Aquí mandas la respuesta del usuario para revisarla.
                    //  Procura que devuelva un entero entre 1 y 3 para la cantidad de estrellas, y lo mandas por parametro al método de las estrellas.
                    changeStarState(0);
                }
            }
        });

        BTN_Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Skip.startAnimation(press);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void showDialog() {
        /*if (!dialog.isShowing()) {
            TV_Category.setText(getCategoryResId(question.getCategory()));
            TV_Topic.setText(question.getTopic());
            TV_Question.setText(question.getQuestion());

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
        }*/
    }


    public void changeStarState(int number_of_stars) {
        switch (number_of_stars) {
            case 1:
                IV_Star_1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
                showCorrectAnswer();
                break;
            case 2:
                IV_Star_1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
                IV_Star_2.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
                showCorrectAnswer();
                break;
            case 3:
                IV_Star_1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
                IV_Star_2.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
                IV_Star_3.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
                break;
            default: break;
        }

        wasAnswered = true;
        BTN_Skip.setVisibility(View.GONE);
    }

    /**
     * Método que muestra el Pop Up.
     * @param openQuestion
     */
    public void setQuestion(OpenQuestion openQuestion) {
        this.question = openQuestion;
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

    private void showCorrectAnswer() {
        TV_Correct_Answer_Title.setVisibility(View.VISIBLE);
        TV_Correct_Answer.setVisibility(View.VISIBLE);
    }

    private void initAnimations(){
        press = AnimationUtils.loadAnimation(context, R.anim.press);
    }
}
