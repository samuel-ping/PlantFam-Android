package com.planttrack.planttrack_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.planttrack.planttrack_android.addplantscreen.AddPlantScreen
import com.planttrack.planttrack_android.addplantscreen.AddPlantViewModel
import com.planttrack.planttrack_android.loginscreen.LoginScreen
import com.planttrack.planttrack_android.loginscreen.LoginViewModel
import com.planttrack.planttrack_android.manageplantsscreen.ManagePlantsScreen
import com.planttrack.planttrack_android.manageplantsscreen.ManagePlantsViewModel
import com.planttrack.planttrack_android.settingsscreen.SettingsScreen
import com.planttrack.planttrack_android.settingsscreen.SettingsViewModel
import com.planttrack.planttrack_android.ui.theme.PlantTrackAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
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

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @Composable
    fun PlantTrackApp() {
        val navController = rememberAnimatedNavController()
        val scaffoldState = rememberScaffoldState()

        Surface(color = MaterialTheme.colors.background) {
            PlantNavHost(navController, scaffoldState)
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @Composable
    fun PlantNavHost(navController: NavHostController, scaffoldState: ScaffoldState) {
        AnimatedNavHost(
            navController = navController,
            startDestination = if (plantTrackApp.currentUser() == null) "login" else "manageplants",
            // Disable animations between screen transitions.
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
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