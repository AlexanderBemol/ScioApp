package com.nordokod.scio.entity;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

public class MultipleChoiceQuestion extends Question {
    public static String KEY_ANSWERS="ANSWERS";
    public static String KEY_ANSWER="ANSWER";
    public static String KEY_CORRECT="CORRECT";

    private ArrayList<Answer> answers;

    public MultipleChoiceQuestion(String id, String question, int kindOfQuestions) {
        super(id, question,kindOfQuestions);
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

    public class Answer {
        private String answer;
        private boolean correct;

        Answer(String answer, boolean correct) {
            this.answer = answer;
            this.correct = correct;
        }

        public String getAnswer() {
            return answer;
        }

        public boolean isCorrect() {
            return correct;
        }


    }
}
