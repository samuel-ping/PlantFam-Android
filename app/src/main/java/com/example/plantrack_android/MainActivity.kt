package com.example.plantrack_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantrack_android.ui.theme.PlanTrackAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanTrackAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PlantCard(nickname = "Shrimp", scientificName = "Echeveria fleur blanc")
                }
            }
        }
    }
}

//@Composable
//private fun PlantCardGrid(plants: List<Plant>) {
//
//}

@Composable
private fun PlantCard(nickname: String, scientificName: String) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        PlanTrackAndroidTheme {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "$nickname",
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "$scientificName", style = MaterialTheme.typography.subtitle1)
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { /* TODO */ }
                ) {
                    Text("Show more")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlanTrackAndroidTheme {
        PlantCard(nickname = "Shrimp", scientificName = "Echeveria fleur blanc")
    }
}