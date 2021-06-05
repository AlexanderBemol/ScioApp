package com.nordokod.scio.kt.utils

import android.view.View
import com.facebook.FacebookException
import com.google.android.gms.auth.GoogleAuthException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.google.firebase.crashlytics.*
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.StorageException
import com.nordokod.scio.R
import com.nordokod.scio.kt.constants.*
import com.nordokod.scio.kt.constants.GuideException.Code
import com.nordokod.scio.kt.constants.enums.ErrorMessage
import com.nordokod.scio.kt.constants.enums.GuideCategory
import kotlinx.android.synthetic.main.list_categories.*
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

fun Exception.getEnumErrorMessage(): ErrorMessage {
    return when (this) {
        is PhoneNetworkException -> ErrorMessage.NETWORK_PHONE
        is FirebaseNetworkException -> ErrorMessage.NETWORK_FIREBASE
        is FirebaseAuthInvalidCredentialsException -> ErrorMessage.AUTH_INVALID_CREDENTIALS
        is FirebaseAuthEmailException -> ErrorMessage.AUTH_SEND_EMAIL
        is FirebaseAuthInvalidUserException -> {
            when (this.errorCode) {
                "ERROR_USER_NOT_FOUND" -> ErrorMessage.AUTH_USER_NOT_FOUND
                else -> ErrorMessage.AUTH_INVALID_USER
            }
        }
        is FirebaseAuthUserCollisionException -> ErrorMessage.AUTH_USER_COLLISION
        is FirebaseAuthException -> ErrorMessage.AUTH_UNKNOWN
        is FirebaseFirestoreException -> ErrorMessage.FIRESTORE_EXCEPTION
        is StorageException -> ErrorMessage.STORAGE_EXCEPTION
        is FirebaseException -> ErrorMessage.FIREBASE_EXCEPTION
        is FacebookException -> ErrorMessage.FACEBOOK_EXCEPTION
        is GoogleAuthException -> ErrorMessage.GOOGLE_EXCEPTION
        is OnlyPremiumException -> ErrorMessage.USER_NOT_PREMIUM
        is GuideException -> {
            when (this.code) {
                Code.GUIDE_NOT_AVAILABLE -> ErrorMessage.GUIDE_NOT_AVAILABLE
                Code.NO_GUIDES -> ErrorMessage.NO_GUIDES_EXCEPTION
                Code.NO_QUESTIONS_IN_GUIDE -> ErrorMessage.NO_QUESTIONS_IN_GUIDE
            }
        }
        is InputDataException -> {
            when (this.code) {
                InputDataException.Code.DATETIME_AFTER -> ErrorMessage.DATETIME_AFTER
                InputDataException.Code.DATETIME_BEFORE -> ErrorMessage.DATETIME_BEFORE
                InputDataException.Code.INVALID_MAIL -> ErrorMessage.INVALID_MAIL
                InputDataException.Code.INVALID_PASSWORD -> ErrorMessage.INVALID_PASSWORD
                InputDataException.Code.INVALID_USERNAME -> ErrorMessage.INVALID_USERNAME
                InputDataException.Code.PASSWORDS_NOT_MATCH -> ErrorMessage.PASSWORDS_NOT_MATCH
                InputDataException.Code.EMPTY_FIELD -> ErrorMessage.EMPTY_FIELD
                InputDataException.Code.NOT_ENOUGH_OPTIONS -> ErrorMessage.NOT_ENOUGH_OPTIONS
                InputDataException.Code.NOT_CORRECT_OPTION_SELECTED -> ErrorMessage.NOT_CORRECT_OPTION_SELECTED
            }
        }
        is IrrelevantException -> ErrorMessage.IRRELEVANT_EXCEPTION
        else -> ErrorMessage.UNKNOWN_EXCEPTION
    }
}

fun Exception.recordException(){
    if(this.getEnumErrorMessage().getRecordException())
        FirebaseCrashlytics.getInstance().recordException(this)
}

fun Int.formatToNDigits(n : Int) = this.toString().padStart(n , '0')

fun Int.getMonthName() : Int {
    return when(this){
        0 -> R.string.month_january
        1 -> R.string.month_february
        2 -> R.string.month_march
        3 -> R.string.month_april
        4 -> R.string.month_may
        5 -> R.string.month_june
        6 -> R.string.month_july
        7 -> R.string.month_august
        8 -> R.string.month_september
        9 -> R.string.month_october
        10 -> R.string.month_november
        else -> R.string.month_december
    }
}

fun View.toGuideCategory() : GuideCategory {
    return when(this.id){
        R.id.CL_Art -> GuideCategory.ART
        R.id.CL_Entertainment -> GuideCategory.ENTERTAINMENT
        R.id.CL_Exacts -> GuideCategory.EXACT_SCIENCES
        R.id.CL_Socials -> GuideCategory.SOCIAL_SCIENCES
        R.id.CL_Sports -> GuideCategory.SPORTS
        R.id.CL_Tech -> GuideCategory.TECH
        else -> GuideCategory.OTHERS
    }
}

fun Date.daysLeft() : Int {
    return if(this.after(Date())){
        val leftTime = this.time - Calendar.getInstance().timeInMillis
        TimeUnit.MILLISECONDS.toDays(leftTime).toInt()
    } else 0
}