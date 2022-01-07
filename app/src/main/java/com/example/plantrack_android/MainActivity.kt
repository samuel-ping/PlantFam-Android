package com.example.plantrack_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.plantrack_android.model.Plant
import com.example.plantrack_android.ui.theme.PlanTrackAndroidTheme
import java.util.*

class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Hardcoded data, delete when possible
        val plantList = listOf(
            Plant(
                "Shrimp",
                "Echeveria fleur blanc",
                "Echeveria fleur blanc",
                Date(2019, 8, 27),
                "Home Depot",
                false,
                null
            ),
            Plant(
                "Butler",
                "Echeveria cubic frost",
                "Echeveria cubic frost",
                Date(2020, 11, 28),
                "Gift",
                false,
                null
            ),
            Plant(
                "Three Musketeers",
                "Burros tails",
                "Sedum morganianum",
                Date(2021, 2, 24),
                "Gift",
                false,
                null
            )
        )

        setContent {
            PlanTrackAndroidTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ManagePlantsScreen(plants = plantList)
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ManagePlantsScreen(plants: List<Plant>) {
    Scaffold(
        topBar = {/* TODO */},
        drawerContent = {/* TODO */},
        bottomBar = {/* TODO */},
        floatingActionButton = { AddPlantButton()},
        snackbarHost = {/* TODO */},
        content = {
            PlantCardGrid(plants = plants)
        }
    )
}

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

@Composable
private fun PlantCard(plant: Plant) {
    Surface(
        color = MaterialTheme.colors.secondaryVariant,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        PlanTrackAndroidTheme {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "${plant.nickname}",
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${plant.scientificName}", style = MaterialTheme.typography.subtitle1)
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { /* TODO */ }
                ) {
                    Text("Show more")
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

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val plantList = listOf(
        Plant(
            "Shrimp",
            "Echeveria fleur blanc",
            "Echeveria fleur blanc",
            Date(2019, 8, 27),
            "Home Depot",
            false,
            null
        ),
        Plant(
            "Butler",
            "Echeveria cubic frost",
            "Echeveria cubic frost",
            Date(2020, 11, 28),
            "Gift",
            false,
            null
        ),
        Plant(
            "Three Musketeers",
            "Burros tails",
            "Sedum morganianum",
            Date(2021, 2, 24),
            "Gift",
            false,
            null
        )
    )

    PlanTrackAndroidTheme {
        PlantCardGrid(plants = plantList)
    }
}