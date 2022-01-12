package com.planttrack.planttrack_android.loginscreen

import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.lifecycle.ViewModel
import com.planttrack.planttrack_android.TAG
import com.planttrack.planttrack_android.plantTrackApp
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.mongodb.Credentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
//    var scaffoldState = ScaffoldState(DrawerState(initialValue = DrawerValue.Closed), SnackbarHostState())
//    var isSnackBarShowing: Boolean by mutableStateOf(false)
//        private set

    fun login(
        username: String,
        password: String,
        creatingUser: Boolean,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ) {
        if (!validCredentials(username, password)) {
            onLoginFailed("Invalid credentials.", scaffoldState, scope)
            return
        }

        if (creatingUser) {
            plantTrackApp.emailPassword.registerUserAsync(username, password) {
                if (!it.isSuccess) {
                    onLoginFailed("Could not register user.", scaffoldState, scope)
                    Log.e(TAG(), "Error: ${it.error}")
                } else {
                    Log.i(TAG(), "Successfully registered user.")
                    login(username, password, false, onSuccess, onFailure, scaffoldState, scope)
                }
            }
        } else {
            val credentials = Credentials.emailPassword(username, password)
            plantTrackApp.loginAsync(credentials) {
                if (!it.isSuccess) {
                    onLoginFailed("Failed to log in.", scaffoldState, scope)
                    Log.e(TAG(), "Error: ${it.error}")
                    onFailure
                }
                else onSuccess
            }
        }
    }

    private fun validCredentials(username: String, password: String): Boolean = when {
        // zero-length usernames and passwords are not valid (or secure), so prevent users from creating accounts with those client-side.
        username.isEmpty() || password.isEmpty() -> false
        else -> true
    }

    private fun onLoginFailed(
        errorMessage: String,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ) {
        scope.launch {
            scaffoldState.snackbarHostState.showSnackbar(errorMessage)
        }
    }

//    private fun onLoginSuccess() {
//
//    }
}