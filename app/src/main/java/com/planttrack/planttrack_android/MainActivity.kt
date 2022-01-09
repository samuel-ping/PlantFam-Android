package com.planttrack.planttrack_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.planttrack.planttrack_android.model.Plant
import com.planttrack.planttrack_android.ui.components.AddPlantButton
import com.planttrack.planttrack_android.addplantscreen.AddPlantScreen
import com.planttrack.planttrack_android.manageplantsscreen.ManagePlantsScreen
import com.planttrack.planttrack_android.ui.components.SavePlantButton
import com.planttrack.planttrack_android.ui.theme.PlantTrackAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // TODO: Hardcoded data, delete when possible
    private val plantList = listOf(
        Plant(
            "Shrimp",
            "Echeveria fleur blanc",
            "Echeveria fleur blanc",
            LocalDate.of(2019, 7, 27),
            "Home Depot",
            false,
            null,
            null
        ),
        Plant(
            "Butler",
            "Echeveria cubic frost",
            "Echeveria cubic frost",
            LocalDate.of(2020, 10, 28),
            "Gift",
            false,
            null,
            null
        ),
        Plant(
            "Three Musketeers",
            "Burros tails",
            "Sedum morganianum",
            LocalDate.of(2021, 1, 24),
            "Gift",
            false,
            null,
            null
        )
    )

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlantTrackAndroidTheme {
                PlantTrackApp()
            }
        }
    }

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @Composable
    fun PlantTrackApp() {
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()

        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            when (backstackEntry.value?.destination?.route) {
                                "manageplants" -> Text("PlantTrack")
                                "addplant" -> Text("Add Plant")
                            }
                        },
                        navigationIcon = if (navController.previousBackStackEntry != null) {
                            {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    when (backstackEntry.value?.destination?.route) {
                                        "addplant" -> Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Close add plant screen"
                                        )
                                        else -> Icon(
                                            imageVector = Icons.Filled.ArrowBack,
                                            contentDescription = "Go back"
                                        )
                                    }
                                }
                            }
                        } else {
                            null
                        }
                    )
                },
//                    drawerContent = {/* TODO */ },
//                    bottomBar = {/* TODO */ },
                floatingActionButton = {
                    when (backstackEntry.value?.destination?.route) {
                        "manageplants" -> AddPlantButton(navController)
                        "addplant" -> SavePlantButton(navController)
                    }
                },
//                    snackbarHost = {/* TODO */ },
            ) {
                PlantNavHost(navController)
            }
        }
    }

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @Composable
    fun PlantNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
        NavHost(
            navController = navController,
            startDestination = "manageplants",
            modifier = modifier
        ) {
            composable("manageplants") {
                ManagePlantsScreen(plants = plantList)
            }
            composable("addplant") {
                AddPlantScreen()
            }
        }
    }
}