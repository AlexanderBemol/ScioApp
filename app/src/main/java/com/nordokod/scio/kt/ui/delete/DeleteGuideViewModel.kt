package com.nordokod.scio.kt.ui.delete

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nordokod.scio.kt.constants.enums.SuccessMessage
import com.nordokod.scio.kt.model.repository.IGuideRepository
import com.nordokod.scio.kt.utils.TaskResult
import kotlinx.coroutines.launch

class DeleteGuideViewModel(
        private val guideRepository: IGuideRepository,
) : ViewModel() {
    val error = MutableLiveData<Exception>()
    val successMessage = MutableLiveData<SuccessMessage>()

    fun deleteGuide(guideId : Int){
        viewModelScope.launch {
            try {
                val guideResult = guideRepository.getGuide(guideId)
                if (guideResult is TaskResult.Success){
                    when(val result = guideRepository.deleteGuide(guideResult.data)){
                        is TaskResult.Success -> successMessage.value = SuccessMessage.DELETE_GUIDE
                        is TaskResult.Error -> error.value = result.e
                    }
                } else error.value = (guideResult as TaskResult.Error).e
            } catch (e : Exception){
                error.value = e
            }
        }
    }

}