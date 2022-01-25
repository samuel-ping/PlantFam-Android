package com.plantfam.plantfam.emailconfirmationscreen

import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.lifecycle.ViewModel
import com.amplifyframework.core.Amplify
import com.plantfam.plantfam.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailConfirmationViewModel @Inject constructor() : ViewModel() {
    fun verifyOTP(
        email: String,
        oneTimePassword: String,
        onVerified: () -> Unit,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ) {
        Amplify.Auth.confirmSignUp(email, oneTimePassword,
            { result ->
                if (result.isSignUpComplete) {
                    Log.i(TAG(), "Confirm sign up is complete.")
                    onVerified()
                } else {
                    Log.i(TAG(), "Confirm sign up not complete.")
                    displaySnackbar("Confirm sign up not complete.", scaffoldState, scope)
                }
            },
            {
                Log.e(TAG(), "Failed to confirm sign up.")
            }
        )
    }

    /**
     * Logs in user.
     */
    fun login(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ) {
        Amplify.Auth.signIn(username, password,
            { result ->
                if (result.isSignInComplete) {
                    Log.i(TAG(), "User successfully logged in.")
                    onSuccess()
                } else {
                    displaySnackbar("Failed to log in.", scaffoldState, scope)
                    Log.e(TAG(), "Failed to log in.")
                }
            },
            {
                Log.e(TAG(), "Failed to sign in:", it)
                displaySnackbar("Failed to log in: ${it.message}", scaffoldState, scope)
            }
        )
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