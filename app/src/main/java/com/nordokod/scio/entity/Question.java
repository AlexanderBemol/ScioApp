package com.nordokod.scio.entity;

public class Question {
    private int id;
    private String question;
    private int kindOfQuestion;
    public static String KEY_QUESTION="QUESTION";
    public static String KEY_KIND_OF_QUESTION="KIND_OF_QUESTION";
    public Question(int id, String question, int kindOfQuestion) {
        this.id = id;
        this.question = question;
        this.kindOfQuestion=kindOfQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKindOfQuestion() {
        return kindOfQuestion;
    }

    public void setKindOfQuestion(int kindOfQuestion) {
        this.kindOfQuestion = kindOfQuestion;
    }
}
