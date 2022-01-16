package com.planttrack.planttrack_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.planttrack.planttrack_android.ui.components.AddPlantButton
import com.planttrack.planttrack_android.addplantscreen.AddPlantScreen
import com.planttrack.planttrack_android.addplantscreen.AddPlantViewModel
import com.planttrack.planttrack_android.loginscreen.LoginScreen
import com.planttrack.planttrack_android.loginscreen.LoginViewModel
import com.planttrack.planttrack_android.manageplantsscreen.ManagePlantsScreen
import com.planttrack.planttrack_android.manageplantsscreen.ManagePlantsViewModel
import com.planttrack.planttrack_android.settingsscreen.SettingsScreen
import com.planttrack.planttrack_android.settingsscreen.SettingsViewModel
import com.planttrack.planttrack_android.ui.components.BottomAppBarContent
import com.planttrack.planttrack_android.ui.components.SavePlantButton
import com.planttrack.planttrack_android.ui.theme.PlantTrackAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
        val scaffoldState = rememberScaffoldState()

        Surface(color = MaterialTheme.colors.background) {
            PlantNavHost(navController, scaffoldState)
        }
    }

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @Composable
    fun PlantNavHost(navController: NavHostController, scaffoldState: ScaffoldState) {
        NavHost(
            navController = navController,
            startDestination = if (plantTrackApp.currentUser() == null) "login" else "manageplants"
        ) {
            composable("login") {
                LoginDestination(
                    navController = navController,
                    scaffoldState = scaffoldState
                )
            }
            composable("manageplants") { ManagePlantsDestination(navController) }
            composable("addplant") { AddPlantDestination(navController) }
            composable("settings") { SettingsDestination(navController) }
        }
    }

    @Composable
    fun LoginDestination(navController: NavHostController, scaffoldState: ScaffoldState) {
        val loginViewModel: LoginViewModel = hiltViewModel()
        var scope = rememberCoroutineScope()
        LoginScreen(navController, scaffoldState, scope, loginViewModel)
    }

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @Composable
    fun ManagePlantsDestination(navController: NavHostController) {
        val managePlantsViewModel: ManagePlantsViewModel = hiltViewModel()
        ManagePlantsScreen(navController, managePlantsViewModel)
    }

    @Composable
    fun AddPlantDestination(navController: NavHostController) {
        val addPlantViewModel: AddPlantViewModel = hiltViewModel()
        AddPlantScreen(navController, addPlantViewModel)
    }

    @Composable
    fun SettingsDestination(navController: NavHostController) {
        val settingsViewModel: SettingsViewModel = hiltViewModel()
        SettingsScreen(navController, settingsViewModel)
    }
}