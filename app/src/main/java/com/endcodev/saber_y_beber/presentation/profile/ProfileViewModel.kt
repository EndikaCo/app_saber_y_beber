package com.endcodev.saber_y_beber.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endcodev.saber_y_beber.data.network.AuthenticationService
import com.endcodev.saber_y_beber.data.network.FirebaseClient
import com.endcodev.saber_y_beber.domain.model.ProfileModel
import com.endcodev.saber_y_beber.domain.usecases.GetAllFromUidUseCase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebase: FirebaseClient,
    private val getAllFromUidUseCase: GetAllFromUidUseCase,
    private val authenticationService: AuthenticationService,
    ) : ViewModel() {

    private var _currentUser = MutableLiveData<FirebaseUser>()
    val currentUser: LiveData<FirebaseUser> get() = _currentUser

    private var _activityList = MutableLiveData<List<ProfileModel>>()
    val activityList: LiveData<List<ProfileModel>?> get() = _activityList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val getAllFromUidUseCase = getAllFromUidUseCase()
            _activityList.value = getAllFromUidUseCase
            _isLoading.postValue(false)
        }
    }

    fun user() {
        _currentUser.value = Firebase.auth.currentUser
    }

    fun disconnect() {
        firebase.auth.signOut()
    }

    fun changeUserName(name : String){
        authenticationService.putUserName(name)
    }

    fun deleteAccount( onComplete : (Boolean) -> Unit){
        authenticationService.deleteAccount(onComplete)
    }
}