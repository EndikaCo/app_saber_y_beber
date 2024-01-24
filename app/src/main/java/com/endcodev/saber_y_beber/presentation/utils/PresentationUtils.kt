package com.endcodev.saber_y_beber.presentation.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

object StoreUtils {
    fun openPlayStore(context: Context, appPackageName: String) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    /**
     * Gets the current app version.
     * @return The current app version as an Int.
     */
    fun getVersion(context: Context): Int {
        val appVersion: Int
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appVersion = pInfo.longVersionCode.toInt()
        } else {
            @Suppress("DEPRECATION")
            appVersion = pInfo.versionCode
        }
        return appVersion
    }
}

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

@Singleton
class ResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

    fun getString(@StringRes stringResId: Int, value: Int): String {
        return context.getString(stringResId, value)
    }

    fun getString(@StringRes stringResId: Int, value: String): String {
        return context.getString(stringResId, value)
    }
}