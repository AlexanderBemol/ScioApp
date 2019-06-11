package com.nordokod.scio.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nordokod.scio.entity.AppConstants;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.MultipleChoiceQuestion;
import com.nordokod.scio.entity.OpenQuestion;
import com.nordokod.scio.entity.Question;
import com.nordokod.scio.entity.TrueFalseQuestion;

import java.util.HashMap;
import java.util.Map;

public class QuestionData {
    public interface customListener{
        void onSuccesUpload();
        void onSucces();
        void onFailure(String errorMessage);
    }
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private Context currentContext;
    private QuestionData.customListener customListener;
    public QuestionData(){

    }
    public QuestionData(FirebaseFirestore ff, FirebaseUser currentUser, Context context){
        this.db=ff;
        this.currentUser=currentUser;
        this.currentContext=context;
    }
    public void setCustomListener(QuestionData.customListener customListener) {
        this.customListener = customListener;
    }
    public void addQuestionMultiple(MultipleChoiceQuestion mcq, Guide guide){
        CollectionReference questionCollection=db.collection(AppConstants.CLOUD_GUIDES).document(currentUser.getUid()).collection(AppConstants.CLOUD_GUIDES).document(guide.getId()).collection(AppConstants.CLOUD_QUESTIONS);
        Map<String, Object> data = new HashMap<>();
        data.put(Question.CLOUD_Question,mcq.getQuestion());
        data.put(MultipleChoiceQuestion.CLOUD_Answer1,mcq.getAnswers().get(0).getOption());
        data.put(MultipleChoiceQuestion.CLOUD_Answer2,mcq.getAnswers().get(1).getOption());
        data.put(MultipleChoiceQuestion.CLOUD_Answer3,mcq.getAnswers().get(2).getOption());
        data.put(MultipleChoiceQuestion.CLOUD_Answer4,mcq.getAnswers().get(3).getOption());
        data.put(MultipleChoiceQuestion.CLOUD_AnswerReal1,mcq.getAnswers().get(0).is_correct());
        data.put(MultipleChoiceQuestion.CLOUD_AnswerReal2,mcq.getAnswers().get(1).is_correct());
        data.put(MultipleChoiceQuestion.CLOUD_AnswerReal3,mcq.getAnswers().get(2).is_correct());
        data.put(MultipleChoiceQuestion.CLOUD_AnswerReal4,mcq.getAnswers().get(3).is_correct());
        data.put(Question.CLOUD_KIND_OF_QUESTION,mcq.getId());
        questionCollection.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                customListener.onSuccesUpload();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                customListener.onFailure(e.getMessage());
            }
        });
    }
    public void addQuestionTrueFalse(TrueFalseQuestion tfq,Guide guide){
        CollectionReference questionCollection=db.collection(AppConstants.CLOUD_GUIDES).document(currentUser.getUid()).collection(AppConstants.CLOUD_GUIDES).document(guide.getId()).collection(AppConstants.CLOUD_QUESTIONS);
        Map<String, Object> data = new HashMap<>();
        data.put(Question.CLOUD_Question,tfq.getQuestion());
        data.put(TrueFalseQuestion.CLOUD_ANSWER,tfq.isAnswer());
        data.put(Question.CLOUD_KIND_OF_QUESTION,tfq.getId());
        questionCollection.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                customListener.onSuccesUpload();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                customListener.onFailure(e.getMessage());
            }
        });
    }
    public void addOpenQuestion(OpenQuestion oq,Guide guide){
        CollectionReference questionCollection=db.collection(AppConstants.CLOUD_GUIDES).document(currentUser.getUid()).collection(AppConstants.CLOUD_GUIDES).document(guide.getId()).collection(AppConstants.CLOUD_QUESTIONS);
        Map<String, Object> data = new HashMap<>();
        data.put(Question.CLOUD_Question,oq.getQuestion());
        data.put(Question.CLOUD_KIND_OF_QUESTION,oq.getId());
        data.put(OpenQuestion.CLOUD_ANSWER,oq.getAnswer());
        questionCollection.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                customListener.onSuccesUpload();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                customListener.onFailure(e.getMessage());
            }
        });
    }
}