package com.endcodev.saber_y_beber

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.endcodev.saber_y_beber.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //FacebookSdk.sdkInitialize(applicationContext);
        //AppEventsLogger.activateApp(this);
    }
}