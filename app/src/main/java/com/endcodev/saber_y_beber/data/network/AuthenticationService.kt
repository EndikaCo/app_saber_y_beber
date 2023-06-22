package com.endcodev.saber_y_beber.data.network

import android.util.Log
import androidx.annotation.NonNull
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.ResourcesProvider
import com.endcodev.saber_y_beber.data.model.DialogModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.SignInMethodQueryResult
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
        const val TAG = "AuthenticationService **"
        const val NO_ERROR = 0
        const val MAIL_NO_VERIFICATION = 100
        const val ERROR_MAIL_OR_PASS = 101
    }

    fun createUser(email: String, pass: String, userName: String): DialogModel {
        val auth = firebase.auth
        var error = DialogModel("null", "null")

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                putUserName(userName)
                error = sendMailVerification()
                Log.v(TAG, "OK: createUserWithEmail:success ${auth.currentUser?.toString()}")
            } else if (it.isCanceled) {
                Log.e(TAG, "Error: createUserWithEmail:canceled -->${it.exception}")
                error = DialogModel("Error", "Error occurred creating account")
            } else {
                Log.e(TAG, "Error: createUserWithEmail:failure")
                error = DialogModel("Error", "Error occurred creating account")
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

    private fun sendMailVerification(): DialogModel {

        var error = DialogModel("null", "null")

        Firebase.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            error = if (it.isSuccessful) {
                Log.v(TAG, "OK: sendEmailVerification:Success")
                DialogModel(
                    resources.getString(R.string.register_success),
                    resources.getString(R.string.register_check_mail)
                )
            } else {
                Log.e(TAG, "Error: sendEmailVerification:fail")
                DialogModel("Server Error", "Unable to send verification email")
            }
        }
        return error
    }

    fun mailPassLogin(loginMail: String, loginPass: String): Int {
        var error = -1

        Firebase.auth.signInWithEmailAndPassword(loginMail, loginPass)
            .addOnCompleteListener {
                error = if (it.isSuccessful) {
                    if (!Firebase.auth.currentUser?.isEmailVerified!!) {
                        MAIL_NO_VERIFICATION
                    } else {
                        NO_ERROR
                    }
                } else {
                    ERROR_MAIL_OR_PASS
                }
            }
        return error
    }

    fun isUserConnected(): Boolean {
        val currentUser = firebase.auth.currentUser
        return currentUser != null
    }
}