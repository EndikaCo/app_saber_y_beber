package com.endcodev.saber_y_beber.presenter.correction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endcodev.saber_y_beber.data.model.CorrectionModel
import com.endcodev.saber_y_beber.domain.GetCorrectionsUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CorrectViewModel @Inject constructor(
    private val getCorrectionsUseCase: GetCorrectionsUseCase
) : ViewModel() {

    companion object {
        const val TAG = "getCorrectionsUseCase **"
    }

    private val _correctModel = MutableLiveData<CorrectionModel?>()
    val correctModel: LiveData<CorrectionModel?> get() = _correctModel

    private val _correctionList = MutableLiveData<List<CorrectionModel>?>()
    val correctionList: LiveData<List<CorrectionModel>?> get() = _correctionList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var position: Int = 0

    fun onCreate() {
        //getAllCorrections() todo
    }

    private fun getAllCorrections() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            _correctionList.value = getCorrectionsUseCase()
            //availableCorrection()
            _isLoading.postValue(false)
        }
    }

    /**
     * @param item is the quest to check its availability
     * @return FALSE if available to correct - Or TRUE if the quest has already been created or corrected by the use.
     */
    private fun checkAnyCorrection(item: CorrectionModel): Boolean {
        for ((index, value) in item.correctors.withIndex()) {
            if (value.id == Firebase.auth.currentUser!!.uid) {
                Log.v(TAG, "correction $index already corrected or created")
                return true
            }
        }
        return false
    }
}



