package com.nordokod.scio.constants;

public enum Provider {
    FACEBOOK(1,"FACEBOOK"),
    GOOGLE(2,"GOOGLE"),
    MAIL(3,"MAIL");
    private int code;
    private String description;
    Provider(int code,String description){
        this.code=code;
        this.description=description;
    }
    public int getCode() {
        return code;
    }
    public String getDescription(){return description;}
}
