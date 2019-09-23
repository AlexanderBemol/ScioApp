package com.nordokod.scio.entity;

import android.graphics.Bitmap;

import java.util.Date;

public class Guide {
    public static String KEY_GUIDES="GUIDES";
    public static String KEY_PERSONAL_GUIDES="PERSONAL_GUIDES";
    public static String KEY_UID="UID";
    public static String KEY_ONLINE="ONLINE";
    public static String KEY_CATEGORY="CATEGORY";
    public static String KEY_ID="ID";
    public static String KEY_TOPIC="TOPIC";
    public static String KEY_DATETIME="DATETIME";
    public static String KEY_ACTIVATED="ACTIVATED";

    private int category;
    private String id;
    private String topic;
    private String UID;
    private boolean online;
    private boolean activated;
    private Date datetime;

    public Guide(int category, String id, String topic, String UID, boolean online, boolean activated, Date datetime) {
        this.category = category;
        this.id = id;
        this.topic = topic;
        this.UID = UID;
        this.online = online;
        this.activated = activated;
        this.datetime = datetime;
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
        return this.datetime.compareTo(new Date());
    }
}
