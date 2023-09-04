package com.endcodev.saber_y_beber

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SaberYBeberApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}
