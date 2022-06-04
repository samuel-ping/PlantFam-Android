package com.plantfam.plantfam.ui.screens.settingsscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthException
import com.amplifyframework.kotlin.core.Amplify
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.plantFamApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    fun logout(onSuccess: () -> Unit) {
        plantFamApp.currentUser()?.logOutAsync {
            if (it.isSuccess) {
                Log.v(TAG(), "User logged out of Realms.")
            } else {
                Log.e(TAG(), "Log out failed! Error: ${it.error}")
            }
        }

        viewModelScope.launch {
            try {
                Amplify.Auth.signOut()
                Log.i(TAG(), "Signed out of Amplify Auth successfully.")
                onSuccess()
            } catch (failure: AuthException.SignedOutException) {
                Log.e(TAG(), "Sign out of Amplify Auth failed", failure)
            }
        }
    }
}