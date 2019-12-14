package com.nordokod.scio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
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
import com.nordokod.scio.entity.MultipleChoiceQuestion;
import com.nordokod.scio.entity.OperationCanceledException;
import com.nordokod.scio.model.Question;
import com.nordokod.scio.process.UserMessage;

import java.util.Objects;

public class NewMultipleChoiceQuestionFragment extends BottomSheetDialogFragment implements BasicFragment {

    private Context context;
    private MainActivity activity;
    private Guide guide;

    private AppCompatEditText ET_Question, ET_Option_1, ET_Option_2, ET_Option_3, ET_Option_4;
    private AppCompatButton BTN_Cancel, BTN_Create;
    private SwitchCompat Switch_Option_1, Switch_Option_2, Switch_Option_3, Switch_Option_4;

    private Animation press;

    public NewMultipleChoiceQuestionFragment() { }

    @SuppressLint("ValidFragment")
    public NewMultipleChoiceQuestionFragment(Context context, MainActivity activity, Guide guide) {
        this.context = context;
        this.activity = activity;
        this.guide = guide;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_multiple_choice_question, container, false);

        initComponents(view);
        initListeners();
        initAnimations();

        return view;
    }

    @Override
    public void initComponents(View view) {
        ET_Question     = view.findViewById(R.id.FNewMCQuestion_ET_Question);

        ET_Option_1     = view.findViewById(R.id.FNewMCQuestion_ET_Option_1);
        ET_Option_2     = view.findViewById(R.id.FNewMCQuestion_ET_Option_2);
        ET_Option_3     = view.findViewById(R.id.FNewMCQuestion_ET_Option_3);
        ET_Option_4     = view.findViewById(R.id.FNewMCQuestion_ET_Option_4);

        Switch_Option_1 = view.findViewById(R.id.FNewMCQuestion_Switch_Option_1);
        Switch_Option_2 = view.findViewById(R.id.FNewMCQuestion_Switch_Option_2);
        Switch_Option_3 = view.findViewById(R.id.FNewMCQuestion_Switch_Option_3);
        Switch_Option_4 = view.findViewById(R.id.FNewMCQuestion_Switch_Option_4);

        BTN_Cancel      = view.findViewById(R.id.FNewMCQuestion_BTN_Cancel);
        BTN_Create      = view.findViewById(R.id.FNewMCQuestion_BTN_Create);
    }

    @Override
    public void initListeners() {
        BTN_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Cancel.startAnimation(press);
                activity.onCloseFragment("New Multiple Choice");
            }
        });

        BTN_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTN_Create.startAnimation(press);
                if(Objects.requireNonNull(ET_Question.getText()).length()!=0){
                    AppCompatEditText[] editTextArray = {ET_Option_1,ET_Option_2,ET_Option_3,ET_Option_4};
                    SwitchCompat[] switchCompatArray = {Switch_Option_1,Switch_Option_2,Switch_Option_3,Switch_Option_4};

                    int optionCounter=0;
                    boolean correctOptionSelected=false;
                    for (int i=0;i<editTextArray.length;i++){
                       if(Objects.requireNonNull(editTextArray[i].getText()).length()!=0){
                           optionCounter++;
                           correctOptionSelected=switchCompatArray[i].isSelected();
                       }
                    }
                    if(optionCounter<2)showError(new InputDataException(InputDataException.Code.NOT_ENOUGH_OPTIONS));
                    else if(!correctOptionSelected)showError(new InputDataException(InputDataException.Code.NOT_CORRECT_OPTION_SELECTED));
                    else{
                        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(0, Objects.requireNonNull(ET_Question.getText()).toString(), KindOfQuestion.MULTIPLE_CHOICE.getCode());
                        for(int i = 0;i<editTextArray.length;i++){
                            if(Objects.requireNonNull(editTextArray[i].getText()).length()!=0){
                                multipleChoiceQuestion.addAnswer(Objects.requireNonNull(editTextArray[i].getText()).toString(),switchCompatArray[i].isSelected());
                            }
                        }
                        Question question = new Question();

                        question.addQuestion(
                                KindOfQuestion.MULTIPLE_CHOICE,
                                guide,
                                multipleChoiceQuestion
                        )
                                .addOnSuccessListener(documentReference -> showSuccessfulMessage(UserOperations.CREATE_QUESTION))
                                .addOnCanceledListener(() -> showError(new OperationCanceledException()))
                                .addOnFailureListener(e -> showError(e));
                    }
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
        activity.onCloseFragment("New Multiple Choice");
    }

    private void initAnimations(){
        press = AnimationUtils.loadAnimation(context, R.anim.press);
    }
}
