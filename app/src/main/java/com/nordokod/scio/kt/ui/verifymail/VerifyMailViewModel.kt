package com.nordokod.scio.kt.ui.verifymail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.enums.SuccessMessage
import com.nordokod.scio.kt.model.repository.IAuthRepository
import com.nordokod.scio.kt.utils.Event
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class VerifyMailViewModel(private val auth: IAuthRepository) : ViewModel() {
    val verifyMailAction = MutableLiveData<Event<VerifyMailActions>>()
    val successMessage = MutableLiveData<Event<SuccessMessage>>()
    val error = MutableLiveData<Event<Exception>>()

    fun logout(){
        try {
            viewModelScope.launch {
                withTimeout(Generic.TIMEOUT_VALUE){
                    when(val result = auth.logOut()){
                        is TaskResult.Success ->{
                            verifyMailAction.value = Event(VerifyMailActions.GO_TO_LOGIN)
                        }
                        is TaskResult.Error -> {
                            error.value = Event(result.e)
                        }
                    }
                }
            }
        } catch (e : Exception) {
            error.value = Event(e)
        }
    }
    fun sendVerificationMail(){
        try {
            viewModelScope.launch {
                withTimeout(Generic.TIMEOUT_VALUE){
                    when(val result = auth.sendVerificationMail()){
                        is TaskResult.Success ->{
                            successMessage.value = Event(SuccessMessage.RESEND_VERIFICATION_MAIL)
                        }
                        is TaskResult.Error -> {
                            error.value = Event(result.e)
                        }
                    }
                }
            }
        } catch (e : Exception) {
            error.value = Event(e)
        }
    }
    fun refreshUser(){
        try {
            viewModelScope.launch {
                withTimeout(Generic.TIMEOUT_VALUE){
                    when(val result = auth.refreshUser()){
                        is TaskResult.Success -> {
                            if(result.data.emailVerified) {
                                successMessage.value = Event(SuccessMessage.MAIL_VERIFIED)
                                verifyMailAction.value = Event(VerifyMailActions.GO_TO_MAIN)
                            } else {
                                verifyMailAction.value = Event(VerifyMailActions.STOP_REFRESH_ANIMATION)
                            }
                        }
                        is TaskResult.Error -> {
                            error.value = Event(result.e)
                        }
                    }
                }
            }
        } catch (e : Exception) {
            error.value = Event(e)
        }
    }
}