package com.nordokod.scio.kt.constants.enums

import android.content.Context
import com.nordokod.scio.R
import es.dmoral.toasty.Toasty

enum class ErrorMessage(private val kindOfToast: Int, private val messageCode: Int, private val recordException: Boolean) {
    NETWORK_PHONE(2, R.string.NETWORK_PHONE_MESSAGE,false),
    NETWORK_FIREBASE(2,R.string.NETWORK_FIREBASE_MESSAGE,false),
    AUTH_INVALID_CREDENTIALS(2,R.string.AUTH_INVALID_CREDENTIALS_MESSAGE,false),
    AUTH_SEND_EMAIL(2,R.string.AUTH_SEND_MAIL_MESSAGE,true),
    AUTH_INVALID_USER(2,R.string.AUTH_INVALID_USER_MESSAGE,false),
    AUTH_USER_COLLISION(2,R.string.AUTH_USER_COLLISION_MESSAGE,false),
    AUTH_UNKNOWN(2,R.string.AUTH_UNKNOW_MESSAGE,true),
    FIRESTORE_EXCEPTION(2,R.string.FIRESTORE_EXCEPTION_MESSAGE,true),
    STORAGE_EXCEPTION(2,R.string.STORAGE_EXCEPTION_MESSAGE,true),
    FIREBASE_EXCEPTION(2,R.string.FIREBASE_EXCEPTION_MESSAGE,true),
    FACEBOOK_EXCEPTION(2,R.string.FACEBOOK_EXCEPTION_MESSAGE,false),
    GOOGLE_EXCEPTION(2,R.string.GOOGLE_EXCEPTION_MESSAGE,false),
    USER_NOT_PREMIUM(1,R.string.USER_PERMISSION_MESSAGE,false),
    DATETIME_AFTER(1,R.string.DATETIME_AFTER_MESSAGE,false),
    DATETIME_BEFORE(1,R.string.DATETIME_BEFORE_MESSAGE,false),
    INVALID_MAIL(1,R.string.INVALID_MAIL_MESSAGE,false),
    INVALID_PASSWORD(1,R.string.INVALID_PASSWORD_MESSAGE,false),
    INVALID_USERNAME(1,R.string.INVALID_USERNAME_MESSAGE,false),
    PASSWORDS_NOT_MATCH(1,R.string.PASSWORDS_DONT_MATCH_MESSAGE,false),
    UNKNOWN_EXCEPTION(2,R.string.UNKNOW_EXCEPTION_MESSAGE,true),
    EMPTY_FIELD(1,R.string.EMPTY_FIELD_MESSAGE,false),
    NO_GUIDES_EXCEPTION(1,R.string.NO_GUIDES_EXCEPTION_MESSAGE,false),
    NOT_ENOUGH_OPTIONS(1,R.string.NOT_ENOUGH_OPTIONS_MESSAGE,false),
    NOT_CORRECT_OPTION_SELECTED(1,R.string.NOT_CORRECT_OPTION_SELECTED_MESSAGE,false),
    AUTH_USER_NOT_FOUND(2,R.string.AUTH_USER_NOT_FOUND_MESSAGE,false),
    NO_QUESTIONS_IN_GUIDE(2,R.string.NO_QUESTIONS_IN_GUIDE_MESSAGE,false),
    GUIDE_NOT_AVAILABLE(2,R.string.NO_QUESTIONS_IN_GUIDE_MESSAGE,false),
    IRRELEVANT_EXCEPTION(1,R.string.UNKNOW_EXCEPTION_MESSAGE,false);

    fun showMessage(context: Context){
        if(this.kindOfToast==2) Toasty.error(context,context.getString(this.messageCode)).show()
        else Toasty.warning(context,context.getString(this.messageCode)).show()
    }
    fun getRecordException() = this.recordException
}