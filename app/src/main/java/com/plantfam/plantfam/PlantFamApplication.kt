package com.plantfam.plantfam

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.log.LogLevel
import io.realm.log.RealmLog
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration

lateinit var plantFamApp: App

inline fun <reified T> T.TAG(): String = T::class.java.simpleName

@HiltAndroidApp
class PlantFamApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize the Realm SDK
        Realm.init(this)
        plantFamApp = App(
            AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                .defaultSyncErrorHandler { _, error ->
                    Log.e(TAG(), "Sync error: ${error.errorMessage}")
                }
                .build())

        // Enable more logging in debug mode
        if (BuildConfig.DEBUG) {
            RealmLog.setLevel(LogLevel.ALL)
        }

        Log.v(TAG(), "Initialized the Realm App configuration for: ${plantFamApp.configuration.appId}")
    }
}