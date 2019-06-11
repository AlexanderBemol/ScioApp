package com.nordokod.scio.entity;

import android.graphics.Bitmap;

import java.util.Date;

public class Guide {
    public static String KEY_UID="UID";
    public static String KEY_ONLINE="Online";
    public static String KEY_CATEGORY="Category";
    public static String KEY_ID="Id";
    public static String KEY_TOPIC="Topic";
    public static String KEY_DATETIME="Datetime";
    public static String KEY_DATE="Date";
    public static String KEY_TIME="Time";
    public static String KEY_ACTIVATED="Activated";
    private int category;
    private String id;
    private String topic;
    private String date;
    private String time;
    private String UID;
    private boolean Online;
    private boolean is_actived;
    private Date datetime;

    private String user_Name;
    private Bitmap user_photo;

    public Guide(int category, String id, String topic, String date, String time, boolean is_actived) {
        this.category = category;
        this.id = id;
        this.topic = topic;
        this.date = date;
        this.time = time;
        this.is_actived = is_actived;
    }

    public Guide() {

    }

    public void setUserData(String user_Name, Bitmap user_photo) {
        this.user_Name = user_Name;
        this.user_photo = user_photo;
    }

    public String getUser_Name() {
        return user_Name;
    }

    public Bitmap getUser_photo() {
        return user_photo;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIs_actived() {
        return is_actived;
    }

    public void setIs_actived(boolean is_actived) {
        this.is_actived = is_actived;
    }

    public int getDaysLeft() {
        return 0;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public boolean isOnline() {
        return Online;
    }

    public void setOnline(boolean online) {
        Online = online;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
