package com.nordokod.scio.kt.ui.main

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

class MainViewModel(private  val authRepository: IAuthRepository) : ViewModel() {
    val error = MutableLiveData<Event<Exception>>()
    val successMessage = MutableLiveData<Event<SuccessMessage>>()
    val mainAction = MutableLiveData<Event<MainActions>>()

    fun logOut(){
        viewModelScope.launch {
            try{
                withTimeout(Generic.TIMEOUT_VALUE){
                    when(val result = authRepository.logOut()){
                        is TaskResult.Success -> {
                            mainAction.value = Event(MainActions.GO_TO_LOGIN)
                        }
                        is TaskResult.Error -> {
                            error.value = Event(result.e)
                        }
                    }
                }
            } catch (e : Exception){
              error.value = Event(e)
            }
        }
    }
}