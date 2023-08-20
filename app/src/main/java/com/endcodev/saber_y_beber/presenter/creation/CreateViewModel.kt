package com.endcodev.saber_y_beber.presenter.creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endcodev.saber_y_beber.data.model.CorrectionModel
import com.endcodev.saber_y_beber.data.model.CorrectorModel
import com.endcodev.saber_y_beber.data.model.EditTextError
import com.endcodev.saber_y_beber.data.model.EditTextErrorModel
import com.endcodev.saber_y_beber.data.network.FirebaseClient
import com.endcodev.saber_y_beber.domain.GetCorrectionsUseCase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val getCorrectionsUseCase: GetCorrectionsUseCase,
    private val firebase: FirebaseClient,
) : ViewModel() {

    private val _notification = MutableLiveData<String>()
    val notification: LiveData<String> get() = _notification

    private val _difficulty = MutableLiveData(0)
    val difficulty: LiveData<Int> get() = _difficulty

    private var _questError = MutableLiveData<EditTextErrorModel>()
    val questError: LiveData<EditTextErrorModel>get() = _questError

    fun onCreate() {
        EditTextErrorModel(null, null, null, null, null, null, null)
    }

    private fun postCorrection(correctionModel: CorrectionModel) {
        viewModelScope.launch {
            lateinit var database: DatabaseReference
            val path = "corrections/${getCorrectionsUseCase()?.size.toString()}"
            database = FirebaseDatabase.getInstance().reference.child(path)
            database.setValue(correctionModel).addOnSuccessListener {
                _notification.postValue("Subido correcto puta") // todo enviar val cono in o sealed class y coger string en fragment
            }.addOnFailureListener {
                _notification.postValue("Algo ha ido mal puta")
            }
        }
    }

    fun checkValues(
        quest: String,
        optionA: String,
        optionB: String,
        optionC: String
    ) {
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
                    difficulty = _difficulty.value!!, //todo
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
            _questError.value?.questError = EditTextError("cannot be empty", 1)
            false
        } else if (!quest.contains('?')) {
            _questError.value?.questError = EditTextError("do you forget '?' at the end", 2)
            false
        } else {
            _questError.value?.questError = null
            true
        }
    }

    private fun isValidCorrectOption(option: String): Boolean {
        return if (option == "") {
            _questError.value?.correctError = EditTextError("cannot be empty", 1)
            false
        } else {
            _questError.value?.correctError = null
            true
        }
    }

    private fun isValidOptionB(optionB: String): Boolean {
        return if (optionB == "") {
            _questError.value?.option2Error = EditTextError("cannot be empty", 1)
            false
        } else {
            _questError.value?.option2Error = null
            true
        }
    }

    private fun isValidOptionC(optionC: String): Boolean {
        return if (optionC == "") {
            _questError.value?.option3Error = EditTextError("cannot be empty", 1)
            false
        } else {
            _questError.value?.option3Error = null
            true
        }
    }

    private fun isValidAlternative(alternative: String): Boolean {
        return if (alternative == "") {
            questError.value?.alternativeError = null
            true
        } else if (alternative.startsWith("o ")) {
            questError.value?.alternativeError = null
            true
        } else {
            questError.value?.alternativeError = EditTextError("Should start with \"o\"", 2)
            false
        }
    }

    private fun isValidDifficulty(difficulty: Int): Boolean {
        return if (difficulty == 0) {
            questError.value?.difficultyError = EditTextError("difficulty is empty", 1)
            false
        } else {
            questError.value?.difficultyError = EditTextError("", 0)
            true
        }
    }

    fun difficultyChange() {
        _difficulty.value = _difficulty.value!! + 1
        if (_difficulty.value == 4)
            _difficulty.value = 1
    }
}

