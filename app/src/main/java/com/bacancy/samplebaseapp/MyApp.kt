package com.bacancy.samplebaseapp

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().log("App started")
    }

}