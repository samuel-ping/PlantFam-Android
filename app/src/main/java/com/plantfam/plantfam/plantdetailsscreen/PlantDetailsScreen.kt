package com.plantfam.plantfam.plantdetailsscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.plantfam.plantfam.R

@Composable
fun PlantDetailsScreen(
    plantId: String,
    navController: NavHostController,
    viewModel: PlantDetailsViewModel
) {
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
        with(plant!!) {
            Column {
                if(plant.coverPhoto == null) {
                    val painter = rememberImagePainter(R.drawable.plant_image_placeholder)
                    val painterState = painter.state

                    AnimatedVisibility(visible = (painterState is ImagePainter.State.Loading)) {
                        CircularProgressIndicator()
                    }
                    Image(
                        painter = painter,
                        contentDescription = "Placeholder image for your plant.",
                        modifier = Modifier.size(128.dp).clip(CircleShape)
                    )
                }
                Image(
                    painter = rememberImagePainter(
                        data = "g",
                        builder = {
//                            placeholder()
                            transformations(CircleCropTransformation())
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp)
                )
                nickname?.let { it1 -> Text(it1) }
                commonName?.let { it -> Text(it) }
                scientificName?.let { it -> Text(it) }
                Text(adoptionDate.toString())
                adoptedFrom?.let { it -> Text(it) }
            }
        }
    }
}