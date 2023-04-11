package com.endcodev.saber_y_beber.presenter.ui.home

import androidx.lifecycle.*
import com.endcodev.saber_y_beber.data.model.PlayersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {
    private val _playerList = MutableLiveData<MutableList<PlayersModel>>()
    val playerList: LiveData<MutableList<PlayersModel>> get() = _playerList

}