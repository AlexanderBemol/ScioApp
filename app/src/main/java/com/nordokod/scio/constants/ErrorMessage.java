package com.nordokod.scio.constants;

import com.nordokod.scio.R;

public enum ErrorMessage {
    NETWORK_PHONE(R.string.NETWORK_PHONE_TITLE,R.string.NETWORK_PHONE_MESSAGE),
    NETWORK_FIREABSE(R.string.NETWORK_FIREBASE_TITLE,R.string.NETWORK_FIREBASE_MESSAGE),
    AUTH_INVALID_CREDENTIALS(R.string.AUTH_INVALID_CREDENTIALS_TITLE,R.string.AUTH_INVALID_CREDENTIALS_MESSAGE),
    AUTH_SEND_EMAIL(R.string.AUTH_SEND_MAIL_TITLE,R.string.AUTH_SEND_MAIL_MESSAGE),
    AUTH_INVALID_USER(R.string.AUTH_INVALID_USER_TITLE,R.string.AUTH_INVALID_USER_MESSAGE),
    AUTH_USER_COLLISION(R.string.AUTH_USER_COLLISION_TITLE,R.string.AUTH_USER_COLLISION_MESSAGE),
    AUTH_UNKNOW(R.string.AUTH_UNKNOW_TITLE,R.string.AUTH_UNKNOW_MESSAGE),
    FIRESTORE_EXCEPTION(R.string.FIRESTORE_EXCEPTION_TITLE,R.string.FIRESTORE_EXCEPTION_MESSAGE),
    STORAGE_EXCEPTION(R.string.STORAGE_EXCEPTION_TITLE,R.string.STORAGE_EXCEPTION_MESSAGE),
    FIREBASE_EXCEPTION(R.string.FIREBASE_EXCEPTION_TITLE,R.string.FIREBASE_EXCEPTION_MESSAGE),
    FACEBOOK_EXCEPTION(R.string.FACEBOOK_EXCEPTION_TITLE,R.string.FACEBOOK_EXCEPTION_MESSAGE),
    GOOGLE_EXCEPTION(R.string.GOOGLE_EXCEPTION_TITLE,R.string.GOOGLE_EXCEPTION_MESSAGE),
    USER_PERMISSION(R.string.USER_PERMISSION_TITLE,R.string.USER_PERMISSION_MESSAGE),
    CANCELED_OPERATION(R.string.CANCELED_OPERATION_TITLE,R.string.CANCELED_OPERATION_MESSAGE),
    INVALID_INTERN_VALUE(R.string.INVALID_INTERN_VALUE_TITLE,R.string.INVALID_INTERN_VALUE_MESSAGE),
    DATETIME_AFTER(R.string.DATETIME_AFTER_TITLE,R.string.DATETIME_AFTER_MESSAGE),
    DATETIME_BEFORE(R.string.DATETIME_BEFORE_TITLE,R.string.DATETIME_BEFORE_MESSAGE),
    INVALID_MAIL(R.string.INVALID_MAIL_TITLE,R.string.INVALID_MAIL_MESSAGE),
    INVALID_PASSWORD(R.string.INVALID_PASSWORD_TITLE,R.string.INVALID_PASSWORD_MESSAGE),
    INVALID_USERNAME(R.string.INVALID_USERNAME_TITLE,R.string.INVALID_USERNAME_MESSAGE),
    PASSWORDS_DONT_MATCH(R.string.PASSWORDS_DONT_MATCH_TITLE,R.string.PASSWORDS_DONT_MATCH_MESSAGE),
    UNKNOWN_EXCEPTION(R.string.UNKNOW_EXCEPTION_TITLE,R.string.UNKNOW_EXCEPTION_MESSAGE),
    EMPTY_FIELD(R.string.EMPTY_FIELD_TITLE,R.string.EMPTY_FIELD_MESSAGE),
    NO_GUIDES_EXCEPTION(R.string.NO_GUIDES_EXCEPTION_TITLE,R.string.NO_GUIDES_EXCEPTION_MESSAGE),
    NOT_ENOUGH_OPTIONS(R.string.NOT_ENOUGH_OPTIONS_TITLE,R.string.NOT_ENOUGH_OPTIONS_MESSAGE),
    NOT_CORRECT_OPTION_SELECTED(R.string.NOT_CORRECT_OPTION_SELECTED_TITLE,R.string.NOT_CORRECT_OPTION_SELECTED_MESSAGE);

    private int titleCode,messageCode;

    ErrorMessage(int titleCode, int messageCode){
        this.titleCode = titleCode;
        this.messageCode = messageCode;
    }

    public int getTitleCode() { return this.titleCode;}
    public int getMessageCode() { return this.messageCode;}

}