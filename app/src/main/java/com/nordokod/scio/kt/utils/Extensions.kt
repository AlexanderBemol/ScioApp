package com.nordokod.scio.kt.utils

import com.facebook.FacebookException
import com.google.android.gms.auth.GoogleAuthException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.StorageException
import com.nordokod.scio.kt.constants.GuideException
import com.nordokod.scio.kt.constants.GuideException.Code
import com.nordokod.scio.kt.constants.InputDataException
import com.nordokod.scio.kt.constants.OnlyPremiumException
import com.nordokod.scio.kt.constants.PhoneNetworkException
import com.nordokod.scio.kt.constants.enums.ErrorMessage
import java.lang.Exception

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
        else -> ErrorMessage.UNKNOWN_EXCEPTION
    }
}