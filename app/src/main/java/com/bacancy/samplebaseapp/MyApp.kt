package com.bacancy.samplebaseapp

import android.app.Application
import com.bacancy.samplebaseapp.forKoin.appModule
import com.bacancy.samplebaseapp.local.databaseModule
import com.bacancy.samplebaseapp.silentUpdates.networkModule
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().log("App started")
        NetworkHelper.initialize(this)

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(appModule, networkModule, databaseModule))
        }
    }

}