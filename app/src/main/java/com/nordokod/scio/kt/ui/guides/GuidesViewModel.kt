package com.nordokod.scio.kt.ui.guides

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nordokod.scio.kt.constants.enums.SuccessMessage
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.repository.IAuthRepository
import com.nordokod.scio.kt.model.repository.IGuideRepository
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.launch
import kotlin.Exception

class GuidesViewModel (
        private val guideRepository: IGuideRepository,
        private val authRepository: IAuthRepository,
    ) : ViewModel() {
    val error = MutableLiveData<Exception>()
    //val successMessage = MutableLiveData<SuccessMessage>()
    val guides = MutableLiveData<List<Guide>>()

    fun getGuides() {
        viewModelScope.launch {
            try {
                val userResult = authRepository.getBasicUserInfo()
                if (userResult is TaskResult.Success){
                    when (val guidesResult = guideRepository.getUserGuides(userResult.data.uid)){
                        is TaskResult.Error -> error.value = guidesResult.e
                        is TaskResult.Success -> guides.value = guidesResult.data
                    }
                }
            } catch ( e : Exception) {
                error.value = e
            }
        }
    }

}