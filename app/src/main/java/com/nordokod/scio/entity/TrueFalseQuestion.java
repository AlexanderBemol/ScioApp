package com.nordokod.scio.entity;

public class TrueFalseQuestion extends Question {
    private boolean answer;

    public TrueFalseQuestion(String question, String topic, int category, boolean answer) {
        super(question, topic, category);
        this.answer = answer;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
