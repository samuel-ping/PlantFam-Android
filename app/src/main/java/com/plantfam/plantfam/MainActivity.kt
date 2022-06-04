package com.plantfam.plantfam

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
import com.plantfam.plantfam.ui.screens.addplantscreen.AddPlantScreen
import com.plantfam.plantfam.ui.screens.addplantscreen.AddPlantViewModel
import com.plantfam.plantfam.ui.screens.emailconfirmationscreen.EmailConfirmationScreen
import com.plantfam.plantfam.ui.screens.emailconfirmationscreen.EmailConfirmationViewModel
import com.plantfam.plantfam.ui.screens.loginscreen.LoginScreen
import com.plantfam.plantfam.ui.screens.loginscreen.LoginViewModel
import com.plantfam.plantfam.ui.screens.manageplantsscreen.ManagePlantsScreen
import com.plantfam.plantfam.ui.screens.manageplantsscreen.ManagePlantsViewModel
import com.plantfam.plantfam.ui.screens.plantdetailsscreen.PlantDetailsScreen
import com.plantfam.plantfam.ui.screens.plantdetailsscreen.PlantDetailsViewModel
import com.plantfam.plantfam.ui.screens.settingsscreen.SettingsScreen
import com.plantfam.plantfam.ui.screens.settingsscreen.SettingsViewModel
import com.plantfam.plantfam.ui.theme.PlantFamAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlantFamAndroidTheme {
                PlantFamApp()
            }
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @Composable
    fun PlantFamApp() {
        val navController = rememberAnimatedNavController()
        val scaffoldState = rememberScaffoldState()

        PlantNavHost(navController, scaffoldState)
    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @Composable
    fun PlantNavHost(navController: NavHostController, scaffoldState: ScaffoldState) {
        AnimatedNavHost(
            navController = navController,
            startDestination = "login",
            // Disable animations between screen transitions.
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            composable("login") { LoginDestination(navController, scaffoldState) }
            composable("emailconfirmation/{email}") { backStackEntry ->
                backStackEntry.arguments?.getString("email")?.let {
                    EmailConfirmationDestination(it, navController, scaffoldState)
                }
            }
            composable("manageplants") { ManagePlantsDestination(navController) }
            composable("addplant") { AddPlantDestination(navController) }
            composable("details/{plantId}") { backStackEntry ->
                backStackEntry.arguments?.getString("plantId")
                    ?.let { PlantDetailsDestination(it, navController) }
            }
            composable("settings") { SettingsDestination(navController) }
        }
    }

    @Composable
    fun LoginDestination(navController: NavHostController, scaffoldState: ScaffoldState) {
        val loginViewModel: LoginViewModel = hiltViewModel()
        var scope = rememberCoroutineScope()
        LoginScreen(navController, scaffoldState, scope, loginViewModel)
    }

    @Composable
    fun EmailConfirmationDestination(
        email: String,
        navController: NavHostController,
        scaffoldState: ScaffoldState
    ) {
        val emailConfirmationViewModel: EmailConfirmationViewModel = hiltViewModel()
        var scope = rememberCoroutineScope()
        EmailConfirmationScreen(
            email,
            navController,
            scaffoldState,
            scope,
            emailConfirmationViewModel
        )
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
    fun PlantDetailsDestination(plantId: String, navController: NavHostController) {
        val plantDetailsViewModel: PlantDetailsViewModel = hiltViewModel()
        PlantDetailsScreen(plantId, navController, plantDetailsViewModel)
    }

    @Composable
    fun SettingsDestination(navController: NavHostController) {
        val settingsViewModel: SettingsViewModel = hiltViewModel()
        SettingsScreen(navController, settingsViewModel)
    }
}