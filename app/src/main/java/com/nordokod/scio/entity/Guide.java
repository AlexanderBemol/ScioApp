package com.nordokod.scio.entity;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.DAYS;


public class Guide {
    public static String KEY_GUIDES="GUIDES";
    public static String KEY_PERSONAL_GUIDES="PERSONAL_GUIDES";
    public static String KEY_ONLINE="ONLINE";
    public static String KEY_CATEGORY="CATEGORY";
    public static String KEY_TOPIC="TOPIC";
    public static String KEY_DATETIME="DATETIME";
    public static String KEY_ACTIVATED="ACTIVATED";
    public static String KEY_PERSONAL="PERSONAL";

    private int category;
    private String id;
    private String topic;
    private String UID;
    private boolean online;
    private boolean activated;
    private boolean personal;
    private Date datetime;

    private ArrayList<Question> questions;

    public Guide(int category, String id, String topic, String UID, boolean online, boolean activated,boolean personal, Date datetime) {
        this.category = category;
        this.id = id;
        this.topic = topic;
        this.UID = UID;
        this.online = online;
        this.activated = activated;
        this.datetime = datetime;
        this.personal = personal;
        this.questions = new ArrayList<>();
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getDaysLeft(){
        return (int)( (datetime.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24));
    }

    public boolean isPersonal() {
        return personal;
    }

    public void setPersonal(boolean personal) {
        this.personal = personal;
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
    public void addQuestion(Question question){
        this.questions.add(question);
    }

}
