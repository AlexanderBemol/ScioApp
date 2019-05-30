package com.nordokod.scio.entity;

public class TrueFalseQuestion extends Question {
    private boolean answer;

    public TrueFalseQuestion(String question, boolean answer) {
        super(question);
        this.answer = answer;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
