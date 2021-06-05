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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.lang.Exception

class SignUpViewModel(private val authRepository: IAuthRepository) : ViewModel() {
    val error = MutableLiveData<Event<Exception>>()
    val successMessage = MutableLiveData<Event<SuccessMessage>>()
    val signUpAction = MutableLiveData<Event<SignUpActions>>()

    fun signUp(mail: String, password1: String, password2: String){
        if (mail != "" && password1 != "" && password2 != ""){
            if(password1 == password2){
                if(password1.length >= 8){
                    viewModelScope.launch{
                        try{
                            withTimeout(Generic.TIMEOUT_VALUE){
                                when(val result = authRepository.signUpWithMail(mail,password1)){
                                    is TaskResult.Success -> {
                                        successMessage.value = Event(SuccessMessage.SIGN_UP_USER)
                                        signUpAction.value = Event(SignUpActions.GO_TO_VERIFY_MAIL)
                                    }
                                    is TaskResult.Error -> error.value = Event(result.e)
                                }
                            }
                        } catch (e: Exception){
                            error.value = Event(e)
                        }
                    }
                } else error.value = Event(InputDataException(InputDataException.Code.INVALID_PASSWORD))
            } else error.value = Event(InputDataException(InputDataException.Code.PASSWORDS_NOT_MATCH))
        } else error.value = Event(InputDataException(InputDataException.Code.EMPTY_FIELD))
    }

}