package com.nordokod.scio.Entidad;

import java.util.Date;

public class User {
    private String username;
    private String email;
    private String password;
    private String photoPath;
    private Date birthDay;
    private String educationalLevel;

    public String fbsUID = "UID";//cadenas identificadoras para firebase
    public String fbsBirthDay = "birthDay";
    public String fbsEducationalLevel = "educationalLevel";
    public String fbsUsersCollection = "Users";


    public String getPhotoPath() {
        return photoPath;
    }
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getEducationalLevel() {
        return educationalLevel;
    }

    public void setEducationalLevel(String educationalLevel) {
        this.educationalLevel = educationalLevel;
    }
}
