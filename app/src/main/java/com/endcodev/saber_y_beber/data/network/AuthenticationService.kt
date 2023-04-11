package com.endcodev.saber_y_beber.data.network

import android.util.Log
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.ResourcesProvider
import com.endcodev.saber_y_beber.data.model.DialogMessage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthenticationService @Inject constructor(
    private val firebase: FirebaseClient,
    private val resources: ResourcesProvider
) {

    companion object {
        const val TAG = "Register Log:"
        const val NO_ERROR = 0
        const val MAIL_NO_VERIFICATION = 100
        const val ERROR_MAIL_OR_PASS = 101
    }

    fun createUser(email: String, pass: String, userName: String): DialogMessage {
        val auth = firebase.auth
        lateinit var error: DialogMessage

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                putUserName(userName)
                error = sendMailVerification()
                Log.v(TAG, "OK: createUserWithEmail:success ${auth.currentUser?.toString()}")
            } else if (it.isCanceled) {
                Log.e(TAG, "Error: createUserWithEmail:canceled -->${it.exception}")
                error = DialogMessage("Error", "Error occurred creating account")
            } else {
                Log.e(TAG, "Error: createUserWithEmail:failure")
                error = DialogMessage("Error", "Error occurred creating account")
            }
        }
        return error
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

    private fun sendMailVerification(): DialogMessage {
        lateinit var error: DialogMessage

        Firebase.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            error = if (it.isSuccessful) {
                Log.v(TAG, "OK: sendEmailVerification:Success")
                DialogMessage(
                    resources.getString(R.string.register_success),
                    resources.getString(R.string.register_check_mail)
                )
            } else {
                Log.e(TAG, "Error: sendEmailVerification:fail")
                DialogMessage("Server Error", "Unable to send verification email")
            }
        }
        return error
    }

    fun mailPassLogin(loginMail: String, loginPass: String): Int {
        var error = -1

        Firebase.auth.signInWithEmailAndPassword(loginMail, loginPass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (Firebase.auth.currentUser?.isEmailVerified!!)// if mail not verified
                        error = MAIL_NO_VERIFICATION
                    if (Firebase.auth.currentUser?.isEmailVerified!!)// if mail verified
                        error = NO_ERROR
                } else
                    error = ERROR_MAIL_OR_PASS //if pass or user not correct
            }
        return error
    }
}