package com.plantfam.plantfam.loginscreen

import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.plantFamApp
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.mongodb.Credentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    var isBusy by mutableStateOf(false)

    fun login(
        username: String,
        password: String,
        creatingUser: Boolean,
        onSuccess: () -> Unit,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ) {
        isBusy = true

        if (!validCredentials(username, password)) {
            displaySnackbar("Invalid credentials.", scaffoldState, scope)
            isBusy = false
            return
        }

        if (creatingUser) {
            plantFamApp.emailPassword.registerUserAsync(username, password) {
                if (!it.isSuccess) {
                    displaySnackbar("Could not register user.", scaffoldState, scope)
                    Log.e(TAG(), "Error: ${it.error}")
                    isBusy = false
                } else {
                    Log.i(TAG(), "Successfully registered user.")
                    login(username, password, true, onSuccess, scaffoldState, scope)
                }
            }
        } else {
            val credentials = Credentials.emailPassword(username, password)
            plantFamApp.loginAsync(credentials) {
                if (!it.isSuccess) {
                    displaySnackbar("Failed to log in.", scaffoldState, scope)
                    Log.e(TAG(), "Error: ${it.error}")
                    isBusy = false
                }
                else {
                    Log.i(TAG(), "User successfully logged in.")
                    isBusy = false
                    onSuccess()
                }
            }
        }
    }

    private fun validCredentials(username: String, password: String): Boolean = when {
        // zero-length usernames and passwords are not valid (or secure), so prevent users from creating accounts with those client-side.
        username.isEmpty() || password.isEmpty() -> false
        else -> true
    }

    private fun displaySnackbar(
        errorMessage: String,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ) {
        scope.launch {
            scaffoldState.snackbarHostState.showSnackbar(errorMessage)
        }
    }
}