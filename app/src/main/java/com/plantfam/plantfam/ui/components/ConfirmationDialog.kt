package com.plantfam.plantfam.ui.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable

@Composable
fun ConfirmationDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm)
                { Text(text = "Confirm") }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = "Cancel") }
            },
            title = { Text(text = "Delete Plant?") },
            text = { Text(text = "Are you sure you want to delete this plant?") }
        )
    }
}