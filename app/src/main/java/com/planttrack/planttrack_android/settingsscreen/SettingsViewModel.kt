package com.planttrack.planttrack_android.settingsscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.planttrack.planttrack_android.TAG
import com.planttrack.planttrack_android.plantTrackApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    fun logout(onSuccess: () -> Unit) {
        plantTrackApp.currentUser()?.logOutAsync {
            if (it.isSuccess) {
                Log.v(TAG(), "User logged out.")
                onSuccess()
            } else {
                Log.e(TAG(), "Log out failed! Error: ${it.error}")
            }
        }
    }
}