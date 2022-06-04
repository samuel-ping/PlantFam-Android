package com.plantfam.plantfam.ui.screens.emailconfirmationscreen

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