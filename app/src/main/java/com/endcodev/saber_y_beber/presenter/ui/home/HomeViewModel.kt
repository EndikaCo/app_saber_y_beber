package com.endcodev.saber_y_beber.presenter.ui.home

import androidx.lifecycle.*
import com.endcodev.saber_y_beber.data.database.entities.toDb
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.domain.GetPlayersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playersUseCase: GetPlayersUseCase

) : ViewModel() {
    private val _playerList = MutableLiveData<MutableList<PlayersModel>>()
    val playerList: LiveData<MutableList<PlayersModel>> get() = _playerList

    init {
        viewModelScope.launch {
            _playerList.value = playersUseCase.invoke().toMutableList()
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
}