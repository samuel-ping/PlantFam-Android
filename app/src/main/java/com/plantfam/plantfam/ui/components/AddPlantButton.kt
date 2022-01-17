package com.plantfam.plantfam.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AddPlantButton(navHostController: NavHostController) {
    ExtendedFloatingActionButton(
        text = { Text("Add plant") },
        onClick = { navHostController.navigate("addplant") },
        icon = {
            Icon(
                Icons.Filled.Add, "Add new plant", tint = Color.White
            )
        },
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
        backgroundColor = Color.Green,
        contentColor = Color.White,
        modifier = Modifier.padding(16.dp)
    )
}