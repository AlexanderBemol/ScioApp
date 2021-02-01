package com.nordokod.scio.kt.constants

class UnknownException: Exception()

class IrrelevantException: Exception()

class PhoneNetworkException: Exception()

class OnlyPremiumException: Exception()

class GuideException(val code: Code): Exception(){
    enum class Code{
        NO_GUIDES, NO_QUESTIONS_IN_GUIDE,GUIDE_NOT_AVAILABLE
    }
}

class InputDataException(val code: Code): Exception(){
    enum class Code {
        DATETIME_AFTER, DATETIME_BEFORE, INVALID_MAIL, INVALID_PASSWORD, INVALID_USERNAME, PASSWORDS_NOT_MATCH, EMPTY_FIELD, NOT_ENOUGH_OPTIONS, NOT_CORRECT_OPTION_SELECTED
    }
}