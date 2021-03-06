package com.nordokod.scio.old.view;

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

import com.nordokod.scio.R;
import com.nordokod.scio.old.constants.Utilities;
import com.nordokod.scio.old.entity.Guide;
import com.nordokod.scio.old.entity.TrueFalseQuestion;
import com.nordokod.scio.old.model.StarsHistory;

import java.util.Objects;

public class TrueFalseQuestionDialog implements BasicDialog {

    private Context context;
    private Dialog dialog;
    private TrueFalseQuestion question;
    private Guide guide;

    private AppCompatImageView IV_Star_1, IV_Star_2, IV_Star_3, IV_Close;
    private AppCompatTextView TV_Question, TV_Category, TV_Topic, TV_True, TV_False;
    private AppCompatButton BTN_Close;

    private boolean wasAnswered = false;


    public TrueFalseQuestionDialog(Context context) {
        this.context = context;
        initDialog();
        initComponents();
        initListeners();
    }

    public void setQuestion(TrueFalseQuestion trueFalseQuestion, Guide guide) {
        this.question = trueFalseQuestion;
        this.guide = guide;
        showDialog();
    }

    @Override
    public void initDialog() {
        dialog = new Dialog(context, R.style.DefaultTheme);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_true_false_question);
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

        BTN_Close       = dialog.findViewById(R.id.TFQuestion_BTN_Close);
    }


    @Override
    public void initListeners() {
        IV_Close.setOnClickListener(v -> dialog.dismiss());

        TV_True.setOnClickListener(v -> {
            int amountOfStars = 0;
            if (question.isAnswer() && !wasAnswered) {
                // Mostramos que eligi?? la respuesta correcta.
                TV_True.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
                TV_True.setTextAppearance(context, R.style.correctAnswer);

                changeStarState();
                amountOfStars = 3;
                wasAnswered = true;
            } else if (!wasAnswered) {
                // Mostramos que eligi?? la respuesta incorrecta.
                TV_True.setBackgroundDrawable(context.getDrawable(R.drawable.background_wrong_answer));
                TV_True.setTextAppearance(context, R.style.wrongAnswer);
                // Mostramos que Falso es la respuesta correcta.
                TV_False.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
                TV_False.setTextAppearance(context, R.style.correctAnswer);
            }
            logInsert(amountOfStars);
            BTN_Close.setVisibility(View.VISIBLE);

        });

        TV_False.setOnClickListener(v -> {
            if (!question.isAnswer() && !wasAnswered) {
                // Mostramos que eligi?? la respuesta correcta.
                TV_False.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
                TV_False.setTextAppearance(context, R.style.correctAnswer);

                changeStarState();
                logInsert(3);
                wasAnswered = true;
            } else if (!wasAnswered) {
                // Mostramos que eligi?? la respuesta incorrecta.
                TV_False.setBackgroundDrawable(context.getDrawable(R.drawable.background_wrong_answer));
                TV_False.setTextAppearance(context, R.style.wrongAnswer);
                // Mostramos que True es la respuesta correcta.
                TV_True.setBackgroundDrawable(context.getDrawable(R.drawable.background_correct_answer));
                TV_True.setTextAppearance(context, R.style.correctAnswer);
                logInsert(0);
            }

            BTN_Close.setVisibility(View.VISIBLE);
        });

        BTN_Close.setOnClickListener(v -> {
            dialog.dismiss();
        });
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



    private void changeStarState() {
        IV_Star_1.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
        IV_Star_2.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
        IV_Star_3.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.starFillColor), PorterDuff.Mode.SRC_IN));
    }

    private void logInsert(int stars){
        StarsHistory starsHistory = new StarsHistory(context);
        starsHistory.createStarHistory(question.getGuide(),question.getId(),stars,2);
    }
}
