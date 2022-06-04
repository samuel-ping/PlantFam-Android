package com.plantfam.plantfam.ui.screens.loginscreen

import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import com.plantfam.plantfam.TAG
import com.plantfam.plantfam.plantFamApp
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.mongodb.Credentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    /**
     * Calls onSuccess() if user is logged in.
     */
    fun redirectIfLoggedIn(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if(Amplify.Auth.fetchAuthSession().isSignedIn && plantFamApp.currentUser()?.isLoggedIn == true) onSuccess()
            else Log.i(TAG(), "User not logged in, redirecting to login screen.")
        }
    }

    /**
     * Registers new user account.
     */
    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ) {
        if (!validCredentials(email, password)) {
            displaySnackbar("Invalid credentials.", scaffoldState, scope)
            return
        }

        val options =
            AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build()

        viewModelScope.launch {
            try {
                Amplify.Auth.signUp(email, password, options)
                Log.i(TAG(), "Successfully registered user.")
                onSuccess()
            } catch (failure: AuthException) {
                Log.e(TAG(), "User registration error:", failure)
                displaySnackbar("Could not register user.", scaffoldState, scope)
            }
        }
    }

    /**
     * Logs in user.
     */
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ) {
        viewModelScope.launch {
            try {
                Amplify.Auth.signIn(email, password)
                Log.i(TAG(), "User successfully logged into Amplify Auth.")

                val mobileClient = AWSMobileClient.getInstance()
                Log.d(TAG(), mobileClient.isSignedIn.toString())

                val customJWTCredentials: Credentials = Credentials.jwt(mobileClient.tokens.idToken.tokenString)

                plantFamApp.loginAsync(customJWTCredentials) {
                    if (it.isSuccess) {
                        Log.i(TAG(), "User successfully logged into Realms.")
                        onSuccess()
                    } else {
                        Log.e("AUTH", it.error.toString())
                    }
                }
            } catch (failure: AuthException) {
                Log.e(TAG(), "Failed to sign into Amplify: ")
                displaySnackbar("Failed to log in.", scaffoldState, scope)
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