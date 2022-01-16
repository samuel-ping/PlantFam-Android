package com.planttrack.planttrack_android.manageplantsscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.planttrack.planttrack_android.plantTrackApp
import com.planttrack.planttrack_android.service.model.Plant
import com.planttrack.planttrack_android.ui.components.AddPlantButton
import com.planttrack.planttrack_android.ui.components.BottomAppBarContent
import com.planttrack.planttrack_android.ui.components.SavePlantButton
import com.planttrack.planttrack_android.ui.theme.PlantTrackAndroidTheme
import io.realm.Realm
import io.realm.mongodb.User
import java.time.LocalDate

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ManagePlantsScreen(navController: NavHostController, viewModel: ManagePlantsViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Plants") })
        },
        bottomBar = { BottomAppBarContent(navController) },
        floatingActionButton = {
            AddPlantButton(navController)
        },
    ) {
        PlantCardGrid(plants = viewModel.plants)
    }
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
        PlantTrackAndroidTheme {
            Column(modifier = Modifier.padding(24.dp)) {
                with(plant) {
                    nickname?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colors.secondaryVariant,
                            style = MaterialTheme.typography.h5
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "the $commonName.", style = MaterialTheme.typography.subtitle1)
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
//    val plantList = listOf(
//        Plant(
//            "Shrimp",
//            "Echeveria fleur blanc",
//            "Echeveria fleur blanc",
//            LocalDate.of(2019, 7, 27),
//            "Home Depot",
//            false,
//            null,
//            null
//        ),
//        Plant(
//            "Butler",
//            "Echeveria cubic frost",
//            "Echeveria cubic frost",
//            LocalDate.of(2020, 10, 28),
//            "Gift",
//            false,
//            null,
//            null
//        ),
//        Plant(
//            "Three Musketeers",
//            "Burros tails",
//            "Sedum morganianum",
//            LocalDate.of(2021, 1, 24),
//            "Gift",
//            false,
//            null,
//            null
//        )
//    )

//    ManagePlantsScreen(plants = mutableListOf())
}
