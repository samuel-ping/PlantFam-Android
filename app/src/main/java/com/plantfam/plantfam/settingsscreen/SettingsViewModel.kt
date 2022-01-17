package com.plantfam.plantfam.settingsscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.plantFamApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    fun logout(onSuccess: () -> Unit) {
        plantFamApp.currentUser()?.logOutAsync {
            if (it.isSuccess) {
                Log.v(TAG(), "User logged out.")
                onSuccess()
            } else {
                Log.e(TAG(), "Log out failed! Error: ${it.error}")
            }
        }
    }
}