package com.plantfam.plantfam.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun AddPlantButton(navHostController: NavController) {
    FloatingActionButton(
        shape = CircleShape,
        onClick = { navHostController.navigate("addplant") },
        contentColor = Color.White,
    ) {
        Icon(Icons.Filled.Add, "Add new plant", tint = Color.White)
    }
}