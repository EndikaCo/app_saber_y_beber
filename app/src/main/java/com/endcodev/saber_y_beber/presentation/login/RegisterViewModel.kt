package com.endcodev.saber_y_beber.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.network.AuthenticationService
import com.endcodev.saber_y_beber.presentation.utils.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val resources: ResourcesProvider,
    private val authenticationService: AuthenticationService
) : ViewModel() {

    companion object {
        const val PASS_SHORT = 101
        const val PASS_DIGIT = 102
        const val PASS_CAP = 103
        const val PASS_MINUS = 104
        const val PASS_SPECIAL = 105
    }

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _user = MutableLiveData<String>()
    val user: LiveData<String> get() = _user

    private val _pass = MutableLiveData<Int>()
    val pass: LiveData<Int> get() = _pass

    private val _repeat = MutableLiveData<String>()
    val repeat: LiveData<String> get() = _repeat

    private val _dialog = MutableLiveData<Int>()
    val dialog: LiveData<Int> get() = _dialog

    fun createAccount(email: String, pass: String, repeat: String, userName: String) {

        val emailOk: Boolean = isValidMail(email)
        val passOk: Boolean = isValidPass(pass)
        val repeatOk: Boolean = isEqualPass(pass, repeat)
        val userOk: Boolean = isValidUser(userName)
        if (emailOk && passOk && repeatOk && userOk)
            authenticationService.createUser(email, pass, userName) {
                _dialog.value = it
            }
    }

    private fun isValidPass(pass: String): Boolean {
        if (pass.length < 6) {
            _pass.value = PASS_SHORT
            return false
        }
        if (pass.firstOrNull { it.isDigit() } == null) {
            _pass.value = PASS_DIGIT
            return false
        }
        if (pass.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) {
            _pass.value = PASS_CAP
            return false
        }
        if (pass.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } == null) {
            _pass.value = PASS_MINUS
            return false
        }
        if (pass.firstOrNull { !it.isLetterOrDigit() } == null) {
            _pass.value = PASS_SPECIAL
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
            _email.value = resources.getString(R.string.register_error_mail_valid)
            return false
        }
        if (!email.contains('@') || !email.contains('.')) {
            _email.value = resources.getString(R.string.register_error_mail_valid)
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