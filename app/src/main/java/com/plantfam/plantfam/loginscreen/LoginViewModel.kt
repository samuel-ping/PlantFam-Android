package com.plantfam.plantfam.loginscreen

import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.lifecycle.ViewModel
import com.amazonaws.mobile.client.AWSMobileClient
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
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
    fun redirectIfLoggedIn(onSuccess: () -> Unit): Boolean = Amplify.Auth.currentUser != null

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

        Amplify.Auth.signUp(email, password, options,
            {
                Log.i(TAG(), "Successfully registered user.")
                Log.d(TAG(), Thread.currentThread().name)
                onSuccess()
            },
            {
                displaySnackbar("Could not register user.", scaffoldState, scope)
                Log.e(TAG(), "User registration error:", it)
                Log.d(TAG(), Thread.currentThread().name)
            }
        )
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
        Amplify.Auth.signIn(email, password,
            { result ->
                if (result.isSignInComplete) {
                    Log.i(TAG(), "User successfully logged in.")

                    // Log in Realms user using JWT from Cognito
                    val cognitoAuthPlugin = Amplify.Auth.getPlugin("awsCognitoAuthPlugin")
                    val mobileClient = cognitoAuthPlugin.escapeHatch as AWSMobileClient
                    val customJwtCredentials: Credentials =
                        Credentials.jwt(mobileClient.tokens.idToken.tokenString)

                    plantFamApp.login(customJwtCredentials)

//                    plantFamApp.login(customJwtCredentials) {
//                        if(it.isSuccess) {
//                            Log.i(TAG(), "Logged into realms via JWT.")
//                            onSuccess()
//                        } else {
//                            Log.e(TAG(), "Error logging into Realms: ${it.error}")
//                        }
//                    }

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