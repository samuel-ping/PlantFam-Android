package com.planttrack.planttrack_android.manageplantsscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.planttrack.planttrack_android.service.model.Plant
import com.planttrack.planttrack_android.ui.components.AddPlantButton
import com.planttrack.planttrack_android.ui.components.BottomAppBarContent
import com.planttrack.planttrack_android.ui.components.PlantCard
import com.planttrack.planttrack_android.ui.theme.PlantTrackAndroidTheme

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ManagePlantsScreen(navController: NavHostController, viewModel: ManagePlantsViewModel) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()

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
            onRefresh = { viewModel.refresh() }
        ) {
            PlantCardGrid(viewModel.plants, navController)
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun PlantCardGrid(plants: List<Plant>, navController: NavHostController) {
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
                onDelete = {}
            ) // TODO: plants[plant] seems unnecessary?
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
