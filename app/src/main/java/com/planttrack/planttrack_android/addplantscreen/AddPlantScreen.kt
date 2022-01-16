package com.planttrack.planttrack_android.addplantscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.planttrack.planttrack_android.ui.components.AddPlantButton
import com.planttrack.planttrack_android.ui.components.BottomAppBarContent
import com.planttrack.planttrack_android.ui.components.SavePlantButton
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun AddPlantScreen(navController: NavHostController, viewModel: AddPlantViewModel) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Plant")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close add plant screen"
                        )
                    }
                }
            )
        },
        floatingActionButton = { SavePlantButton(navController) },
    ) {

        Column {
            Text(
                text = "Nickname:",
                modifier = Modifier
                    .padding(8.dp),
                style = MaterialTheme.typography.body1
            )
            TextField(
                value = nickname,
                onValueChange = { nickname = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Nickname") })

            Text(
                text = "Common Name:",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.body1
            )
            TextField(
                value = commonName,
                onValueChange = { commonName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Common Name") })

            Text(
                text = "Scientific Name:",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.body1
            )
            TextField(
                value = scientificName,
                onValueChange = { scientificName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Scientific Name") })

            Text(
                text = "Adopted From:",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.body1
            )
            TextField(
                value = adoptedFrom,
                onValueChange = { adoptedFrom = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Adopted From") }) // TODO: Turn into selection

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

            // TODO: Add selection menu for parent
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    AddPlantScreen()
}