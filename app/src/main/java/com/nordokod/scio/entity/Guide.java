package com.nordokod.scio.entity;

public class Guide {

    private int category;
    private String id;
    private String topic;
    private String date;
    private String time;
    private boolean is_actived;

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
}