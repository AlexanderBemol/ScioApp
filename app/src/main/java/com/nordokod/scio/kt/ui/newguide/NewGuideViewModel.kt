package com.nordokod.scio.kt.ui.newguide

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nordokod.scio.kt.constants.Generic
import com.nordokod.scio.kt.constants.InputDataException
import com.nordokod.scio.kt.constants.enums.GuideCategory
import com.nordokod.scio.kt.constants.enums.SuccessMessage
import com.nordokod.scio.kt.model.entity.Guide
import com.nordokod.scio.kt.model.repository.IAuthRepository
import com.nordokod.scio.kt.model.repository.IGuideRepository
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.lang.Exception
import java.util.*

class NewGuideViewModel(
        private val guideRepository: IGuideRepository,
        private val authRepository: IAuthRepository,
) : ViewModel() {
    val error = MutableLiveData<Exception>()
    val successMessage = MutableLiveData<SuccessMessage>()

    fun createGuide(category : GuideCategory?, topic : String, testDate : Date){
        if(category != null && topic.isNotEmpty()){
            if(testDate.after(Date())){
                viewModelScope.launch {
                    try {
                        val userResult = authRepository.getBasicUserInfo()
                        if(userResult is TaskResult.Success){
                            val guide = Guide(
                                    topic = topic,
                                    testDate = testDate,
                                    category = category.code,
                                    creationUser = userResult.data.uid,
                                    updateUser = userResult.data.uid
                            )
                            withTimeout(Generic.TIMEOUT_VALUE){
                                when(val result = guideRepository.createGuide(guide)){
                                    is TaskResult.Success -> successMessage.value = SuccessMessage.CREATE_GUIDE
                                    is TaskResult.Error -> error.value = result.e
                                }
                            }
                        }
                    } catch (e : Exception) {
                        error.value = e
                    }
                }
            } else {
                error.value = InputDataException(InputDataException.Code.DATETIME_BEFORE)
            }
        } else {
            error.value = InputDataException(InputDataException.Code.EMPTY_FIELD)
        }
    }

}