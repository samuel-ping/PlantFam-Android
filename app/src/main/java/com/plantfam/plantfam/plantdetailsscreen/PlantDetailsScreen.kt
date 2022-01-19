package com.plantfam.plantfam.plantdetailsscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (plant.coverPhoto.isNullOrBlank()) {
                        val painter = rememberImagePainter(R.drawable.plant_image_placeholder)
                        val painterState = painter.state

                        AnimatedVisibility(visible = (painterState is ImagePainter.State.Loading)) {
                            CircularProgressIndicator()
                        }
                        Image(
                            painter = painter,
                            contentDescription = "Placeholder image for your plant.",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(128.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = rememberImagePainter(
                                data = "g",
                                builder = {
                                    transformations(CircleCropTransformation())
                                    // TODO: Add placeholder
                                }
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(128.dp)
                        )
                    }
                }

                nickname?.let { it1 -> Text(it1) }
                commonName?.let { it -> Text(it) }
                scientificName?.let { it -> Text(it) }
                Text(adoptionDate.toString())
                adoptedFrom?.let { it -> Text(it) }
            }
        }
    }
}