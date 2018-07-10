package com.rosterloh.mirror

import android.app.Application
//import com.google.android.things.device.TimeManager
import timber.log.Timber

class MirrorApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // TimeManager.getInstance().setTimeZone("Europe/London")
    }
}
