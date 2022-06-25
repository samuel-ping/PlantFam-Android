package com.plantfam.plantfam.ui.screens.emailconfirmationscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope

@Composable
fun EmailConfirmationScreen(
    email: String,
    navController: NavController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    viewModel: EmailConfirmationViewModel
) {
    var oneTimePassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close email confirmation screen"
                        )
                    }
                }
            )
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm your email address")
            Text("Enter the One Time Password sent to $email:")

            TextField(
                value = oneTimePassword,
                onValueChange = { oneTimePassword = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("One Time Password") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )

            Button(
                onClick = {
                    viewModel.verifyOTP(
                        email = email,
                        oneTimePassword = oneTimePassword,
                        onVerified = {
                             navController.navigate("login")
                        },
                        scaffoldState = scaffoldState,
                        scope = scope
                    )
                },
            ) {
                Text("Verify & Proceed")
            }
        }
    }
}
