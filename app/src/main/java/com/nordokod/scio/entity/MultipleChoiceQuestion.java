package com.nordokod.scio.entity;

import java.util.ArrayList;

public class MultipleChoiceQuestion extends Question {

    private ArrayList<String> answer;
    private int correct_answer_index;

    public MultipleChoiceQuestion(String question, String topic, int category, ArrayList<String> answer, int correct_answer_index) {
        super(question, topic, category);
        this.answer = answer;
        this.correct_answer_index = correct_answer_index;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<String> answer) {
        this.answer = answer;
    }

    public int getCorrect_answer_index() {
        return correct_answer_index;
    }

    public void setCorrect_answer_index(int correct_answer_index) {
        this.correct_answer_index = correct_answer_index;
    }
}
