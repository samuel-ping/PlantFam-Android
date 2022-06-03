package com.plantfam.plantfam.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.plantfam.plantfam.service.model.Plant
import java.io.File

const val MaterialIconDimension = 24f

@ExperimentalMaterialApi
@Composable
fun PlantCard(plant: Plant, onClick: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    val applicationContext = LocalContext.current.applicationContext

    var dropdownMenuExpanded by remember { mutableStateOf(false) }

    Card(
        elevation = 6.dp,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onLongPress = { dropdownMenuExpanded = true },
                onTap = { onClick() }
            )
        }
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            with(plant) {
                Image(
                    painter = rememberImagePainter(File("${applicationContext.filesDir}/${plant.coverPhoto}")),
                    contentDescription = "Photo of ${plant.nickname} the ${plant.scientificName}.",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(128.dp)
                )
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

        DropdownMenu(
            expanded = dropdownMenuExpanded,
            onDismissRequest = { dropdownMenuExpanded = false }) {
            DropdownMenuItem(
                onClick = {
                    dropdownMenuExpanded = false
                    onEdit()
                },
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit plant",
                    Modifier.size(MaterialIconDimension.dp)
                )
                Text("Edit plant", Modifier.weight(1f))
            }
            DropdownMenuItem(
                onClick = {
                    dropdownMenuExpanded = false
                    onDelete()
                },
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete plant",
                    Modifier.size(MaterialIconDimension.dp)
                )
                Text("Delete plant", Modifier.weight(1f))
            }
        }
    }
}