package com.example.plantrack_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        val plantList = listOf<Plant>(
            Plant("Shrimp", "Echeveria fleur blanc", Date(2019, 8, 27)),
            Plant("Butler", "Echeveria cubic frost", Date(2020, 11, 28))
        )

        setContent {
            PlanTrackAndroidTheme {
                Surface(color = MaterialTheme.colors.background) {
                    PlantCardGrid(plants = plantList)
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun PlantCardGrid(plants: List<Plant>) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(count = 3),
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

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val plantList = listOf<Plant>(
        Plant("Shrimp", "Echeveria fleur blanc", Date(2019, 8, 27)),
        Plant("Butler", "Echeveria cubic frost", Date(2020, 11, 28))
    )

    PlanTrackAndroidTheme {
        PlantCardGrid(plants = plantList)
    }
}