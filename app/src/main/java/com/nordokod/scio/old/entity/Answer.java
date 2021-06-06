package com.nordokod.scio.old.entity;

import java.io.Serializable;
public class Answer implements Serializable {
    private String answer;
    private boolean correct;
    public Answer(String answer, boolean correct) {
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
