package com.nordokod.scio.old.constants;

public enum KindOfQuestion {
    MULTIPLE_CHOICE(1),
    TRUE_FALSE(2),
    OPEN(3);

    private int code;
    KindOfQuestion(int code){
        this.code=code;
    }

    public int getCode() {
        return code;
    }
}
