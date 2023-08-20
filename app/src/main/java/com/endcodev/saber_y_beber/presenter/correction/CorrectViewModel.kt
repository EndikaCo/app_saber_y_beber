package com.endcodev.saber_y_beber.presenter.correction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endcodev.saber_y_beber.data.model.CorrectionModel
import com.endcodev.saber_y_beber.data.model.CorrectorModel
import com.endcodev.saber_y_beber.data.network.FirebaseClient
import com.endcodev.saber_y_beber.domain.GetCorrectionsUseCase
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CorrectViewModel @Inject constructor(
    private val getCorrectionsUseCase: GetCorrectionsUseCase,
    private val firebase: FirebaseClient,

    ) : ViewModel() {

    companion object {
        const val TAG = "getCorrectionsUseCase **"
    }

    private var allCorrectionList: List<CorrectionModel>? = null
    private var position: Int = 0

    private val _correctModel = MutableLiveData<CorrectionModel?>()
    val correctModel: LiveData<CorrectionModel?> get() = _correctModel

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _toCreate = MutableLiveData<Boolean>()
    val toCreate: LiveData<Boolean> get() = _toCreate

    fun onCreate() {
        getAllCorrections()
    }

    private fun getAllCorrections() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            allCorrectionList = getCorrectionsUseCase()
            if (allCorrectionList == null) {
                _correctModel.postValue(null)
            } else
                postAvailableCorrection()
            _isLoading.postValue(false)
        }
    }

    /**
     * send to UI the correction
     */
    fun postAvailableCorrection() {
        for ((index, item) in allCorrectionList?.withIndex()!!) {
            if (!checkAnyCorrection(item)) { // check for same UID
                position = index
                _correctModel.postValue(item)
                return
            }
        }
        _correctModel.postValue(null)
    }


    /**
     * @param item is the quest to check its availability
     * @return FALSE if available to correct or TRUE if the quest has already been created or corrected by the use.
     */
    private fun checkAnyCorrection(item: CorrectionModel): Boolean {

        //if (firebase.auth.currentUser == null) // todo sobra
        //    return false

        for ((index, value) in item.correctors.withIndex()) {
            if (value.id == firebase.auth.currentUser!!.uid) {
                Log.v(TAG, "correction $index already corrected or created")
                return true
            }
        }
        return false
    }

    /**
     * @return the UID of the user.
     */
    private fun getUid(): String {
        val uid = firebase.auth.uid
        uid?.let { return it}
        return "00"
    }


    /**
     * @param state is the state of the correction.
     */
    fun acceptCorrection(state: Boolean) {

        val correctionModel = CorrectorModel(getUid(), state)

        val validCorrection = allCorrectionList?.get(position)
        if (validCorrection == null) {
            _correctModel.postValue(null)
            return
        }

        // add the correction to the list
        validCorrection.correctors.add(correctionModel)

        // get database reference
        val database: DatabaseReference =
            firebase.dataBase.reference.child("corrections").child("$position")

        // update the database
        database.child("correctors").setValue(validCorrection.correctors)

        // flag to navigate to create fragment
        _toCreate.value = true
    }
}