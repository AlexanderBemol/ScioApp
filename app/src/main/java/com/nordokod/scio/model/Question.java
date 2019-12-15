package com.nordokod.scio.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.nordokod.scio.constants.KindOfQuestion;
import com.nordokod.scio.entity.Guide;
import com.nordokod.scio.entity.MultipleChoiceQuestion;
import com.nordokod.scio.entity.OpenQuestion;
import com.nordokod.scio.entity.TrueFalseQuestion;

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
    public Task<DocumentReference> addQuestion(KindOfQuestion kindOfQuestion, Guide guide, com.nordokod.scio.entity.Question question){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference questionCollection=db.collection(Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection(Guide.KEY_PERSONAL_GUIDES).document(guide.getId()).collection(com.nordokod.scio.entity.Question.KEY_QUESTIONS);
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
        }
        return questionCollection.add(data);
    }

    public Task<Void> addMultipleAnswersToQuestion (ArrayList<MultipleChoiceQuestion.Answer> answers, DocumentReference questionDocument){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();

        for (MultipleChoiceQuestion.Answer x : answers) {
            DocumentReference documentReference = questionDocument.collection(MultipleChoiceQuestion.KEY_ANSWERS).document();
            Map<String,Object> data = new HashMap<>();
            data.put(MultipleChoiceQuestion.KEY_ANSWER,x.getAnswer());
            data.put(MultipleChoiceQuestion.KEY_CORRECT,x.isCorrect());
            batch.set(documentReference,data);
        }
        return batch.commit();
    }
    /**
     * eliminar pregunta de la guía
     * @param guide entidad guide con id de guia
     * @param question entidad question con id de pregunta
     * @return task con el resultado
     */
    public Task<Void> deleteQuestion(Guide guide, com.nordokod.scio.entity.Question question) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference questionCollection = db.collection(Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection(Guide.KEY_PERSONAL_GUIDES).document(guide.getId()).collection(com.nordokod.scio.entity.Question.KEY_QUESTIONS);
        return questionCollection.document(String.valueOf(question.getId())).delete();
    }

    /**
     * obtiene todas las preguntas de una guía
     * @param guide entidad guide con su id
     * @return task con el resultado
     */
    public Task<QuerySnapshot> getSnapshotQuestionsOfGuide(Guide guide){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference questionCollection = db.collection(Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection(Guide.KEY_PERSONAL_GUIDES).document(guide.getId()).collection(com.nordokod.scio.entity.Question.KEY_QUESTIONS);
        return questionCollection.get();
    }

    public ArrayList<com.nordokod.scio.entity.Question> getQuestionsFromSnapshot(QuerySnapshot queryDocumentSnapshot) throws InterruptedException {
        ArrayList<com.nordokod.scio.entity.Question> questions = new ArrayList<>();
        final Object syncObject = new Object();
        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshot) {
            com.nordokod.scio.entity.Question question;
            int kindOfQuestion = (int) documentSnapshot.get(com.nordokod.scio.entity.Question.KEY_KIND_OF_QUESTION);
            String stringQuestion = Objects.requireNonNull(documentSnapshot.get(com.nordokod.scio.entity.Question.KEY_QUESTION)).toString();
            String id = documentSnapshot.getId();
            if (KindOfQuestion.OPEN.getCode() == kindOfQuestion) {
                String openAnswer = Objects.requireNonNull(documentSnapshot.get(OpenQuestion.KEY_ANSWER)).toString();
                question = new OpenQuestion(id, stringQuestion, kindOfQuestion, openAnswer);
            } else if (KindOfQuestion.TRUE_FALSE.getCode() == kindOfQuestion) {
                boolean trueFalseAnswer = (boolean) Objects.requireNonNull(documentSnapshot.get(TrueFalseQuestion.KEY_ANSWER));
                question = new TrueFalseQuestion(id, stringQuestion, kindOfQuestion, trueFalseAnswer);
            } else {
                CollectionReference collectionReference = (CollectionReference) documentSnapshot.get(MultipleChoiceQuestion.KEY_ANSWERS);
                question = new MultipleChoiceQuestion(id,stringQuestion,kindOfQuestion);
                Objects.requireNonNull(collectionReference).get()
                        .addOnSuccessListener(doc->{
                            for(DocumentSnapshot mcAnswer:doc){
                                ((MultipleChoiceQuestion)question).addAnswer(Objects.requireNonNull(mcAnswer.get(MultipleChoiceQuestion.KEY_ANSWER)).toString(), (boolean) mcAnswer.get(MultipleChoiceQuestion.KEY_CORRECT));
                            }
                            synchronized (syncObject){
                                syncObject.notify();
                            }
                        });
            }
        }
        synchronized (syncObject){
            syncObject.wait();
        }
        return questions;
    }

}
