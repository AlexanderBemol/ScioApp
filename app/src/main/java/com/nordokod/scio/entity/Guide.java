package com.nordokod.scio.entity;
import java.util.ArrayList;
import java.util.Date;


public class Guide {
    public static String KEY_GUIDES="GUIDES";
    public static String KEY_PERSONAL_GUIDES="PERSONAL_GUIDES";
    public static String KEY_ONLINE="ONLINE";
    public static String KEY_CATEGORY="CATEGORY";
    public static String KEY_TOPIC="TOPIC";
    public static String KEY_DATETIME="DATETIME";
    public static String KEY_ACTIVATED="ACTIVATED";
    public static String KEY_CREATION_DATE="CREATION_DATE";
    public static String KEY_UPDATE_DATE="UPDATE_DATE";
    public static String KEY_CREATION_USER="CREATION_USER";
    public static String KEY_UPDATE_USER="UPDATE_USER";


    private int category;
    private String id;
    private String topic;
    private String UID;
    private boolean online;
    private boolean activated;
    private Date testDatetime;
    private Date creationDate;
    private Date updateDate;
    private String creationUser;
    private String updateUser;
    private ArrayList<Question> questions;

    public Guide(int category, String id, String topic, String UID, boolean online, boolean activated, Date testDatetime, Date creationDate, Date updateDate, String creationUser, String updateUser) {
        this.category = category;
        this.id = id;
        this.topic = topic;
        this.UID = UID;
        this.online = online;
        this.activated = activated;
        this.testDatetime = testDatetime;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.creationUser = creationUser;
        this.updateUser = updateUser;
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

    public Date getTestDatetime() {
        return testDatetime;
    }

    public void setTestDatetime(Date testDatetime) {
        this.testDatetime = testDatetime;
    }

    public int getDaysLeft(){
        return (int)( (testDatetime.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24));
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
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
