package com.endcodev.saber_y_beber.data.network

import android.util.Log
import com.endcodev.saber_y_beber.domain.utils.App
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationService @Inject constructor(
    private val client: FirebaseClient,
) {
    companion object {
        const val NO_ERROR = 0
        const val MAIL_NO_VERIFICATION = 100
        const val ERROR_MAIL_OR_PASS = 101
        const val ERROR_CREATING_ACC = 102
        const val MAIL_SENT_SUCCESS = 103
        const val MAIL_SENT_ERROR = 104
    }

    fun createUser(
        email: String,
        pass: String,
        userName: String,
        completionHandler: (Int) -> Unit
    ) {
        client.auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                putUserName(userName)
                sendMailVerification { mError ->
                    completionHandler(mError)
                }
            } else {
                Log.e(App.tag, "Error: createUserWithEmail:failure")
                completionHandler(ERROR_CREATING_ACC)
            }
        }
    }

    fun putUserName(name: String) {

        client.auth.currentUser?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.v(App.tag, "OK: User profile updated correctly.")
                } else {
                    Log.e(App.tag, "Error: User profile update error")
                }
            }
    }

    private fun sendMailVerification(completionHandler: (Int) -> Unit) {

        client.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                completionHandler(MAIL_SENT_SUCCESS)
            } else {
                Log.e(App.tag, "Error: sendEmailVerification:fail")
                completionHandler(MAIL_SENT_ERROR)
            }
        }
    }

    fun mailPassLogin(loginMail: String, loginPass: String, completionHandler: (Int) -> Unit) {
        client.auth.signInWithEmailAndPassword(loginMail, loginPass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!Firebase.auth.currentUser?.isEmailVerified!!) {
                        completionHandler(MAIL_NO_VERIFICATION) //todo send another
                        //sendMailVerification{}
                        Log.v(App.tag, "Login success but mail no verification")
                    } else {
                        completionHandler(NO_ERROR)
                        Log.v(App.tag, "Login success")
                    }
                } else if (task.isCanceled || task.isComplete) {
                    completionHandler(ERROR_MAIL_OR_PASS)
                    Log.v(App.tag, "Login failed")
                }
            }
    }

    fun deleteAccount(onComplete: (Boolean) -> Unit) {
        client.auth.currentUser?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true)
                } else {
                    onComplete(false)
                }
            }
    }
}