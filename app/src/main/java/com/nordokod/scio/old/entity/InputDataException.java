package com.nordokod.scio.old.entity;

public class InputDataException extends Exception {
    public enum Code{
        DATETIME_AFTER,
        DATETIME_BEFORE,
        INVALID_MAIL,
        INVALID_PASSWORD,
        INVALID_USERNAME,
        PASSWORDS_DONT_MATCH,
        EMPTY_FIELD,
        NOT_ENOUGH_OPTIONS,
        NOT_CORRECT_OPTION_SELECTED
    }
    private Code code;
    public InputDataException(Code code){
        this.code=code;
    }

    public Code getCode() {
        return code;
    }
}
