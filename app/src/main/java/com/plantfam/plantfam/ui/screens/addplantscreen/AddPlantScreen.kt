package com.plantfam.plantfam.ui.screens.addplantscreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.plantfam.plantfam.R
import com.plantfam.plantfam.network.model.Plant
import com.plantfam.plantfam.ui.components.PlantPickerDialog
import com.plantfam.plantfam.ui.components.SavePlantButton
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Composable
fun AddPlantScreen(navController: NavHostController, viewModel: AddPlantViewModel) {
    val context = LocalContext.current
    val plants = viewModel.plants

    var coverPhoto by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var commonName by remember { mutableStateOf("") }
    var scientificName by remember { mutableStateOf("") }
    var adoptionDate by remember { mutableStateOf(LocalDate.now()) }
    var adoptedFrom by remember { mutableStateOf("") }
    var parent: Plant? by remember { mutableStateOf(null) }
    var isDeceased by remember { mutableStateOf("No") }
    var deceasedDate by remember { mutableStateOf(LocalDate.now()) }

    var parentPickerDialogState by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) coverPhoto = it.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Plant") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close add plant screen"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            SavePlantButton(
                addPlant = {
                    val defaultZoneId: ZoneId = ZoneId.systemDefault()

                    val plant = Plant(
                        _partition = viewModel.user!!.id,
                        nickname = nickname.ifBlank { null },
                        commonName = commonName.ifBlank { null },
                        scientificName = scientificName.ifBlank { null },
                        adoptionDate = Date.from(
                            adoptionDate.atStartOfDay(defaultZoneId).toInstant()
                        ),
                        adoptedFrom = adoptedFrom.ifBlank { null },
                        isDeceased = isDeceased == "Yes",
                        deceasedDate = Date.from(
                            deceasedDate.atStartOfDay(defaultZoneId).toInstant()
                        ),
                        parent = parent,
                        coverPhoto = coverPhoto.ifBlank { null }
                    )

                    viewModel.addPlant(plant)

                    navController.navigateUp()
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (coverPhoto.isBlank()) { // Should only be blank, never null
                    Image(
                        painter = rememberImagePainter(R.drawable.plant_image_placeholder),
                        contentDescription = "Placeholder image for your plant.",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .clickable {
                                launcher.launch("image/*")
                            }
                    )
                } else {
                    Image(
                        painter = rememberImagePainter(
                            data = Uri.parse(coverPhoto),
                            builder = { transformations(CircleCropTransformation()) }
                        ),
                        contentDescription = "Photo of your plant!",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .clickable {
                                launcher.launch("image/*")
                            }
                    )
                }

                Text(
                    text = "Change display photo",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        launcher.launch("image/*")
                    }
                )
            }

            TextField(
                value = nickname,
                onValueChange = { nickname = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nickname") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )

            TextField(
                value = commonName,
                onValueChange = { commonName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Common Name") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )

            TextField(
                value = scientificName,
                onValueChange = { scientificName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Scientific Name") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )

            TextField(
                value = adoptedFrom,
                onValueChange = { adoptedFrom = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Adopted From") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            ) // TODO: Turn into selection

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Date of Adoption:",
                    modifier = Modifier.padding(end = 8.dp),
                    style = MaterialTheme.typography.body1
                )
                Button(
                    onClick = {
                        viewModel.showDatePickerDialog(
                            context,
                            onSelection = { date ->
                                adoptionDate = date
                            }
                        )
                    },
                ) {
                    Text(text = adoptionDate.toString())
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Parent:",
                    modifier = Modifier.padding(end = 8.dp),
                    style = MaterialTheme.typography.body1
                )
                Button(
                    onClick = { parentPickerDialogState = true },
                ) {
                    Text(text = parent?.nickname ?: "Unknown")
                }

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = "Reset",
                    color = Color.Blue,
                    modifier = Modifier.clickable { parent = null }
                )
            }
            if (parentPickerDialogState) {
                PlantPickerDialog(
                    plants = plants,
                    onSelection = { plant ->
                        parent = plant
                    },
                    dismiss = {
                        parentPickerDialogState = false
                    }
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Entered Plant Heaven?",
                    style = MaterialTheme.typography.body1
                )
                RadioButton(
                    selected = isDeceased == "No",
                    onClick = {
                        isDeceased = "No"
                    }
                )
                Text(
                    text = "No",
                )

                Spacer(modifier = Modifier.size(4.dp))

                RadioButton(
                    selected = isDeceased == "Yes",
                    onClick = {
                        isDeceased = "Yes"
                    }
                )
                Text(
                    text = "Yes",
                )
            }

            if (isDeceased == "Yes") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Date of Expiration:",
                        modifier = Modifier.padding(end = 8.dp),
                        style = MaterialTheme.typography.body1
                    )
                    Button(
                        onClick = {
                            viewModel.showDatePickerDialog(
                                context,
                                onSelection = { date ->
                                    deceasedDate = date
                                }
                            )
                        },
                    ) {
                        Text(text = deceasedDate.toString())
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    AddPlantScreen()
}