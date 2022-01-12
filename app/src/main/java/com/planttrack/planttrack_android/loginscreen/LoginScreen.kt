package com.planttrack.planttrack_android.loginscreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.planttrack.planttrack_android.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    viewModel: LoginViewModel
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("New User", "Existing User")
    var formEnabled by rememberSaveable { mutableStateOf(true) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(true) }

//    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
//            .background(Color.LightGray)
//            .fillMaxSize()
    )
//            .clickable { focusManager.clearFocus() })
    {
        TabRow(selectedTabIndex = tabIndex) { // 3.
            tabTitles.forEachIndexed { index, title ->
                Tab(selected = tabIndex == index, // 4.
                    onClick = { tabIndex = index },
                    text = { Text(text = title) }) // 5.
            }
        }
        when (tabIndex) { // 6.
            0 -> LoginFields(
                isEnabled = isEnabled,
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onLoginClick = {
                    isEnabled = false
                    // TODO: Disable login button and textfields, then reenable if error
                    viewModel.login(
                        email,
                        password,
                        false,
                        {
                            navController.navigate("manageplants")
                        },
                        scaffoldState,
                        scope
                    )
                    isEnabled = true
                },
            )
            1 -> LoginFields(
                isEnabled = isEnabled,
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onLoginClick = {
                    isEnabled = false
                    // TODO: Disable login button and textfields, then reenable if error
                    viewModel.login(
                        email,
                        password,
                        true,
                        {
                            navController.navigate("manageplants")
                        },
                        scaffoldState,
                        scope
                    )
                    isEnabled = true
                },
            )
        }
    }
}

@Composable
fun LoginFields(
    isEnabled: Boolean,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login")

        TextField(
            value = email,
            label = { Text(text = "email") },
            onValueChange = onEmailChange,
            enabled = isEnabled,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        TextField(
            value = password,
            label = { Text(text = "password") },
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            enabled = isEnabled,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        Button(onClick = { onLoginClick() }
        ) {
            Text("Login")
        }
    }
}
