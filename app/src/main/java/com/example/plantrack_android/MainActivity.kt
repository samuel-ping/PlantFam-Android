package com.example.plantrack_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.plantrack_android.model.Plant
import com.example.plantrack_android.ui.manageplants.AddPlantButton
import com.example.plantrack_android.ui.manageplants.ManagePlantsScreen
import com.example.plantrack_android.ui.theme.PlanTrackAndroidTheme
import java.util.*

class MainActivity : ComponentActivity() {
    // TODO: Hardcoded data, delete when possible
    private val plantList = listOf(
        Plant(
            "Shrimp",
            "Echeveria fleur blanc",
            "Echeveria fleur blanc",
            GregorianCalendar(2019 + 1900, 7, 27),
            "Home Depot",
            false,
            null
        ),
        Plant(
            "Butler",
            "Echeveria cubic frost",
            "Echeveria cubic frost",
            GregorianCalendar(2020 + 1900, 10, 28),
            "Gift",
            false,
            null
        ),
        Plant(
            "Three Musketeers",
            "Burros tails",
            "Sedum morganianum",
            GregorianCalendar(2021 + 1900, 1, 24),
            "Gift",
            false,
            null
        )
    )

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlantTrackApp()
        }
    }

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @Composable
    fun PlantTrackApp() {
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()

        PlanTrackAndroidTheme {
            Surface(color = MaterialTheme.colors.background) {
                Scaffold(
                    topBar = {/* TODO */ },
                    drawerContent = {/* TODO */ },
                    bottomBar = {/* TODO */ },
                    floatingActionButton = {
                        when (backstackEntry.value?.destination?.route) {
                            "manageplants" -> {
                                AddPlantButton()
                            }
                        }
                    },
                    snackbarHost = {/* TODO */ },
                ) {
                    PlantNavHost(navController)
                }
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
        }
    }
}