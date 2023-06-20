package com.endcodev.saber_y_beber.presenter.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.model.ErrorModel
import com.endcodev.saber_y_beber.databinding.ActivityMainBinding
import com.endcodev.saber_y_beber.presenter.dialogs.ErrorDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity **"
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //FacebookSdk.sdkInitialize(applicationContext);
        //AppEventsLogger.activateApp(this);

        mainViewModel.isConnected()

        initObservers()

        appReady()
    }

    /**
     * Initializes observers for LiveData objects in the ViewModel.
     */
    private fun initObservers() {
        mainViewModel.version.observe(this) {
            if (it != null)
                versionControl(it)
            else
                Log.e(TAG, "App version is null")
        }
    }

    /**
     * Sets up an OnPreDrawListener to check whether the initial data is ready before drawing the UI.
     */
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
                    } else {
                        // The content isn't ready. Suspend.
                        false
                    }
                }
            }
        )
    }

    /**
     * Gets package info in a backwards-compatible way.
     */
    private fun PackageManager.getPackageInfoCompat(
        packageName: String,
        flags: Int = 0
    ): PackageInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
        }

    /**
     * Gets the current app version.
     * @return The current app version as an Int.
     */
    private fun getVersion(): Int {

        val appVersion: Int
        val pInfo = this.packageManager.getPackageInfoCompat(packageName, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appVersion = pInfo.longVersionCode.toInt()
        } else {
            @Suppress("DEPRECATION")
            appVersion = pInfo.versionCode
        }
        return appVersion
    }


    /**
     * Checks whether the current app version matches the required version and shows an error dialog if it doesn't.
     * @param needVersion The required version as a String.
     */
    private fun versionControl(needVersion: String) {

        val error =
            ErrorModel(
                getString(R.string.require_update),
                getString(R.string.require_version, getVersion().toString(), needVersion.toInt()),
                getString(R.string.update),
                getString(R.string.cancel)
            )

        if (needVersion.toInt() != getVersion()) {
            val dialog = ErrorDialogFragment(onAcceptClickLister = {
                if (it) {
                    openPlayStore()
                } else {
                    finish()
                    exitProcess(0)
                }
            }, error)
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, "dialog")
        }
    }

    /**
     * Opens the Play Store to update the app.
     */
    private fun openPlayStore() {
        val appPackageName = packageName
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.isConnected()
    }

}