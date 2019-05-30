package com.nordokod.scio.entity;

public class Question {
    private String question;
    private String topic;
    private int category;

    public Question(String question, String topic, int category) {
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
}
