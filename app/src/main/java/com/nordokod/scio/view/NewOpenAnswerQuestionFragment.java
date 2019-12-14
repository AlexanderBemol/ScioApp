package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nordokod.scio.R;
import com.nordokod.scio.constants.KindOfQuestion;
import com.nordokod.scio.constants.UserOperations;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.InputDataException;
import com.nordokod.scio.entity.OpenQuestion;
import com.nordokod.scio.entity.OperationCanceledException;
import com.nordokod.scio.model.Question;
import com.nordokod.scio.process.UserMessage;

import java.util.Objects;

public class NewOpenAnswerQuestionFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private MainActivity activity;
    private Guide guide;

    private AppCompatEditText ET_Question, ET_Answer;
    private AppCompatButton BTN_Cancel, BTN_Create;

    private Animation press;

    public NewOpenAnswerQuestionFragment() { }

    @SuppressLint("ValidFragment")
    public NewOpenAnswerQuestionFragment(Context context, MainActivity activity, Guide guide) {
        this.context = context;
        this.activity = activity;
        this.guide = guide;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_open_answer_question, container, false);

        initComponents(view);
        initListeners();
        initAnimations();

        return view;
    }

    @Override
    public void initComponents(View view) {
        ET_Question = view.findViewById(R.id.FNewOpenQuestion_ET_Question);
        ET_Answer   = view.findViewById(R.id.FNewOpenQuestion_ET_Answer);

        BTN_Cancel  = view.findViewById(R.id.FNewOpenQuestion_BTN_Cancel);
        BTN_Create  = view.findViewById(R.id.FNewOpenQuestion_BTN_Create);
    }

    @Override
    public void initListeners() {
        BTN_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Cancel.startAnimation(press);
                activity.onCloseFragment("New Open Answer");
            }
        });

        BTN_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Create.startAnimation(press);

                if(Objects.requireNonNull(ET_Question.getText()).length()!=0&& Objects.requireNonNull(ET_Answer.getText()).length()!=0){
                    OpenQuestion openQuestion = new OpenQuestion(0, ET_Question.getText().toString(), KindOfQuestion.OPEN.getCode(), ET_Answer.getText().toString());

                    Question question = new Question();
                    question.addQuestion(KindOfQuestion.OPEN, guide, openQuestion)
                            .addOnSuccessListener(documentReference -> showSuccessfulMessage(UserOperations.CREATE_QUESTION))
                            .addOnCanceledListener(() -> showError(new OperationCanceledException()))
                            .addOnFailureListener(e -> showError(e));
                }else{
                    showError(new InputDataException(InputDataException.Code.EMPTY_FIELD));
                }

            }
        });
    }
    private void showError(Exception e){
        UserMessage userMessage = new UserMessage();
        userMessage.showErrorMessage(context,userMessage.categorizeException(e));
    }
    private void showSuccessfulMessage(UserOperations userOperations){
        UserMessage userMessage = new UserMessage();
        userMessage.showSuccessfulOperationMessage(context,userOperations);
        activity.onCloseFragment("New Open Answer");
    }
    private void initAnimations(){
        press = AnimationUtils.loadAnimation(context, R.anim.press);
    }
}
