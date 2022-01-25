package com.plantfam.plantfam.loginscreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.amplifyframework.core.Amplify
import com.plantfam.plantfam.ui.components.CreateAccountForm
import com.plantfam.plantfam.ui.components.LoginForm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LoginScreen(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    viewModel: LoginViewModel
) {
    viewModel.redirectIfLoggedIn { navController.navigate("manageplants") }

    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("New User", "Existing User")

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Log In")
                }
            )
        },
        scaffoldState = scaffoldState,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            TabRow(selectedTabIndex = tabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = { Text(text = title) })
                }
            }
            when (tabIndex) {
                0 -> {
                    scaffoldState
                    CreateAccountForm(
                        email = email,
                        password = password,
                        onEmailChange = { email = it },
                        onPasswordChange = { password = it },
                        onLoginClick = {
                            // TODO: Disable login button and textfields, then reenable if error
                            viewModel.register(
                                email = email,
                                password = password,
                                onSuccess = {
//                                    navController.popBackStack()
                                    navController.navigate("emailconfirmation/$email")
                                },
                                scaffoldState = scaffoldState,
                                scope = scope
                            )
                        },
                    )
                }
                1 -> LoginForm(
                    email = email,
                    password = password,
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it },
                    onLoginClick = {
                        // TODO: Disable login button and textfields, then reenable if error
                        viewModel.login(
                            email = email,
                            password = password,
                            onSuccess = {
                                navController.popBackStack()
                                navController.navigate("manageplants")
                            },
                            scaffoldState = scaffoldState,
                            scope = scope
                        )
                    },
                )
            }
        }
    }
}


