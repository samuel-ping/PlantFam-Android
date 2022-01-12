package com.planttrack.planttrack_android

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.annotations.RealmModule
import io.realm.log.LogLevel
import io.realm.log.RealmLog
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration

lateinit var plantTrackApp: App

inline fun <reified T> T.TAG(): String = T::class.java.simpleName
const val PARTITION_EXTRA_KEY = "PARTITION"
const val PROJECT_NAME_EXTRA_KEY = "PROJECT NAME"

@HiltAndroidApp
class PlantTrackApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize the Realm SDK
        Realm.init(this)
        plantTrackApp = App(
            AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                .defaultSyncErrorHandler { session, error ->
                    Log.e(TAG(), "Sync error: ${error.errorMessage}")
                }
                .build())

        // Enable more logging in debug mode
        if (BuildConfig.DEBUG) {
            RealmLog.setLevel(LogLevel.ALL)
        }

        Log.v(TAG(), "Initialized the Realm App configuration for: ${plantTrackApp.configuration.appId}")
    }
}