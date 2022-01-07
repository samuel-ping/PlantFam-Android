package com.example.plantrack_android.ui.addplant

import android.widget.CalendarView
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.plantrack_android.model.Plant
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

@Composable
fun AddPlantScreen() {
    var nickname by remember { mutableStateOf("") }
    var commonName by remember { mutableStateOf("") }
    var scientificName by remember { mutableStateOf("") }
    var adoptionDate by remember { mutableStateOf("") }
    var adoptedFrom by remember { mutableStateOf("") }
    var isDeceased by remember { mutableStateOf("") }
    var parent by remember { mutableStateOf("") }

    TextField(value = nickname, onValueChange = { nickname = it }, label = { Text("Nickname") })
    Spacer(modifier = Modifier.width(8.dp))
    TextField(
        value = commonName,
        onValueChange = { commonName = it },
        label = { Text("Common Name") })
    Spacer(modifier = Modifier.width(8.dp))
    TextField(
        value = scientificName,
        onValueChange = { scientificName = it },
        label = { Text("Scientific Name") })
    Spacer(modifier = Modifier.width(8.dp))
    TextField(
        value = adoptedFrom,
        onValueChange = { nickname = it },
        label = { Text("Adopted From") }) // TODO: Turn into selection
    // TODO: Add adoptionDate datepicker, https://stackoverflow.com/a/68883016/13026376
//    AndroidView(
//        { CalendarView(it) },
//        modifier = Modifier.wrapContentWidth(),
//        update = { views ->
//            views.setOnDateChangeListener { calendarView, i, i2, i3 ->
//            }
//        }
//    )
    // TODO: Add boolean switch for isDeceased
    // TODO: Add selection menu for parent
}

//private fun showDatePicker() {
//    val picker = MaterialDatePicker.Builder.datePicker().build()
//    activity?.let {
//        picker.show(it.supportFragmentManager, picker.toString())
//        picker.addOnPositiveButtonClickListener {
//
//        }
//    }
//}