package com.plantfam.plantfam.ui.screens.manageplantsscreen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.amplifyframework.core.Amplify
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.plantfam.plantfam.ui.components.AddPlantButton
import com.plantfam.plantfam.ui.components.BottomAppBarContent
import com.plantfam.plantfam.ui.components.ConfirmationDialog
import com.plantfam.plantfam.ui.components.PlantCard

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ManagePlantsScreen(navController: NavHostController, viewModel: ManagePlantsViewModel) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val applicationContext = LocalContext.current.applicationContext
    val plants = viewModel.plants

    var showDeletePlantConfirmationDialog by remember { mutableStateOf(false) }

    // Redirect to login page is user is not logged in.
    Amplify.Auth.fetchAuthSession(
        { if(!it.isSignedIn) navController.navigate("login") },
        { Log.e("ManagePlantsScreen", "Failed to fetch auth session.")}
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Plants") })
        },
        bottomBar = { BottomAppBarContent(navController) },
        floatingActionButton = {
            AddPlantButton(navController)
        },
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                viewModel.refresh()
            }
        ) {
            LazyVerticalGrid(
                cells = GridCells.Fixed(count = 2),
                contentPadding = PaddingValues(8.dp)
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



@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    ManagePlantsScreen(plants = mutableListOf())
}
