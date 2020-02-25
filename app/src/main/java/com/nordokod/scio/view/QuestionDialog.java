package com.nordokod.scio.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentTransaction;

import com.nordokod.scio.R;
import com.nordokod.scio.constants.KindOfQuestion;
import com.nordokod.scio.constants.Utilities;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.MultipleChoiceQuestion;
import com.nordokod.scio.entity.OpenQuestion;
import com.nordokod.scio.entity.Question;
import com.nordokod.scio.entity.TrueFalseQuestion;

import java.util.Objects;

public class QuestionDialog implements BasicDialog {

    private Context context;
    private Dialog dialog;
    private Guide guide;
    private Question question;

    public QuestionDialog(Context context, Guide guide, Question question) {
        this.context = context;
        this.guide = guide;
        this.question = question;

        initDialog();
        initComponents();
        initListeners();
        showQuestionFragment();
    }

    @Override
    public void initDialog() {
        dialog = new Dialog(context, R.style.NightTheme);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_question);
    }

    @Override
    public void initComponents() {
        TV_Category = dialog.findViewById(R.id.QuestionDialog_TV_Category);
        TV_Topic    = dialog.findViewById(R.id.QuestionDialog_TV_Topic);
        IV_Close    = dialog.findViewById(R.id.QuestionDialog_IV_Close);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void showDialog() {
        if (!dialog.isShowing()) {
            TV_Category.setText(Utilities.getStringFromCategory(guide.getCategory()));
            TV_Topic.setText(guide.getTopic());

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <Build.VERSION_CODES.O)
                Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_TOAST);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            else
                Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_PHONE);

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

    private void showQuestionFragment() {
        if (question.getKindOfQuestion() == KindOfQuestion.MULTIPLE_CHOICE.getCode())
            setMultipleChoiceQuestionFragment();
        else if (question.getKindOfQuestion() == KindOfQuestion.TRUE_FALSE.getCode())
            setTrueFalseQuestionFragment();
        else if (question.getKindOfQuestion() == KindOfQuestion.OPEN.getCode())
            setOpenQuestionFragment();
    }

    private void setMultipleChoiceQuestionFragment() {
        FragmentTransaction transaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.QuestionDialog_FL_Question, new MultipleChoiceQuestionFragment(context, (MultipleChoiceQuestion) question, null));
        transaction.addToBackStack(null);
        transaction.commit();

        showDialog();
    }

    private void setTrueFalseQuestionFragment() {
        FragmentTransaction transaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.QuestionDialog_FL_Question, new TrueFalseQuestionFragment(context, (TrueFalseQuestion) question, null));
        transaction.addToBackStack(null);
        transaction.commit();

        showDialog();
    }

    private void setOpenQuestionFragment() {
        FragmentTransaction transaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.QuestionDialog_FL_Question, new OpenQuestionFragment(context, (OpenQuestion) question, null));
        transaction.addToBackStack(null);
        transaction.commit();

        showDialog();
    }

    ////////////////////////////////////////////////////////////////////////// Objects from the view
    private AppCompatTextView TV_Category;
    private AppCompatTextView TV_Topic;
    private AppCompatImageView IV_Close;
    private AppCompatButton BTN_Answer;

}
