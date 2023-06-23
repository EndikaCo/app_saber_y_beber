package com.endcodev.saber_y_beber.presenter.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.ResourcesProvider
import com.endcodev.saber_y_beber.data.network.AuthenticationService
import com.endcodev.saber_y_beber.data.network.AuthenticationService.Companion.MAIL_NO_VERIFICATION
import com.endcodev.saber_y_beber.data.network.AuthenticationService.Companion.NO_ERROR
import com.endcodev.saber_y_beber.presenter.login.LoginFragment.Companion.TAG

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val resources: ResourcesProvider,
    private val authenticationService: AuthenticationService

) : ViewModel() {

    private var _toast = MutableLiveData<String>()
    val toast get() = _toast

    private var _inConnected = MutableLiveData<Boolean>()
    val isConnected get() = _inConnected

    fun login(loginMail: String, loginPass: String) {

        if (loginMail.isNotEmpty() && loginPass.isNotEmpty()) {

            authenticationService.mailPassLogin(loginMail, loginPass) { error ->
                when (error) {
                    NO_ERROR -> {
                        // Login success
                        Log.v(TAG, "Login Success")
                        _toast.value = "Login Success"//todo strings
                        _inConnected.value = true
                    }

                    MAIL_NO_VERIFICATION -> {
                        // Email not verified
                        _toast.value =
                            resources.getString(R.string.login_not_verified)
                        Log.v(TAG, "MAIL_NO_VERIFICATION")
                    }

                    else -> {
                        // Login failed or other error
                        _toast.value = resources.getString(R.string.login_failed)
                        Log.v(TAG, "ERROR_MAIL_OR_PASS")
                    }
                }
            }
        } else {
            _toast.value = "empty mail or pass" //todo set error, y strings
            Log.v(TAG, "empty mail or pass")
        }
    }
}