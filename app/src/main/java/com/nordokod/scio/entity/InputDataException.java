package com.nordokod.scio.entity;

import com.nordokod.scio.constants.ErrorMessage;

public class InputDataException extends Exception {
    public enum Code{
        DATETIME_AFTER,
        DATETIME_BEFORE,
        INVALID_MAIL,
        INVALID_PASSWORD,
        INVALID_USERNAME,
        PASSWORDS_DONT_MATCH;
    }
    private Code code;
    InputDataException(Code code){
        this.code=code;
    }

    public Code getCode() {
        return code;
    }
}
