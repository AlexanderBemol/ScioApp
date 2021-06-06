package com.nordokod.scio.old.entity;

public class TrueFalseQuestion extends Question {
    public static String KEY_ANSWER="ANSWER";
    private boolean answer;

    public TrueFalseQuestion(String guide,int id, String question, int kindOfQuestion, boolean answer) {
        super(guide,id,question,kindOfQuestion);
        this.answer = answer;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
