package com.nordokod.scio.model;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nordokod.scio.constants.KindOfQuestion;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.MultipleChoiceQuestion;
import com.nordokod.scio.entity.OpenQuestion;
import com.nordokod.scio.entity.TrueFalseQuestion;
import com.nordokod.scio.entity.Answer;

import java.util.ArrayList;
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
    public Task<Void> addQuestion(KindOfQuestion kindOfQuestion, Guide guide, com.nordokod.scio.entity.Question question){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put(com.nordokod.scio.entity.Question.KEY_QUESTION,question.getQuestion());
        data.put(com.nordokod.scio.entity.Question.KEY_KIND_OF_QUESTION,question.getKindOfQuestion());
        switch (kindOfQuestion){
            case TRUE_FALSE:
                TrueFalseQuestion trueFalseQuestion=(TrueFalseQuestion)question;
                data.put(TrueFalseQuestion.KEY_ANSWER,trueFalseQuestion.isAnswer());
                break;
            case OPEN:
                OpenQuestion openQuestion=(OpenQuestion)question;
                data.put(OpenQuestion.KEY_ANSWER,openQuestion.getAnswer());
                break;
            case MULTIPLE_CHOICE:
                int index=0;
                MultipleChoiceQuestion multipleChoiceQuestion =(MultipleChoiceQuestion)question;
                Map<String,Object> map = new HashMap<>();
                for (Answer answer : multipleChoiceQuestion.getAnswers()){
                    map.put(String.valueOf(index),answer);
                    index++;
                }
                data.put(MultipleChoiceQuestion.KEY_ANSWERS,map);
                break;
        }
        return db.collection(Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getUid())).collection(Guide.KEY_PERSONAL_GUIDES).document(guide.getId()).update(com.nordokod.scio.entity.Question.KEY_QUESTIONS, FieldValue.arrayUnion(data));
    }

    /**
     * eliminar pregunta de la guía
     * @param guide entidad guide con id de guia
     * @param question entidad question con id de pregunta
     * @return task con el resultado
     */
    public Task<Void> deleteQuestion(Guide guide, Map<String, Object> question) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection(Guide.KEY_PERSONAL_GUIDES).document(guide.getId()).update(com.nordokod.scio.entity.Question.KEY_QUESTIONS,FieldValue.arrayRemove(question));
    }

    /**
     * obtiene todas las preguntas de una guía
     * @param guide entidad guide con su id
     * @return Arraylist con preguntas
     */
    public ArrayList<com.nordokod.scio.entity.Question> getQuestionsFromGuide(Guide guide){
        ArrayList<com.nordokod.scio.entity.Question> questions = new ArrayList<>();
        int index1=0;
        for (Object aux : guide.getAuxQuestions()) {
            com.nordokod.scio.entity.Question question;
            int kindOfQuestion = ((Long)((HashMap<String,Object>)aux).get(com.nordokod.scio.entity.Question.KEY_KIND_OF_QUESTION)).intValue();
            String stringQuestion = Objects.requireNonNull(((HashMap<String, Object>) aux).get(com.nordokod.scio.entity.Question.KEY_QUESTION)).toString();
            String id = String.valueOf(index1);
            if (KindOfQuestion.OPEN.getCode() == kindOfQuestion) {
                String openAnswer = Objects.requireNonNull(((HashMap<String, Object>) aux).get(OpenQuestion.KEY_ANSWER)).toString();
                question = new OpenQuestion(id, stringQuestion, kindOfQuestion, openAnswer);
            } else if (KindOfQuestion.TRUE_FALSE.getCode() == kindOfQuestion) {
                boolean trueFalseAnswer = (boolean) ((HashMap<String,Object>)aux).get(TrueFalseQuestion.KEY_ANSWER);
                question = new TrueFalseQuestion(id, stringQuestion, kindOfQuestion, trueFalseAnswer);
            } else {
                question = new MultipleChoiceQuestion(id,stringQuestion,kindOfQuestion);
                ArrayList<Answer> answerArrayList = new ArrayList<>();
                HashMap<String,Object> map = (HashMap<String, Object>) ((HashMap<String,Object>)aux).get(MultipleChoiceQuestion.KEY_ANSWERS);
                for(int i = 0; i < map.size(); i++){
                    HashMap<String,Object> item = (HashMap<String, Object>) map.get(String.valueOf(i));
                    Answer answer = new Answer(
                            item.get("answer").toString(),
                            (boolean)item.get("correct")
                    );
                    answerArrayList.add(answer);
                }
                ((MultipleChoiceQuestion)question).setAnswers(answerArrayList);
            }
            questions.add(question);
            index1++;
        }

        return questions;
    }

}
