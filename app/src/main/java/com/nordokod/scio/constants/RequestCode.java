package com.nordokod.scio.constants;

public enum RequestCode {
    RC_GOOGLE(9001),
    RC_GALLERY(101),
    RC_CAMERA(102),
    RC_UPDATE_INMEDIATE(103),
    RC_UPDATE_FLEXIBLE(104);

    private int code;
    RequestCode(int code){
        this.code=code;
    }
    public int getCode() {
        return code;
    }
}
