package com.planttrack.planttrack_android.plantdetailsscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun PlantDetailsScreen(plantId: String, navController: NavHostController, viewModel: PlantDetailsViewModel) {
    val plant = viewModel.getPlant(plantId)
    Scaffold(
        topBar = {
            TopAppBar(
//                title = { Text(plant?.nickname!!) },
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Close plant details screen"
                        )
                    }
                }
            )
        },
    ) {
        Column {
//            plant?.apply {
//                this.nickname?.let { it -> Text(it) }
//                this.commonName?.let { it -> Text(it) }
//                this.scientificName?.let { it -> Text(it) }
//                this.adoptionDate?.let { it -> Text(it.toString()) }
//                this.adoptedFrom?.let { it -> Text(it) }
//                this.isDeceased?.let { it -> Text(it.toString()) }
//            }
            with(plant!!) {
                nickname?.let { it1 -> Text(it1) }
                commonName?.let { it -> Text(it) }
                scientificName?.let { it -> Text(it) }
                adoptionDate?.let { it -> Text(it.toString()) }
                adoptedFrom?.let { it -> Text(it) }
            }
        }

    }
}