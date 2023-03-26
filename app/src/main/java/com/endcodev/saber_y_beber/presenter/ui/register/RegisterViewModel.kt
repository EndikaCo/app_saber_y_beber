package com.endcodev.saber_y_beber.presenter.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.ResourcesProvider
import com.endcodev.saber_y_beber.data.model.DialogMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val resources: ResourcesProvider,
) : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _user = MutableLiveData<String>()
    val user: LiveData<String> get() = _user

    private val _pass = MutableLiveData<String>()
    val pass: LiveData<String> get() = _pass

    private val _repeat = MutableLiveData<String>()
    val repeat: LiveData<String> get() = _repeat

    private val _dialog = MutableLiveData<DialogMessage>()
    val dialog: LiveData<DialogMessage> get() = _dialog

    private var auth: FirebaseAuth = Firebase.auth

    private val TAG = "Register Log:"

    fun createAccount(email: String, pass: String, repeat: String, userName: String) {

        val emailOk: Boolean = isValidMail(email)
        val passOk: Boolean = isValidPass(pass)
        val repeatOk: Boolean = isEqualPass(pass, repeat)
        val userOk: Boolean = isValidUser(userName)
        if (emailOk && passOk && repeatOk && userOk)
            createUser(email, pass, userName)
    }

    private fun createUser(email: String, pass: String, userName: String) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { it ->
            if (it.isSuccessful) {
                putUserName(userName)
                sendMailVerification()
                Log.v(TAG, "OK: createUserWithEmail:success ${auth.currentUser?.toString()}")
            }
            if (it.isCanceled) {
                _dialog.value = DialogMessage("Error", "Error occurred creating account")
                Log.e(TAG, "Error: createUserWithEmail:canceled -->${it.exception}")
            }
            if (it.isComplete) {
                Log.v(TAG, "OK: createUserWithEmail:success ${auth.currentUser?.toString()}")
            } else {
                Log.e(TAG, "Error: createUserWithEmail:failure")
                _dialog.value = DialogMessage("Error", "Error occurred creating account")
            }
        }
    }

    private fun sendMailVerification() {
        Firebase.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.v(TAG, "OK: sendEmailVerification:Success")
                _dialog.value = DialogMessage(
                    resources.getString(R.string.register_success),
                    resources.getString(R.string.register_check_mail)
                )
            } else {
                Log.e(TAG, "Error: sendEmailVerification:fail")
                _dialog.value = DialogMessage("Server Error", "Unable to send verification email")
            }
        }
    }

    private fun putUserName(name: String) {
        val profileUpdates = userProfileChangeRequest { displayName = name }

        Firebase.auth.currentUser!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful)
                Log.v(TAG, "OK: User profile updated correctly.")
            else
                Log.e(TAG, "Error: User profile update error")
        }
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