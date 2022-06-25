package com.plantfam.plantfam.ui.screens.settingsscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.plantfam.plantfam.ui.components.BottomAppBarContent

@Composable
fun SettingsScreen(navController: NavHostController, viewModel: SettingsViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close settings screen"
                        )
                    }
                }
            )
        },
    ) {
        LazyColumn {
            item {
                Button(
                    onClick = {
                        viewModel.logout {
                            navController.popBackStack()
                            navController.navigate("login")
                        }
                    },
                    border = BorderStroke(1.dp, Color.Red),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Log out",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = "Log out")
                }
            }
        }
    }
}