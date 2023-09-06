package com.endcodev.saber_y_beber.presenter.login

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.presenter.utils.ResourcesProvider
import com.endcodev.saber_y_beber.data.network.AuthenticationService
import com.endcodev.saber_y_beber.data.network.AuthenticationService.Companion.MAIL_NO_VERIFICATION
import com.endcodev.saber_y_beber.data.network.AuthenticationService.Companion.NO_ERROR
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val resources: ResourcesProvider,
    private val authenticationService: AuthenticationService

) : ViewModel() {

    companion object { const val TAG = "LoginViewModel ***" }

    private var _toast = MutableLiveData<String>()
    val toast get() = _toast

    private var _isConnected = MutableLiveData<Boolean>()
    val isConnected get() = _isConnected

    fun login(loginMail: String, loginPass: String) {

        if (loginMail.isNotEmpty() && loginPass.isNotEmpty()) {
            authenticationService.mailPassLogin(loginMail, loginPass) { error ->
                when (error) {
                    NO_ERROR -> {
                        Log.v(TAG, "Login Success")
                        _toast.value = resources.getString(R.string.login_success)
                        _isConnected.value = true
                    }
                    MAIL_NO_VERIFICATION -> {
                        _toast.value = resources.getString(R.string.login_not_verified)
                        Log.v(TAG, "MAIL_NO_VERIFICATION")
                    }
                    else -> {
                        _toast.value = resources.getString(R.string.login_failed)
                        Log.v(TAG, "ERROR_MAIL_OR_PASS")
                    }
                }
            }
        } else {
            _toast.value = "empty mail or pass"
            Log.v(TAG, "empty mail or pass")
        }
    }
}