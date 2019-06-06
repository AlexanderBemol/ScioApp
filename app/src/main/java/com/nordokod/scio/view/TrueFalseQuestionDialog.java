package com.nordokod.scio.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nordokod.scio.R;
import com.nordokod.scio.entity.TrueFalseQuestion;

public class TrueFalseQuestionDialog implements BasicDialog {

    private Context context;
    private Dialog dialog;
    private TrueFalseQuestion question;

    private AppCompatImageView IV_Star_1, IV_Star_2, IV_Star_3, IV_Close;
    private AppCompatTextView TV_Question, TV_Category, TV_Topic, TV_True, TV_False;

    private boolean wasAnswered = false;

    public void config(Context context) {
        this.context = context;
        initDialog();
        initComponents();
        initListeners();
    }

    public void setQuestion(TrueFalseQuestion trueFalseQuestion) {
        this.question = trueFalseQuestion;
        //showDialog();
    }

    @Override
    public void initDialog() {
        //AlertDialog.Builder builder=new AlertDialog.Builder(context,R.style.DefaultTheme);

        //LayoutInflater inflater = LayoutInflater.from(context);
        //View dialogView = inflater.inflate(R.layout.dialog_true_false_question, null);
        //builder.setView(dialogView);
        //dialog=builder.create();
        dialog=new Dialog(context,R.style.DefaultTheme);
        dialog.setContentView(R.layout.dialog_true_false_question);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

    }

    @Override
    public void initComponents() {
        IV_Star_1       = dialog.findViewById(R.id.TFQuestion_IV_Star_1);
        IV_Star_2       = dialog.findViewById(R.id.TFQuestion_IV_Star_2);
        IV_Star_3       = dialog.findViewById(R.id.TFQuestion_IV_Star_3);

        IV_Close        = dialog.findViewById(R.id.TFQuestion_IV_Close);

        TV_Category     = dialog.findViewById(R.id.TFQuestion_TV_Category);
        TV_Topic        = dialog.findViewById(R.id.TFQuestion_TV_Topic);
        TV_Question     = dialog.findViewById(R.id.TFQuestion_TV_Question);

        TV_True         = dialog.findViewById(R.id.TFQuestion_Answer_True);
        TV_False        = dialog.findViewById(R.id.TFQuestion_Answer_False);


    }

    /*
    * Posiblemente haya errores en el cambio de color de las respuestas.
    * En caso de que sí, las corrigo cuando ya esté el backend terminado para
    * poder hacer pruebas.
    */
    @Override
    public void initListeners() {
        IV_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TV_True.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question.isAnswer() && !wasAnswered) {
                    // Mostramos que eligió la respuesta correcta.
                    TV_True.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
                    TV_True.setTextAppearance(context, R.style.correctAnswer);

                    changeStarState(0);
                    wasAnswered = true;
                } else if (!wasAnswered) {
                    // Mostramos que eligió la respuesta incorrecta.
                    TV_True.setBackgroundDrawable(context.getDrawable(R.drawable.background_wrong_answer));
                    TV_True.setTextAppearance(context, R.style.wrongAnswer);
                    // Mostramos que Falso es la respuesta correcta.
                    TV_False.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
                    TV_False.setTextAppearance(context, R.style.correctAnswer);
                }
            }
        });

        TV_False.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!question.isAnswer() && !wasAnswered) {
                    // Mostramos que eligió la respuesta correcta.
                    TV_False.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
                    TV_False.setTextAppearance(context, R.style.correctAnswer);

                    changeStarState(0);
                    wasAnswered = true;
                } else if (!wasAnswered) {
                    // Mostramos que eligió la respuesta incorrecta.
                    TV_False.setBackgroundDrawable(context.getDrawable(R.drawable.background_wrong_answer));
                    TV_False.setTextAppearance(context, R.style.wrongAnswer);
                    // Mostramos que True es la respuesta correcta.
                    TV_True.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
                    TV_True.setTextAppearance(context, R.style.correctAnswer);
                }
            }
        });
    }

    @Override
    public void showDialog() {
        if (!dialog.isShowing()) {
            TV_Category.setText(getCategoryResId(question.getCategory()));
            TV_Topic.setText(question.getTopic());
            TV_Question.setText(question.getQuestion());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <Build.VERSION_CODES.O){
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }else{
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
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


    @Override
    public void changeStarState(int number_of_stars) {
        IV_Star_1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
        IV_Star_2.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
        IV_Star_3.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
    }

    private int getCategoryResId(int category) {
        //TODO Agregar los casos según el ID de cada categoría para devolver el R.string....
        switch (category) {
            case 1: return R.string.category_exact_sciences;
            case 2: return R.string.category_social_sciences;
            case 3: return R.string.category_sports;
            case 4: return R.string.category_art;
            case 5: return R.string.category_tech;
            case 6: return R.string.category_entertainment;
            default: return R.string.category_others;
        }
    }
}
