package com.plantfam.plantfam.ui.screens.addplantscreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.plantfam.plantfam.R
import com.plantfam.plantfam.network.model.Plant
import com.plantfam.plantfam.ui.components.SavePlantButton
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Composable
fun AddPlantScreen(navController: NavHostController, viewModel: AddPlantViewModel) {
    val context = LocalContext.current

    var coverPhoto by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var commonName by remember { mutableStateOf("") }
    var scientificName by remember { mutableStateOf("") }
    var adoptionDate by remember { mutableStateOf(LocalDate.now()) }
    var adoptedFrom by remember { mutableStateOf("") }
//    var parent by remember { mutableStateOf("") }
    var isDeceased by remember { mutableStateOf("No") }
    var deceasedDate by remember { mutableStateOf(LocalDate.now()) }

    val adoptionDatePickerDialogState = rememberMaterialDialogState()
    val deceasedDatePickerDialogState = rememberMaterialDialogState()

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
                        nickname = if (nickname.isBlank()) null else nickname,
                        commonName = if (commonName.isBlank()) null else commonName,
                        scientificName = if (scientificName.isBlank()) null else scientificName,
                        adoptionDate = Date.from(
                            adoptionDate.atStartOfDay(defaultZoneId).toInstant()
                        ),
                        adoptedFrom = if (adoptedFrom.isBlank()) null else adoptedFrom,
                        isDeceased = isDeceased == "Yes",
                        deceasedDate = if (isDeceased == "Yes") Date.from(
                            deceasedDate.atStartOfDay(defaultZoneId).toInstant()
                        ) else null,
                        parent = null,
                        coverPhoto = if(coverPhoto.isBlank()) null else coverPhoto
                    )

                    viewModel.addPlant(plant)

                    navController.navigateUp()
                }
            )
        },
    ) {
        LazyColumn(
            contentPadding = PaddingValues(0.dp, 8.dp, 0.dp, 72.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (coverPhoto.isNullOrBlank()) { // Should only be blank, never null
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
                        })
                }
            }

            item {
                TextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = { Text("Nickname") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )
            }

            item {
                TextField(
                    value = commonName,
                    onValueChange = { commonName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = { Text("Common Name") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )
            }

            item {
                TextField(
                    value = scientificName,
                    onValueChange = { scientificName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = { Text("Scientific Name") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )
            }

            item {
                TextField(
                    value = adoptedFrom,
                    onValueChange = { adoptedFrom = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = { Text("Adopted From") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                ) // TODO: Turn into selection
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Date of Adoption:",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.body1
                    )
                    Button(
                        onClick = {
                            adoptionDatePickerDialogState.show()
                        },
                    ) {
                        Text(text = adoptionDate.toString())
                    }
                }
                MaterialDialog(
                    dialogState = adoptionDatePickerDialogState,
                    buttons = {
                        positiveButton("Ok")
                        negativeButton("Cancel")
                    }
                ) {
                    datepicker { date ->
                        adoptionDate = date;
                    }
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Entered Plant Heaven?",
                        modifier = Modifier
                            .padding(start = 8.dp),
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
            }

            item {
                if (isDeceased == "Yes") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Date of Expiration:",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.body1
                        )
                        Button(
                            onClick = {
                                deceasedDatePickerDialogState.show()
                            },
                        ) {
                            Text(text = deceasedDate.toString())
                        }
                    }
                    MaterialDialog(
                        dialogState = deceasedDatePickerDialogState,
                        buttons = {
                            positiveButton("Ok")
                            negativeButton("Cancel")
                        }
                    ) {
                        datepicker { date ->
                            deceasedDate = date;
                        }
                    }
                }
            }
            // TODO: Add selection menu for parent
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    AddPlantScreen()
}