package com.endcodev.saber_y_beber.presenter.forgot_pass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.endcodev.saber_y_beber.data.network.FirebaseClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotViewModel @Inject constructor(val firebase: FirebaseClient) : ViewModel() {

    private val _sent = MutableLiveData<Boolean>()
    val sent: LiveData<Boolean> get() = _sent

    fun forgotPass(email: String) {
        try {
            firebase.auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    _sent.value = true
                }.addOnFailureListener {
                    _sent.value = false
                }
        } catch (e: Exception) {
            _sent.value = false
        }
    }
}