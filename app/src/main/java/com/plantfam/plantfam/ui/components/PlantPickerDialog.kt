package com.plantfam.plantfam.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.plantfam.plantfam.R
import com.plantfam.plantfam.network.model.Plant

@Composable
fun PlantPickerDialog(
    plants: List<Plant>,
    onSelection: (Plant) -> Unit,
    dismiss: () -> Unit,
) {
    Dialog(onDismissRequest = dismiss) {
        Box {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 64.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.White)
            ) {
                for (plant in plants) {
                    item {
                        Row(modifier = Modifier
                            .clickable {
                                onSelection(plant)
                                dismiss()
                            }) {
                            if (plant.coverPhoto.isNullOrBlank()) { // Should only be blank, never null
                                Image(
                                    painter = rememberImagePainter(R.drawable.plant_image_placeholder),
                                    contentDescription = "Placeholder image for your plant.",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(50.dp)
                                )
                            } else {
                                Image(
                                    painter = rememberImagePainter(
                                        data = Uri.parse(plant.coverPhoto),
                                        builder = { transformations(CircleCropTransformation()) }
                                    ),
                                    contentDescription = "Photo of your plant!",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(40.dp)
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                text = "${plant.nickname}"
                            )
                        }
                    }
                }
            }
        }
    }
}