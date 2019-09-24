package com.nordokod.scio.constants;

import com.nordokod.scio.R;

public enum UserOperations {
    //create, update, delete
    //guide, question, user

    CREATE_GUIDE(R.string.CREATE_GUIDE),
    UPDATE_GUIDE(R.string.UPDATE_GUIDE),
    DELETE_GUIDE(R.string.DELETE_GUIDE),
    CREATE_QUESTION(R.string.CREATE_QUESTION),
    UPDATE_QUESTION(R.string.UPDATE_QUESTION),
    DELETE_QUESTION(R.string.DELETE_QUESTION),
    CREATE_USER(R.string.CREATE_USER),
    UPDATE_USER(R.string.UPDATE_USER),
    DELETE_USER(R.string.DELETE_USER),
    SIGN_UP_USER(R.string.SIGN_UP_USER),
    LOGIN_USER(R.string.LOGIN_USER);

    private int messageCode;

    UserOperations(int messageCode){

        this.messageCode = messageCode;
    }

    public int getMessageCode(){ return this.messageCode; }
}
