package com.nordokod.scio.kt.ui.login
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.InputDataException
import com.nordokod.scio.kt.constants.UnknownException
import com.nordokod.scio.kt.constants.enums.SuccessMessage
import com.nordokod.scio.kt.model.repository.IAuthRepository
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.lang.Exception

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


}