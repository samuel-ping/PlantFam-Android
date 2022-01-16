package com.planttrack.planttrack_android.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun SavePlantButton(addPlant: () -> Unit) {
    ExtendedFloatingActionButton(
        text = { Text("Save") },
        onClick = { addPlant() },
        icon = { Icon(Icons.Filled.Save, "Save Plant", tint = Color.White) },
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
        backgroundColor = Color.Green,
        contentColor = Color.White,
        modifier = Modifier.padding(16.dp)
    )
}