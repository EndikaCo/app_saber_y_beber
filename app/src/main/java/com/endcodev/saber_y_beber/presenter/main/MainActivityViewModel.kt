package com.endcodev.saber_y_beber.presenter.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endcodev.saber_y_beber.data.network.FirebaseClient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val firebase : FirebaseClient
) : ViewModel() {

    var isReady: Boolean = false
    private val _version = MutableLiveData<String?>()
    val version: LiveData<String?> get() = _version

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Thread.sleep(500)
            }
            isReady = true
        }
    }

    /**
     * Checks if the app is connected to Firebase
     */
    fun isConnected() {
        val connectedRef = firebase.db.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    Log.d(MainActivity.TAG, "firebase connected")
                    checkVersion()
                } else {
                    Log.d(MainActivity.TAG, "firebase not connected")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(MainActivity.TAG, "firebase listener was cancelled")
            }
        })
    }

    /**
     * Checks the version of the app in Firebase
     */
    private fun checkVersion() {

        val myRef = firebase.data.getReference("/version")
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val needVersion = snapshot.getValue<String>()

                if (needVersion == null) {
                    Log.e(MainActivity.TAG, "need versions is null")
                } else {
                    _version.value = needVersion
                    Log.v(MainActivity.TAG, "versions updated")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(MainActivity.TAG, "Firebase error: $error")
            }
        })
    }
}