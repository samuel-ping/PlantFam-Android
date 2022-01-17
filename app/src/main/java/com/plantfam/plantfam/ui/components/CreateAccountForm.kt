package com.plantfam.plantfam.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CreateAccountForm(
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
        Text("Create Account")

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
            Text("Create Account")
        }
    }
}