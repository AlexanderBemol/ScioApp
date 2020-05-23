package com.nordokod.scio.entity;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.HashMap;

public class MultipleChoiceQuestion extends Question {
    public static String KEY_ANSWERS="ANSWERS";
    public static String KEY_ANSWER="ANSWER";
    public static String KEY_CORRECT="CORRECT";

    private ArrayList<Answer> answers;

    public MultipleChoiceQuestion(String guide, int id, String question, int kindOfQuestions) {
        super(guide,id,question,kindOfQuestions);
        answers=new ArrayList<>();
    }

    public void addAnswer(String option, boolean is_correct) {
        answers.add(new Answer(option, is_correct));
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }
    public void setAnswers(ArrayList<Answer> answersCollection) {
        this.answers = answersCollection;
    }
}
