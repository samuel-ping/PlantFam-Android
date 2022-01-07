package com.example.plantrack_android.ui.manageplants

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.plantrack_android.model.Plant
import com.example.plantrack_android.ui.theme.PlanTrackAndroidTheme
import java.util.*

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ManagePlantsScreen(plants: List<Plant>) {
    Scaffold(
        topBar = {/* TODO */ },
        drawerContent = {/* TODO */ },
        bottomBar = {/* TODO */ },
        floatingActionButton = { AddPlantButton() },
        snackbarHost = {/* TODO */ },
        content = {
            PlantCardGrid(plants = plants)
        }
    )
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun PlantCardGrid(plants: List<Plant>) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(count = 2),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(plants.size) { plant ->
            PlantCard(plants[plant]) // TODO: This seems wrong?
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun PlantCard(plant: Plant) {
    Card(onClick = { /*TODO*/ }, elevation = 6.dp) {
        PlanTrackAndroidTheme {
            Column(modifier = Modifier.padding(24.dp)) {
                with(plant) {
                    Text(
                        text = nickname,
                        color = MaterialTheme.colors.secondaryVariant,
                        style = MaterialTheme.typography.h5
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "the $commonName.", style = MaterialTheme.typography.subtitle1)
                }

            }
        }
    }
}

@Composable
fun AddPlantButton() {
    ExtendedFloatingActionButton(
        text = { Text("Add plant") },
        onClick = { /*TODO*/ },
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

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val plantList = listOf(
        Plant(
            "Shrimp",
            "Echeveria fleur blanc",
            "Echeveria fleur blanc",
            GregorianCalendar(2019 + 1900, 7, 27),
            "Home Depot",
            false,
            null
        ),
        Plant(
            "Butler",
            "Echeveria cubic frost",
            "Echeveria cubic frost",
            GregorianCalendar(2020 + 1900, 10, 28),
            "Gift",
            false,
            null
        ),
        Plant(
            "Three Musketeers",
            "Burros tails",
            "Sedum morganianum",
            GregorianCalendar(2021 + 1900, 1, 24),
            "Gift",
            false,
            null
        )
    )

    PlanTrackAndroidTheme {
        PlantCardGrid(plants = plantList)
    }
}
