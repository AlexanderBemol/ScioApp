package com.nordokod.scio.entity;

import com.nordokod.scio.constants.Exceptions;

public class InvalidValueException extends  Exception {
    public String ExceptionCode;
    InvalidValueException(){
    }
    public String getExceptionCode(){
        return Exceptions.INVALID_VALUE_EXCEPTION.getCode();
    }
}
