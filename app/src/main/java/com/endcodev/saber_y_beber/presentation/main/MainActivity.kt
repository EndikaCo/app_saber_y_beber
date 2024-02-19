package com.endcodev.saber_y_beber.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.ActivityMainBinding
import com.endcodev.saber_y_beber.domain.model.ErrorModel
import com.endcodev.saber_y_beber.domain.utils.App
import com.endcodev.saber_y_beber.presentation.dialogs.ErrorDialogFragment
import com.endcodev.saber_y_beber.presentation.utils.StoreUtils.getVersion
import com.endcodev.saber_y_beber.presentation.utils.StoreUtils.openPlayStore
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel.isConnected()
        initObservers()
        appReady()
    }

    private fun initObservers() {
        mainViewModel.version.observe(this) {
            if (it != null)
                versionControl(it)
            else
                Log.e(App.tag, "App version is null")
        }
    }

    /** Sets up an OnPreDrawListener to check whether the initial data is ready before drawing the UI.*/
    private fun appReady() {
        // Set up an OnPreDrawListener to the root view.
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check whether the initial data is ready.
                    return if (mainViewModel.isReady) {
                        // The content is ready. Start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else
                        false
                }
            }
        )
    }

    /**
     * Checks whether the current app version matches the required version and shows an error dialog if it doesn't.
     * @param needVersion The required version as a String.
     */
    private fun versionControl(needVersion: String) {
        val appPackageName = packageName

        val error =
            ErrorModel(
                getString(R.string.require_update),
                getString(
                    R.string.require_version,
                    getVersion(this).toString(),
                    needVersion.toInt()
                ),
                getString(R.string.update),
                getString(R.string.cancel)
            )

        if (needVersion.toInt() > getVersion(this)) {
            val dialog = ErrorDialogFragment(onAcceptClickLister = {
                if (it) {
                    openPlayStore(this, appPackageName)
                } else {
                    finish()
                    exitProcess(0)
                }
            }, error)
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, "dialog")
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.isConnected()
    }
}