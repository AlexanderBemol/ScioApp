package com.nordokod.scio.kt.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.model.repository.IAuthRepository
import com.nordokod.scio.kt.utils.TaskResult
import com.nordokod.scio.kt.ui.splash.SplashActions;
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.lang.Exception

class SplashViewModel(private val authRepository: IAuthRepository) : ViewModel() {
    val splashAction = MutableLiveData<SplashActions>()

    fun loadInitialConfiguration(){
        viewModelScope.launch{
            try{
                withTimeout(Generic.TIMEOUT_VALUE){
                    when(val result = authRepository.isUserLogged()){
                        is TaskResult.Success -> {
                            splashAction.value =
                                    if(result.data) SplashActions.GO_TO_MAIN
                                    else SplashActions.GO_TO_LOGIN
                        }
                        is TaskResult.Error -> {
                            splashAction.value = SplashActions.GO_TO_LOGIN
                        }
                    }
                }
            } catch (e : Exception){
                splashAction.value = SplashActions.GO_TO_LOGIN
            }
        }

    }

}