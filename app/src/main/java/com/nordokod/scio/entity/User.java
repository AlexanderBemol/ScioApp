package com.nordokod.scio.entity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.nordokod.scio.constants.Utilities;

public class User {
    public static String KEY_USERS="USERS";
    public static String KEY_USERNAME="USERNAME";
    public static String KEY_EMAIL="EMAIL";
    public static String KEY_PHOTO_PATH="PHOTOPATH";
    public static String KEY_BIRTHDAY="BIRTHDAY";
    public static String KEY_STUDY_LEVEL="STUDY_LEVEL";
    public static String KEY_STATE="STATE";
    public static String KEY_PROFILE_PHOTO="PROFILE_PHOTOS";

    public static String DEFAULT_USERNAME="USUARIO DE SENDO SG";
    public static String DEFAULT_EMAIL="SIN INFORMACIÓN";
    public static Date DEFAULT_BIRTHDAY= new Date();

    private String uid;
    private String username;
    private String email;
    private String password;
    private String password2;
    private String photoPath;
    private Date birthdayDate;
    private int studyLevel;
    private int state;

    public User (){ }

    public User (String uid,String username,String email,String photoPath, Date birthdayDate, int studyLevel, int state){
        this.uid=uid;
        this.username=username;
        this.email=email;
        this.photoPath=photoPath;
        this.birthdayDate=birthdayDate;
        this.studyLevel=studyLevel;
        this.state=state;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws InvalidValueException {
        if(!username.matches(Utilities.USER_REGULAR_EXPRESSION))
            throw new InvalidValueException();
        else
            this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
            this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
            this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
            this.password2 = password2;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Date getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(Date birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public int getStudyLevel() {
        return studyLevel;
    }

    public void setStudyLevel(int studyLevel) {
            this.studyLevel = studyLevel;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
            this.state = state;
    }

}
