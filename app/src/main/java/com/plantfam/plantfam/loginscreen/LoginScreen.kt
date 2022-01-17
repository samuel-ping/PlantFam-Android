package com.plantfam.plantfam.loginscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.plantfam.plantfam.ui.components.CreateAccountForm
import com.plantfam.plantfam.ui.components.LoginForm
import kotlinx.coroutines.CoroutineScope

@Composable
fun LoginScreen(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    viewModel: LoginViewModel
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("New User", "Existing User")

    var formEnabled by remember { mutableStateOf(true) }
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
            modifier = Modifier
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
                        isEnabled = formEnabled,
                        email = email,
                        password = password,
                        onEmailChange = { email = it },
                        onPasswordChange = { password = it },
                        onLoginClick = {
                            formEnabled = false
                            // TODO: Disable login button and textfields, then reenable if error
                            viewModel.login(
                                email,
                                password,
                                true,
                                {
                                    navController.popBackStack()
                                    navController.navigate("manageplants")
                                },
                                scaffoldState,
                                scope
                            )
                            formEnabled = true
                        },
                    )
                }
                1 -> LoginForm(
                    isEnabled = formEnabled,
                    email = email,
                    password = password,
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it },
                    onLoginClick = {
                        formEnabled = false
                        // TODO: Disable login button and textfields, then reenable if error
                        viewModel.login(
                            email,
                            password,
                            false,
                            {
                                navController.popBackStack()
                                navController.navigate("manageplants")
                            },
                            scaffoldState,
                            scope
                        )
                        formEnabled = true
                    },
                )
            }
        }
    }
}


