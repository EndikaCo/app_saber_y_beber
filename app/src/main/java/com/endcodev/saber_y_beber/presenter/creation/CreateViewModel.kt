package com.endcodev.saber_y_beber.presenter.creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
) : ViewModel() {

    //private val _isConnected = MutableLiveData<Boolean>()
    //val isConnected: LiveData<Boolean> get() = _isConnected

    init {
        viewModelScope.launch {

        }
    }
}

