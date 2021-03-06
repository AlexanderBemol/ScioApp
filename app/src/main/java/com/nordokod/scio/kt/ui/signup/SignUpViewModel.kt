package com.nordokod.scio.kt.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nordokod.scio.kt.constants.*
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.enums.SuccessMessage
import com.nordokod.scio.kt.model.repository.IAuthRepository
import com.nordokod.scio.kt.utils.Event
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.suspendCoroutine

class SignUpViewModel(private val authRepository: IAuthRepository) : ViewModel() {
    val error = MutableLiveData<Exception>()
    val successMessage = MutableLiveData<SuccessMessage>()
    val signUpAction = MutableLiveData<SignUpActions>()

    fun signUp(mail: String, password1: String, password2: String){
        if (mail != "" && password1 != "" && password2 != ""){
            if(password1 == password2){
                if(password1.length >= 8){
                    viewModelScope.launch{
                        try{
                            withTimeout(Generic.TIMEOUT_VALUE){
                                when(val result = authRepository.signUpWithMail(mail,password1)){
                                    is TaskResult.Success -> {
                                        successMessage.value = SuccessMessage.SIGN_UP_USER
                                        signUpAction.value = SignUpActions.GO_TO_VERIFY_MAIL
                                    }
                                    is TaskResult.Error -> error.value = result.e
                                }
                            }
                        } catch (e: Exception){
                            error.value = e
                        }
                    }
                } else error.value = InputDataException(InputDataException.Code.INVALID_PASSWORD)
            } else error.value = InputDataException(InputDataException.Code.PASSWORDS_NOT_MATCH)
        } else error.value = InputDataException(InputDataException.Code.EMPTY_FIELD)
    }


}