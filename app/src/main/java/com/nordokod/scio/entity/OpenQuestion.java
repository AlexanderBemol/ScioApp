package com.nordokod.scio.entity;

public class OpenQuestion extends Question {

    private String answer;

    public OpenQuestion(int id, String question, String topic, int category, String answer) {
        super(id, question, topic, category);
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
