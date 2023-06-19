package com.endcodev.saber_y_beber.presenter.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.ResourcesProvider
import com.endcodev.saber_y_beber.data.model.DialogModel
import com.endcodev.saber_y_beber.data.network.AuthenticationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val resources: ResourcesProvider,
    private val authenticationService: AuthenticationService
) : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _user = MutableLiveData<String>()
    val user: LiveData<String> get() = _user

    private val _pass = MutableLiveData<String>()
    val pass: LiveData<String> get() = _pass

    private val _repeat = MutableLiveData<String>()
    val repeat: LiveData<String> get() = _repeat

    private val _dialog = MutableLiveData<DialogModel>()
    val dialog: LiveData<DialogModel> get() = _dialog

    fun createAccount(email: String, pass: String, repeat: String, userName: String) {

        val emailOk: Boolean = isValidMail(email)
        val passOk: Boolean = isValidPass(pass)
        val repeatOk: Boolean = isEqualPass(pass, repeat)
        val userOk: Boolean = isValidUser(userName)
        if (emailOk && passOk && repeatOk && userOk)
            _dialog.value = authenticationService.createUser(email, pass, userName)
    }

    private fun isValidPass(pass: String): Boolean {
        if (pass.length < 6) {
            _pass.value = resources.getString(R.string.register_error_pass)
            return false
        }
        if (pass.firstOrNull { it.isDigit() } == null) {
            _pass.value = resources.getString(R.string.register_error_pass2)
            return false
        }
        if (pass.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) {
            _pass.value = resources.getString(R.string.register_error_pass_uppercase)
            return false
        }
        if (pass.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } == null) {
            _pass.value = resources.getString(R.string.register_error_pass_lowercase)
            return false
        }
        if (pass.firstOrNull { !it.isLetterOrDigit() } == null) {
            _pass.value = resources.getString(R.string.register_error_pass_special)
            return false
        }
        return true
    }

    private fun isEqualPass(pass: String, repeat: String): Boolean {
        if (pass.isNotEmpty() && pass == repeat)
            return true
        else
            _repeat.value = resources.getString(R.string.register_error_repeat_match)
        return false
    }

    private fun isValidMail(email: String): Boolean {
        if (email.isEmpty() || email.length < 5) {
            _email.value =  resources.getString(R.string.register_error_mail_valid)
            return false
        }
        if (!email.contains('@') || !email.contains('.')) {
            _email.value =  resources.getString(R.string.register_error_mail_valid)
            return false
        }
        return true
    }

    private fun isValidUser(userName: String): Boolean {
        if (userName.length < 3) {
            _user.value = resources.getString(R.string.register_error_user)
            return false
        }
        return true
    }
}