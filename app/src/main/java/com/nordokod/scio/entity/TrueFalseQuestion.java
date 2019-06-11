package com.nordokod.scio.entity;

public class TrueFalseQuestion extends Question {
    public static String CLOUD_ANSWER="Answer";
    private boolean answer;

    public TrueFalseQuestion(int id, String question, String topic, int category, boolean answer) {
        super(id, question, topic, category);
        this.answer = answer;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
