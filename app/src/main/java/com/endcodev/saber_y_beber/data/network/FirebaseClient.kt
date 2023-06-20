package com.endcodev.saber_y_beber.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseClient @Inject constructor() {

    companion object {
        const val TAG = "FirebaseClient **"
    }

    val auth: FirebaseAuth get() = FirebaseAuth.getInstance()
    val db = Firebase.database
    val data = FirebaseDatabase.getInstance()
}