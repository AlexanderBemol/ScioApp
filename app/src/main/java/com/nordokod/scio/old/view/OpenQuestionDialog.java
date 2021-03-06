package com.nordokod.scio.old.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.old.constants.Utilities;
import com.nordokod.scio.old.entity.Guide;
import com.nordokod.scio.old.entity.InputDataException;
import com.nordokod.scio.old.entity.OpenQuestion;
import com.nordokod.scio.old.model.StarsHistory;
import com.nordokod.scio.old.process.QualifyMethod;
import com.nordokod.scio.old.process.UserMessage;

import java.util.Objects;

public class OpenQuestionDialog implements BasicDialog {

    private Context context;
    private Dialog dialog;
    private OpenQuestion question;
    private Guide guide;

    private AppCompatImageView IV_Star_1, IV_Star_2, IV_Star_3, IV_Close;
    private AppCompatTextView TV_Question, TV_Category, TV_Topic, TV_Correct_Answer, TV_Correct_Answer_Title;
    private AppCompatEditText ET_Answer;
    private AppCompatButton BTN_Skip, BTN_Answer, BTN_Close;

    private Animation press;

    private boolean isAnswered = false;

    public OpenQuestionDialog(Context context) {
        this.context = context;
        initDialog();
        initComponents();
        initListeners();
        initAnimations();
    }

    @Override
    public void initDialog() {
        dialog = new Dialog(context, R.style.DefaultTheme);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_open_question);
    }

    @Override
    public void initComponents() {
        IV_Star_1 = dialog.findViewById(R.id.OQuestion_IV_Star_1);
        IV_Star_2 = dialog.findViewById(R.id.OQuestion_IV_Star_2);
        IV_Star_3 = dialog.findViewById(R.id.OQuestion_IV_Star_3);

        IV_Close = dialog.findViewById(R.id.OQuestion_IV_Close);

        TV_Category = dialog.findViewById(R.id.OQuestion_TV_Category);
        TV_Topic = dialog.findViewById(R.id.OQuestion_TV_Topic);
        TV_Question = dialog.findViewById(R.id.OQuestion_TV_Question);

        TV_Correct_Answer = dialog.findViewById(R.id.OQuestion_TV_Correct_Answer);
        TV_Correct_Answer_Title = dialog.findViewById(R.id.OQuestion_TV_Correct_Answer_Title);

        ET_Answer = dialog.findViewById(R.id.OQuestion_ET_Answer);

        BTN_Answer = dialog.findViewById(R.id.OQuestion_BTN_Answer);
        BTN_Skip = dialog.findViewById(R.id.OQuestion_BTN_Skip);
        BTN_Close = dialog.findViewById(R.id.OQuestion_BTN_Close);
    }

    @Override
    public void initListeners() {
        IV_Close.setOnClickListener(v -> dialog.dismiss());

        BTN_Answer.setOnClickListener(v -> {
            BTN_Answer.startAnimation(press);
            if (!isAnswered) {
                if (Objects.requireNonNull(ET_Answer.getText()).length() == 0)
                    showError(new InputDataException(InputDataException.Code.EMPTY_FIELD));
                else {
                    int amountOfStars = QualifyMethod.getStars(question.getAnswer(), ET_Answer.getText().toString());
                    changeStarState(amountOfStars);
                    showCorrectAnswer();

                    //insert log
                    StarsHistory starsHistory = new StarsHistory(context);
                    starsHistory.createStarHistory(question.getGuide(),question.getId(),amountOfStars,2);
                }

            }
        });

        BTN_Skip.setOnClickListener(v -> {
            BTN_Skip.startAnimation(press);
            dialog.dismiss();
        });

        BTN_Close.setOnClickListener(v -> {
            BTN_Close.startAnimation(press);
            dialog.dismiss();
        });
    }

    @Override
    public void showDialog() {
        if (!dialog.isShowing()) {
            TV_Category.setText(Utilities.getCategoryStringResource(guide.getCategory()));
            TV_Topic.setText(guide.getTopic());
            TV_Question.setText(question.getQuestion());
            TV_Correct_Answer.setText(question.getAnswer());

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


    private void changeStarState(int number_of_stars) {
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
            default:
                break;
        }

        isAnswered = true;
        BTN_Skip.setVisibility(View.GONE);
        BTN_Answer.setVisibility(View.GONE);
        BTN_Close.setVisibility(View.VISIBLE);
    }

    /**
     * M??todo que muestra el Pop Up.
     *
     * @param openQuestion question
     */
    public void setQuestion(OpenQuestion openQuestion, Guide guide) {
        this.question = openQuestion;
        this.guide = guide;
        showDialog();
    }

    private void showCorrectAnswer() {
        TV_Correct_Answer_Title.setVisibility(View.VISIBLE);
        TV_Correct_Answer.setVisibility(View.VISIBLE);
    }

    private void initAnimations() {
        press = AnimationUtils.loadAnimation(context, R.anim.press);
    }

    private void showError(Exception e) {
        UserMessage userMessage = new UserMessage();
        userMessage.showErrorMessage(context, userMessage.categorizeException(e));
    }
}
