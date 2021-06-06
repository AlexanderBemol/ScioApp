package com.nordokod.scio.old.entity;

public class OpenQuestion extends Question {
    public static String KEY_ANSWER="ANSWER";

    private String answer;

    public OpenQuestion(String guide,int id, String question, int kindOfQuestion, String answer) {
        super(guide, id, question, kindOfQuestion);
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
