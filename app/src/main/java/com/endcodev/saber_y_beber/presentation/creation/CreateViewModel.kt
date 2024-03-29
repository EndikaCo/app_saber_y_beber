package com.endcodev.saber_y_beber.presentation.creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endcodev.saber_y_beber.data.network.FirebaseClient
import com.endcodev.saber_y_beber.domain.model.CorrectionModel
import com.endcodev.saber_y_beber.domain.model.CorrectorModel
import com.endcodev.saber_y_beber.domain.model.EditTextErrorModel
import com.endcodev.saber_y_beber.domain.usecases.GetCorrectionsUseCase
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val getCorrectionsUseCase: GetCorrectionsUseCase,
    private val firebase: FirebaseClient,
) : ViewModel() {

    companion object {
        const val QUEST_EMPTY = 101
        const val A_EMPTY = 103
        const val B_EMPTY = 104
        const val C_EMPTY = 105
        const val DIF_EMPTY = 106
        const val SOME_WRONG = 107
        const val OK = 0
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _notification = MutableLiveData<Int>()
    val notification: LiveData<Int> get() = _notification

    private val _difficulty = MutableLiveData(1)
    val difficulty: LiveData<Int> get() = _difficulty

    private var _questError = MutableLiveData<EditTextErrorModel>()
    val questError: LiveData<EditTextErrorModel> get() = _questError

    fun onCreate() {
        EditTextErrorModel(0, 0, 0, 0, 0)
    }

    private fun postCorrection(correctionModel: CorrectionModel) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            //get reference key of last item
            val a = firebase.dataBase.reference.child("corrections/").child("${getCorrectionsUseCase()?.size}")
            val path = "corrections/${a.key}"

            val database = FirebaseDatabase.getInstance().reference.child(path)
            withContext(Dispatchers.IO) {
                try {
                    database.setValue(correctionModel).await()
                    _notification.postValue(OK)
                } catch (e: Exception) {
                    _notification.postValue(SOME_WRONG)
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }

    fun checkValues(quest: String, optionA: String, optionB: String, optionC: String) {
        _questError.value = EditTextErrorModel(0, 0, 0, 0, 0)
        val questOk: Boolean = isValidQuest(quest)
        val aOptionOK: Boolean = isValidCorrectOption(optionA)
        val bOptionOK: Boolean = isValidOptionB(optionB)
        val cOptionOK: Boolean = isValidOptionC(optionC)
        val difficultyOk: Boolean = isValidDifficulty(_difficulty.value!!)

        if (questOk && aOptionOK && bOptionOK && cOptionOK && difficultyOk)
            postCorrection(
                CorrectionModel(
                    correction = quest,
                    author = getUserName(),
                    option1 = optionA,
                    option2 = optionB,
                    option3 = optionC,
                    difficulty = _difficulty.value!!,
                    correctors = correctorsList()
                )
            ) else _questError.postValue(_questError.value)
    }

    private fun correctorsList(): ArrayList<CorrectorModel> {
        val correctorsList: ArrayList<CorrectorModel> = ArrayList()
        correctorsList.add(CorrectorModel(getUid(), true))
        return correctorsList
    }

    private fun getUid(): String {
        val uid = firebase.auth.uid
        uid?.let { return it }
        return "00"
    }

    private fun getUserName(): String {

        val user = firebase.auth.currentUser
        user?.let { return it.displayName.toString() }
        return "Anonymous"
    }

    private fun isValidQuest(quest: String): Boolean {
        return if (quest == "") {
            _questError.value?.questError = QUEST_EMPTY
            false
        } else {
            _questError.value?.questError = 0
            true
        }
    }

    private fun isValidCorrectOption(option: String): Boolean {
        return if (option == "") {
            _questError.value?.correctError = A_EMPTY
            false
        } else {
            _questError.value?.correctError = 0
            true
        }
    }

    private fun isValidOptionB(optionB: String): Boolean {
        return if (optionB == "") {
            _questError.value?.option2Error = B_EMPTY
            false
        } else {
            _questError.value?.option2Error = 0
            true
        }
    }

    private fun isValidOptionC(optionC: String): Boolean {
        return if (optionC == "") {
            _questError.value?.option3Error = C_EMPTY
            false
        } else {
            _questError.value?.option3Error = 0
            true
        }
    }

    private fun isValidDifficulty(difficulty: Int): Boolean {
        return if (difficulty == 0) {
            questError.value?.difficultyError = DIF_EMPTY
            false
        } else {
            questError.value?.difficultyError = 0
            true
        }
    }

    fun difficultyChange() {
        _difficulty.value = _difficulty.value!! + 1
        if (_difficulty.value == 4)
            _difficulty.value = 1
    }
}

