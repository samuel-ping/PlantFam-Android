package com.plantfam.plantfam.ui.screens.manageplantsscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.plantfam.plantfam.ui.components.AddPlantButton
import com.plantfam.plantfam.ui.components.ConfirmationDialog
import com.plantfam.plantfam.ui.components.PlantCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ManagePlantsScreen(navController: NavHostController, viewModel: ManagePlantsViewModel) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val applicationContext = LocalContext.current.applicationContext
    val plants = viewModel.plants

    var showDeletePlantConfirmationDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Plants") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("settings") {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Go to settings"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AddPlantButton(navController)
        },
    ) {
        Column(
            modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp).padding(it)
        ) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = {
                    viewModel.refresh()
                }
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 180.dp),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(0.dp,8.dp)
                ) {
                    items(plants.size) { plant ->
                        PlantCard(
                            plants[plant],
                            onClick = {
                                navController.navigate("details/${plants[plant].id}")
                            },
                            onEdit = {},
                            onDelete = { showDeletePlantConfirmationDialog = true }
                        ) // TODO: plants[plant] seems unnecessary?

                        ConfirmationDialog(
                            show = showDeletePlantConfirmationDialog,
                            title = "Permanently Delete ${plants[plant].nickname}?",
                            description = "Are you sure you want to delete ${plants[plant].nickname}? If this plant has passed away, you can move it to the graveyard instead of deleting it.",
                            onDismiss = { showDeletePlantConfirmationDialog = false },
                            onConfirm = {
                                viewModel.deletePlant(plants[plant])
                                showDeletePlantConfirmationDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    ManagePlantsScreen(plants = mutableListOf())
}
