package com.endcodev.saber_y_beber.presenter.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.database.entities.toDb
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.data.network.FirebaseClient
import com.endcodev.saber_y_beber.domain.GetPlayersUseCase
import com.endcodev.saber_y_beber.presenter.home.HomeFragment.Companion.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playersUseCase: GetPlayersUseCase,
    private val firebase : FirebaseClient

) : ViewModel() {
    private val _playerList = MutableLiveData<MutableList<PlayersModel>>()
    val playerList: LiveData<MutableList<PlayersModel>> get() = _playerList

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> get() = _isConnected

    init {
        viewModelScope.launch {
            _playerList.value = playersUseCase.invoke().toMutableList()
            checkLogin()
        }
    }

    fun addNewPlayer(player: PlayersModel) {
        _playerList.value?.add(player)
        viewModelScope.launch(Dispatchers.IO) {
            playersUseCase.savePlayer(player.toDb())
        }
    }

    fun deletePlayer(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playersUseCase.deletePlayer(name)
        }
    }

    /** checks if user is logged*/
    private fun checkLogin() {
        _isConnected.value = firebase.auth.currentUser != null && firebase.auth.currentUser!!.isEmailVerified
    Log.v(TAG, "LOGIN checked ${_isConnected.value}")
    }
}