package com.endcodev.saber_y_beber.presenter.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endcodev.saber_y_beber.data.database.entities.toDb
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.data.network.FirebaseClient
import com.endcodev.saber_y_beber.domain.GetPlayersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playersUseCase: GetPlayersUseCase,
    private val firebase: FirebaseClient

) : ViewModel() {
    private val _playerList = MutableLiveData<MutableList<PlayersModel>>()
    val playerList: LiveData<MutableList<PlayersModel>> get() = _playerList

    private val _isConnected = MutableLiveData<Char>(null)
    val isConnected: LiveData<Char> get() = _isConnected

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

    /** updates isConnected observer with first char of userName to show in UI*/
    fun checkLogin() {
        val auth = firebase.auth.currentUser
        if (auth != null && auth.isEmailVerified)
            _isConnected.value = auth.displayName?.first()
    }

    /**
     * returns true if user is connected and false if not
     */
    fun isConnected(): Boolean {
        val auth = firebase.auth.currentUser
        return auth != null && auth.isEmailVerified
    }
}