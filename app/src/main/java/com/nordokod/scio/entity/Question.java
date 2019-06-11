package com.nordokod.scio.entity;

public class Question {
    private int id;
    private String question;
    private String topic;
    private int category;
    public static String CLOUD_Question="Question";
    public static String CLOUD_KIND_OF_QUESTION="KindOfQuestion";
    public static int QUESTION_MULTIPLE_CHOICE=1;
    public static int QUESTION_TRUEFALSE=2;
    public static int QUESTION_OPEN=3;
    public Question(int id, String question, String topic, int category) {
        this.id = id;
        this.question = question;
        this.topic = topic;
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
