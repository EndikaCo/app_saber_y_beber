package com.endcodev.saber_y_beber.data.network

import android.util.Log
import com.endcodev.saber_y_beber.domain.utils.App
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationService @Inject constructor(
    private val firebase: FirebaseClient,
) {
    companion object {
        const val NO_ERROR = 0
        const val MAIL_NO_VERIFICATION = 100
        const val ERROR_MAIL_OR_PASS = 101
        const val ERROR_CREATING_ACC = 102
        const val MAIL_SENT_SUCCESS = 103
        const val MAIL_SENT_ERROR = 104
    }

    fun createUser(email: String, pass: String, userName: String, completionHandler: (Int) -> Unit){
        val auth = firebase.auth

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { it ->
            if (it.isSuccessful) {
                putUserName(userName)
                 sendMailVerification{ mError->
                     completionHandler(mError)
                }
                Log.v(App.tag, "OK: createUserWithEmail:success ${auth.currentUser?.toString()}")
            } else {
                Log.e(App.tag, "Error: createUserWithEmail:failure")
                completionHandler(ERROR_CREATING_ACC)
            }
        }
    }

    private fun putUserName(name: String) {

        val profileUpdates = userProfileChangeRequest { displayName = name }
        Firebase.auth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful)
                Log.v(App.tag, "OK: User profile updated correctly.")
            else
                Log.e(App.tag, "Error: User profile update error")
        }
    }

    private fun sendMailVerification(completionHandler: (Int) -> Unit){

        Firebase.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.v(App.tag, "OK: sendEmailVerification:Success")
                completionHandler(MAIL_SENT_SUCCESS)
            } else {
                Log.e(App.tag, "Error: sendEmailVerification:fail")
                completionHandler(MAIL_SENT_ERROR)
            }
        }
    }

    fun mailPassLogin(loginMail: String, loginPass: String, completionHandler: (Int) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(loginMail, loginPass)
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
}