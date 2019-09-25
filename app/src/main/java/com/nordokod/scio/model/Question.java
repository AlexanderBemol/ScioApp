package com.nordokod.scio.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nordokod.scio.constants.KindOfQuestion;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.MultipleChoiceQuestion;
import com.nordokod.scio.entity.OpenQuestion;
import com.nordokod.scio.entity.TrueFalseQuestion;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Question {
    private FirebaseAuth mAuth;
    public Question(){
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * añadir pregunta a la guía
     * @param kindOfQuestion enum con tipo de pregunta
     * @param guide entidad guide con id de guia
     * @param question entidad pregunta
     * @return task con el resultado
     */
    public Task<DocumentReference> addQuestion(KindOfQuestion kindOfQuestion, Guide guide, com.nordokod.scio.entity.Question question){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference questionCollection=db.collection(Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection(Guide.KEY_PERSONAL_GUIDES).document(guide.getId()).collection(com.nordokod.scio.entity.Question.KEY_QUESTIONS);
        Map<String, Object> data = new HashMap<>();
        data.put(com.nordokod.scio.entity.Question.KEY_QUESTION,question.getQuestion());
        data.put(com.nordokod.scio.entity.Question.KEY_KIND_OF_QUESTION,question.getKindOfQuestion());
        switch (kindOfQuestion){
            case MULTIPLE_CHOICE:
                MultipleChoiceQuestion multipleChoiceQuestion= (MultipleChoiceQuestion)question;
                if(!multipleChoiceQuestion.getAnswers().isEmpty()){
                    Map<String, Object> listAnswers = new HashMap<>();
                    for(MultipleChoiceQuestion.Answer answer : multipleChoiceQuestion.getAnswers()){
                        listAnswers.put(MultipleChoiceQuestion.KEY_ANSWER,answer.getAnswer());
                        listAnswers.put(MultipleChoiceQuestion.KEY_CORRECT,answer.isCorrect());
                    }
                    data.put(MultipleChoiceQuestion.KEY_ANSWERS,listAnswers);
                }
                break;
            case TRUE_FALSE:
                TrueFalseQuestion trueFalseQuestion=(TrueFalseQuestion)question;
                data.put(TrueFalseQuestion.KEY_ANSWER,trueFalseQuestion.isAnswer());
                break;
            case OPEN:
                OpenQuestion openQuestion=(OpenQuestion)question;
                data.put(OpenQuestion.KEY_ANSWER,openQuestion.getAnswer());
                break;
        }
        return questionCollection.add(data);
    }

    /**
     * eliminar pregunta de la guía
     * @param guide entidad guide con id de guia
     * @param question entidad question con id de pregunta
     * @return task con el resultado
     */
    public Task<Void> deleteQuestion(Guide guide, com.nordokod.scio.entity.Question question) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference questionCollection = db.collection(Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection(Guide.KEY_GUIDES).document(guide.getId()).collection(com.nordokod.scio.entity.Question.KEY_QUESTION);
        return questionCollection.document(String.valueOf(question.getId())).delete();
    }

    /**
     * obtiene todas las preguntas de una guía
     * @param guide entidad guide con su id
     * @return task con el resultado
     */
    public Task<QuerySnapshot> getQuestionsOfGuide(Guide guide){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference questionCollection = db.collection(Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection(Guide.KEY_GUIDES).document(guide.getId()).collection(com.nordokod.scio.entity.Question.KEY_QUESTION);
        return questionCollection.get();
    }

}
