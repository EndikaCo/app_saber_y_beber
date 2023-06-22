package com.endcodev.saber_y_beber.presenter.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.ResourcesProvider
import com.endcodev.saber_y_beber.data.network.AuthenticationService
import com.endcodev.saber_y_beber.data.network.AuthenticationService.Companion.ERROR_MAIL_OR_PASS
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
            val error = authenticationService.mailPassLogin(loginMail, loginPass)

            Log.v(TAG, authenticationService.isUserConnected().toString())
            if (authenticationService.isUserConnected()) {//todo
                Log.v(TAG, "user connected = true")
                isConnected.value = true
            }

            when (error) {
                NO_ERROR -> {
                    Log.v(TAG, "NO_ERROR").toString()
                    _toast.value = "no error"
                }

                MAIL_NO_VERIFICATION -> {
                    _toast.value =
                        resources.getString(R.string.login_not_verified)
                    Log.v(TAG, "MAIL_NO_VERIFICATION").toString()
                }

                ERROR_MAIL_OR_PASS -> {
                    _toast.value = resources.getString(R.string.login_failed)
                    Log.v(TAG, "ERROR_MAIL_OR_PASS").toString()
                }

                else -> {

                }

            }
        } else
            _toast.value = resources.getString(R.string.login_error)
    }


}