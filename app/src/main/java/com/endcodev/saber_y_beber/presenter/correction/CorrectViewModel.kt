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
import com.google.firebase.database.FirebaseDatabase
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

    private var correctionsDone = 0

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
     * @return FALSE if available to correct - Or TRUE if the quest has already been created or corrected by the use.
     */
    private fun checkAnyCorrection(item: CorrectionModel): Boolean {

        if (firebase.auth.currentUser == null)
            return false
        for ((index, value) in item.correctors.withIndex()) {
            if (value.id == firebase.auth.currentUser!!.uid) {
                Log.v(TAG, "correction $index already corrected or created")
                return true
            }
        }
        return false
    }

    fun acceptCorrection(i: Boolean) {

        val uid = firebase.auth.currentUser!!.uid // todo not null

        val correctionModel = CorrectorModel(uid, true, 0)
        allCorrectionList?.get(position)?.correctors?.add(correctionModel)

        val validCorrection = allCorrectionList?.get(position)
        if (validCorrection == null) {
            _correctModel.postValue(null)
            return
        }

        validCorrection.correctors.add(correctionModel)
        val database: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("corrections").child("$position")

        if (i)
            database.child("rating").setValue(validCorrection.rating + 1)
        else
            database.child("rating").setValue(-1)

        database.child("correctors").setValue(validCorrection.correctors)
        correctionsDone += 1

        if (correctionsDone == 2)
            _toCreate.value = true
    }
}