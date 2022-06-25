package com.plantfam.plantfam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plantfam.plantfam.ui.components.ui.theme.PlantFamTheme
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
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlantFamTheme {
                PlantFamApp()
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun PlantFamApp() {
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()

        PlantNavHost(navController, scaffoldState)
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun PlantNavHost(navController: NavHostController, scaffoldState: ScaffoldState) {
        NavHost(
            navController = navController,
            startDestination = "login",
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
        val scope = rememberCoroutineScope()
        LoginScreen(navController, scaffoldState, scope, loginViewModel)
    }

    @Composable
    fun EmailConfirmationDestination(
        email: String,
        navController: NavHostController,
        scaffoldState: ScaffoldState
    ) {
        val emailConfirmationViewModel: EmailConfirmationViewModel = hiltViewModel()
        val scope = rememberCoroutineScope()
        EmailConfirmationScreen(
            email,
            navController,
            scaffoldState,
            scope,
            emailConfirmationViewModel
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
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