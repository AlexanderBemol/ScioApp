package com.nordokod.scio.entity;
import java.util.Date;

import com.nordokod.scio.constants.StudyLevel;
import com.nordokod.scio.constants.UserState;
import com.nordokod.scio.constants.Utilities;

public class User {
    public static String KEY_USERS="USERS";
    public static String KEY_BIRTHDAY="BIRTHDAY";
    public static String KEY_STUDY_LEVEL="STUDY_LEVEL";
    public static String KEY_STATE="STATE";
    public static String KEY_PROFILE_PHOTO="PROFILE_PHOTOS";

    private String uid;
    private String username;
    private String email;
    private String password;
    private String password2;
    private String photoPath;
    private Date birthdayDate;
    private int studyLevel;
    private int state;

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

    public void setEmail(String email) throws InvalidValueException {
        if(!email.matches(Utilities.EMAIL_REGULAR_EXPRESSION))
            throw new InvalidValueException();
        else
            this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws InvalidValueException {
        if(password.length()<8)
            throw new InvalidValueException();
        else
            this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) throws InvalidValueException {
        if(password2.length()<8)
            throw new InvalidValueException();
        else
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

    public void setStudyLevel(int studyLevel) throws InvalidValueException {
        try{
            StudyLevel studyLevel1 = StudyLevel.values()[studyLevel+1];
            this.studyLevel = studyLevel;
        }catch (ArrayIndexOutOfBoundsException e){
            throw new InvalidValueException();
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) throws InvalidValueException {
        try{
            UserState userState = UserState.values()[state+1];
            this.state = state;
        }catch (ArrayIndexOutOfBoundsException e){
            throw new InvalidValueException();
        }
    }

}
