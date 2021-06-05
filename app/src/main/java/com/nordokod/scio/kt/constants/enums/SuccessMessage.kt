package com.nordokod.scio.kt.constants.enums

import android.content.Context
import com.nordokod.scio.R
import es.dmoral.toasty.Toasty

enum class SuccessMessage(private val messageCode: Int) {
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
    LOGIN_USER(R.string.LOGIN_USER),
    IMPORT_GUIDE(R.string.IMPORT_GUIDE),
    SAVE_CONFIGURATION(R.string.SAVE_CONFIGURATION),
    RESEND_VERIFICATION_MAIL(R.string.RESEND_VERIFICATION_MAIL),
    MAIL_VERIFIED(R.string.MAIL_VERIFICATED),
    DATA_EXPORTED(R.string.DATA_EXPORTED),
    LOG_OUT_USER(R.string.LOG_OUT_USER);

    fun showMessage(context: Context){
        Toasty.success(context,context.getString(this.messageCode)).show()
    }
}