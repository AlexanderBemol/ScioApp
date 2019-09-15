package com.nordokod.scio.constants;

public enum ErrorMessage {
    NETWORK_PHONE(1,1),
    NETWORK_FIREABSE(1,2),
    AUTH_INVALID_CREDENTIALS(2,3),
    AUTH_SEND_EMAIL(2,4),
    AUTH_INVALID_USER(2,5),
    AUTH_USER_COLLISION(2,6),
    AUTH_UNKNOW(2,7),
    FIRESTORE_EXCEPTION(3,8),
    STORAGE_EXCEPTION(3,9),
    FIREBASE_EXCEPTION(4,10),
    FACEBOOK_EXCEPTION(2,11),
    GOOGLE_EXCEPTION(2,12),
    USER_PERMISSION(5,13),
    CANCELED_OPERATION(6,14),
    INVALID_INTERN_VALUE(7,15),
    DATETIME_AFTER(8,16),
    DATETIME_BEFORE(8,17),
    INVALID_MAIL(8,18),
    INVALID_PASSWORD(8,19),
    INVALID_USERNAME(8,20),
    PASSWORDS_DONT_MATCH(8,21),
    UNKNOW_EXCEPTION(7,22),
    EMPTY_FIELD(8,23);

    private int titleCode,messageCode;

    ErrorMessage(int titleCode, int messageCode){
        this.titleCode = titleCode;
        this.messageCode = messageCode;
    }

    public int getTitleCode() { return this.titleCode;}
    public int getMessageCode() { return this.messageCode;}

}