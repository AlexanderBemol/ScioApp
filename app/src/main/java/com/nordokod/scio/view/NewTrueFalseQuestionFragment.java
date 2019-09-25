package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.nordokod.scio.R;
import com.nordokod.scio.constants.KindOfQuestion;
import com.nordokod.scio.constants.UserOperations;
import com.nordokod.scio.controller.MainController;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.InputDataException;
import com.nordokod.scio.entity.OperationCanceledException;
import com.nordokod.scio.entity.TrueFalseQuestion;
import com.nordokod.scio.model.Question;
import com.nordokod.scio.process.UserMessage;

import java.util.Objects;

public class NewTrueFalseQuestionFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private MainActivity activity;
    private Guide guide;

    private AppCompatEditText ET_Question;
    private AppCompatTextView TV_True_Answer, TV_False_Answer;
    private AppCompatButton BTN_Cancel, BTN_Create;
    private Animation press;

    private boolean answer;
    private int preview_answer = 0;

    public NewTrueFalseQuestionFragment() { }

    @SuppressLint("ValidFragment")
    public NewTrueFalseQuestionFragment(Context context, MainActivity activity, Guide guide) {
        this.context = context;
        this.activity = activity;
        this.guide = guide;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_true_false_question, container, false);

        initComponents(view);
        initListeners();
        initAnimations();

        return view;
    }

    @Override
    public void initComponents(View view) {
        ET_Question     = view.findViewById(R.id.FNewTFQuestion_ET_Question);

        TV_True_Answer  = view.findViewById(R.id.FNewTFQuestion_TV_True);
        TV_False_Answer = view.findViewById(R.id.FNewTFQuestion_TV_False);

        BTN_Cancel      = view.findViewById(R.id.FNewTFQuestion_BTN_Cancel);
        BTN_Create      = view.findViewById(R.id.FNewTFQuestion_BTN_Create);

        ET_Question.setText("");
    }

    @Override
    public void initListeners() {
        TV_True_Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = true;
                onChooseAnswer();
            }
        });

        TV_False_Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = false;
                onChooseAnswer();
            }
        });

        BTN_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Cancel.startAnimation(press);
                activity.onCloseFragment("New TrueFalse");
            }
        });

        BTN_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Create.startAnimation(press);
                if (preview_answer > 0) {
                    if(Objects.requireNonNull(ET_Question.getText()).length()>0){
                        TrueFalseQuestion trueFalseQuestion = new TrueFalseQuestion(0, Objects.requireNonNull(ET_Question.getText().toString()), KindOfQuestion.TRUE_FALSE.getCode(), answer);

                        Question question = new Question();
                        question.addQuestion(KindOfQuestion.TRUE_FALSE, guide, trueFalseQuestion).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                showSuccessfulMessage(UserOperations.CREATE_QUESTION);
                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                showError(new OperationCanceledException());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showError(e);
                            }
                        });
                    }else{
                        showError(new InputDataException(InputDataException.Code.EMPTY_FIELD));
                    }

                }else
                    activity.onUnselectedAnswer();
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
        activity.onCloseFragment("New TrueFalse");
    }

    private void initAnimations(){
        press = AnimationUtils.loadAnimation(context, R.anim.press);
    }

    @SuppressLint("ResourceType")
    private void onChooseAnswer() {
        if (answer && preview_answer != 1) {
            TV_True_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_selected_answer));
            TV_True_Answer.setTextAppearance(context, R.style.optionSelectedTextView);

            TV_False_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_card_round));
            TV_False_Answer.setTextAppearance(context, R.style.optionTextView);

            preview_answer = 1;
        } else if (!answer && preview_answer != 2) {
            TV_False_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_selected_answer));
            TV_False_Answer.setTextAppearance(context, R.style.optionSelectedTextView);

            TV_True_Answer.setBackgroundDrawable(context.getDrawable(R.drawable.background_card_round));
            TV_True_Answer.setTextAppearance(context, R.style.optionTextView);

            preview_answer = 2;
        }

        TV_True_Answer.setPadding(
                (int) context.getResources().getDimension(R.dimen.option_textview_padding),
                (int) context.getResources().getDimension(R.dimen.option_textview_padding),
                (int) context.getResources().getDimension(R.dimen.option_textview_padding),
                (int) context.getResources().getDimension(R.dimen.option_textview_padding));

        TV_False_Answer.setPadding(
                (int) context.getResources().getDimension(R.dimen.option_textview_padding),
                (int) context.getResources().getDimension(R.dimen.option_textview_padding),
                (int) context.getResources().getDimension(R.dimen.option_textview_padding),
                (int) context.getResources().getDimension(R.dimen.option_textview_padding));
    }
}
