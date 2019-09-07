package com.nordokod.scio.entity;

import com.nordokod.scio.constants.ErrorMessage;

public class InputDataException extends Exception {
    private ErrorMessage errorMessage;
    InputDataException(ErrorMessage errorMessage){
        this.errorMessage=errorMessage;
    }
    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
