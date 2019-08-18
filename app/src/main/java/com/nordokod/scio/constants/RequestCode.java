package com.nordokod.scio.constants;

public enum RequestCode {
    RC_GOOGLE(9001);
    int code;
    RequestCode(int code){
        this.code=code;
    }
    public int getCode() {
        return code;
    }
}
