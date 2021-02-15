package com.nordokod.scio.kt.ui.login
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.InputDataException
import com.nordokod.scio.kt.constants.UnknownException
import com.nordokod.scio.kt.constants.enums.SuccessMessage
import com.nordokod.scio.kt.model.repository.IAuthRepository
import com.nordokod.scio.kt.utils.Event
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.Exception

class LoginViewModel(private val authRepository: IAuthRepository): ViewModel() {
    val error = MutableLiveData<Event<Exception>>()
    val successMessage = MutableLiveData<Event<SuccessMessage>>()
    val loginAction = MutableLiveData<Event<LoginActions>>()

    fun signInWithMail(email: String, password: String){
        if(email.isBlank()||password.isBlank()){
            error.value = Event(InputDataException(InputDataException.Code.EMPTY_FIELD))
        }else{
            viewModelScope.launch {
                try{
                    withTimeout(Generic.TIMEOUT_VALUE){
                        when(val result = authRepository.signInWithMail(email, password)){
                            is TaskResult.Error -> error.value = Event(result.e)
                            is TaskResult.Success -> {
                                val user = result.data
                                successMessage.value = Event(SuccessMessage.LOGIN_USER)
                                if(!user.emailVerified) loginAction.value = Event(LoginActions.GO_TO_VERIFY_MAIL)
                                else loginAction.value = Event(LoginActions.GO_TO_MAIN)
                            }
                        }
                    }
                }catch (e: TimeoutCancellationException){
                    error.value = Event(UnknownException())
                }
            }
        }
    }

    fun loginWithFacebook(token: String){
        if(token=="") error.value = Event(UnknownException())
        else{
            viewModelScope.launch {
                try {
                    withTimeout(Generic.TIMEOUT_VALUE){
                        when(val result = authRepository.signInWithFacebook(token)){
                            is TaskResult.Success -> {
                                val user = result.data
                                if(user.newUser) {
                                    successMessage.value = Event(SuccessMessage.SIGN_UP_USER)
                                    loginAction.value = Event(LoginActions.GO_TO_MAIN)
                                } else {
                                    successMessage.value = Event(SuccessMessage.LOGIN_USER)
                                    loginAction.value = Event(LoginActions.GO_TO_MAIN)
                                }

                            }
                            is TaskResult.Error -> {
                                error.value = Event(result.e)
                            }
                        }
                    }
                } catch (e: Exception){
                    error.value = Event(e)
                }
            }

        }
    }

    fun loginWithGoogle(token: String){
        if(token=="") error.value = Event(UnknownException())
        else{
            viewModelScope.launch {
                try {
                    withTimeout(Generic.TIMEOUT_VALUE){
                        when(val result = authRepository.signInWithGoogle(token)){
                            is TaskResult.Success -> {
                                val user = result.data
                                if(user.newUser) {
                                    successMessage.value = Event(SuccessMessage.SIGN_UP_USER)
                                    loginAction.value = Event(LoginActions.GO_TO_MAIN)
                                } else {
                                    successMessage.value = Event(SuccessMessage.LOGIN_USER)
                                    loginAction.value = Event(LoginActions.GO_TO_MAIN)
                                }

                            }
                            is TaskResult.Error -> {
                                error.value = Event(result.e)
                            }
                        }
                    }
                } catch (e: Exception){
                    error.value = Event(e)
                }
            }

        }
    }

}