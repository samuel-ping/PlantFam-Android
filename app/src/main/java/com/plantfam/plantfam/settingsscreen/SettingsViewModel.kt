package com.plantfam.plantfam.settingsscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amplifyframework.core.Amplify
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.plantFamApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    fun logout(onSuccess: () -> Unit) {
        Amplify.Auth.signOut(
            { Log.i(TAG(), "Signed out of Amplify Auth successfully.") },
            { Log.e(TAG(), "Sign out of Amplify Auth failed", it) }
        )

        plantFamApp.currentUser()?.logOutAsync {
            if (it.isSuccess) {
                Log.v(TAG(), "User logged out of Realms.")
                onSuccess()
            } else {
                Log.e(TAG(), "Log out failed! Error: ${it.error}")
            }
        }
    }
}