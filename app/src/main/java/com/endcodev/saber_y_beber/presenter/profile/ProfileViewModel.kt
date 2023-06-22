package com.endcodev.saber_y_beber.presenter.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.endcodev.saber_y_beber.data.network.FirebaseClient
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebase: FirebaseClient,
) : ViewModel() {

    private var _currentUser = MutableLiveData<FirebaseUser>()
    val currentUser: LiveData<FirebaseUser> get() = _currentUser


    fun user(){
        _currentUser.value = Firebase.auth.currentUser
    }

    fun disconnect() {
       firebase.auth.signOut()
    }
}