package com.nordokod.scio.constants;

public enum UserState {
    FREE(1,"FREE"),
    PREMIUM(2,"PREMIUM"),
    SUSPENDED(3,"SUSPENDED");
    private int userStateCode;
    private  String userStateDescription;
    UserState(int userStateCode, String userStateDescription ){
        this.userStateCode=userStateCode;
        this.userStateDescription=userStateDescription;
    }
    public int getCode(){return userStateCode;}
    public String getDescription(){return userStateDescription;}
}
