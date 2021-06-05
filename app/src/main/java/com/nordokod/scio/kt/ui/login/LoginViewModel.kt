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
    val error = MutableLiveData<Exception>()
    val successMessage = MutableLiveData<SuccessMessage>()
    val loginAction = MutableLiveData<LoginActions>()

    fun signInWithMail(email: String, password: String){
        if(email.isBlank()||password.isBlank()){
            error.value = InputDataException(InputDataException.Code.EMPTY_FIELD)
        }else{
            viewModelScope.launch {
                try{
                    withTimeout(Generic.TIMEOUT_VALUE){
                        when(val result = authRepository.signInWithMail(email, password)){
                            is TaskResult.Error -> error.value = result.e
                            is TaskResult.Success -> {
                                val user = result.data
                                successMessage.value = SuccessMessage.LOGIN_USER
                                if(!user.emailVerified) loginAction.value = LoginActions.GO_TO_VERIFY_MAIL
                                else loginAction.value = LoginActions.GO_TO_MAIN
                            }
                        }
                    }
                }catch (e: TimeoutCancellationException){
                    error.value = UnknownException()
                }
            }
        }
    }

    fun loginWithFacebook(token: String){
        if(token=="") error.value = UnknownException()
        else{
            viewModelScope.launch {
                try {
                    withTimeout(Generic.TIMEOUT_VALUE){
                        when(val result = authRepository.signInWithFacebook(token)){
                            is TaskResult.Success -> {
                                val user = result.data
                                if(user.newUser) {
                                    successMessage.value = SuccessMessage.SIGN_UP_USER
                                    loginAction.value = LoginActions.GO_TO_MAIN
                                } else {
                                    successMessage.value = SuccessMessage.LOGIN_USER
                                    loginAction.value = LoginActions.GO_TO_MAIN
                                }

                            }
                            is TaskResult.Error -> {
                                error.value = result.e
                            }
                        }
                    }
                } catch (e: Exception){
                    error.value = e
                }
            }

        }
    }

    fun loginWithGoogle(token: String){
        if(token=="") error.value = UnknownException()
        else{
            viewModelScope.launch {
                try {
                    withTimeout(Generic.TIMEOUT_VALUE){
                        when(val result = authRepository.signInWithGoogle(token)){
                            is TaskResult.Success -> {
                                val user = result.data
                                if(user.newUser) {
                                    successMessage.value = SuccessMessage.SIGN_UP_USER
                                    loginAction.value = LoginActions.GO_TO_MAIN
                                } else {
                                    successMessage.value = SuccessMessage.LOGIN_USER
                                    loginAction.value = LoginActions.GO_TO_MAIN
                                }

                            }
                            is TaskResult.Error -> {
                                error.value = result.e
                            }
                        }
                    }
                } catch (e: Exception){
                    error.value = e
                }
            }

        }
    }

}