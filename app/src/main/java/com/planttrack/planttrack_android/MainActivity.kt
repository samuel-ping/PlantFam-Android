package com.planttrack.planttrack_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.planttrack.planttrack_android.ui.components.BottomAppBarContent
import com.planttrack.planttrack_android.ui.components.SavePlantButton
import com.planttrack.planttrack_android.ui.theme.PlantTrackAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // TODO: Hardcoded data, delete when possible
//    private val plantList = listOf(
//        Plant(
//            "Shrimp",
//            "Echeveria fleur blanc",
//            "Echeveria fleur blanc",
//            LocalDate.of(2019, 7, 27),
//            "Home Depot",
//            false,
//            null,
//            null
//        ),
//        Plant(
//            "Butler",
//            "Echeveria cubic frost",
//            "Echeveria cubic frost",
//            LocalDate.of(2020, 10, 28),
//            "Gift",
//            false,
//            null,
//            null
//        ),
//        Plant(
//            "Three Musketeers",
//            "Burros tails",
//            "Sedum morganianum",
//            LocalDate.of(2021, 1, 24),
//            "Gift",
//            false,
//            null,
//            null
//        )
//    )

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
        val scaffoldState = rememberScaffoldState()

        var canPop by remember { mutableStateOf(false) }

        navController.addOnDestinationChangedListener { controller, _, _ ->
            canPop = controller.previousBackStackEntry != null
        }

        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            when (backstackEntry.value?.destination?.route) {
                                "login" -> Text("Log In")
                                "manageplants" -> Text("PlantTrack")
                                "addplant" -> Text("Add Plant")
                                "settings" -> Text("Settings")
                            }
                        },
                        navigationIcon =
                        if (canPop) {
                            {
                                IconButton(
                                    onClick = { navController.navigateUp() }
                                ) {
                                    when (backstackEntry.value?.destination?.route) {
                                        "addplant" -> Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Close add plant screen"
                                        )
//                                        else -> Icon(
//                                            imageVector = Icons.Filled.ArrowBack,
//                                            contentDescription = "Go back"
//                                        )
                                    }
                                }
                            }
                        } else null
                    )
                },
//                    drawerContent = {/* TODO */ },
                bottomBar = { BottomAppBarContent(navController) },
                floatingActionButton = {
                    when (backstackEntry.value?.destination?.route) {
                        "manageplants" -> AddPlantButton(navController)
                        "addplant" -> SavePlantButton(navController)
                    }
                },
                scaffoldState = scaffoldState,
            ) {
                PlantNavHost(navController, scaffoldState)
            }
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
                LoginDestination(navController = navController, scaffoldState = scaffoldState)
            }
            composable("manageplants") {
                ManagePlantsDestination()
            }
            composable("addplant") {
                AddPlantScreen()
            }
            composable("settings") {
                SettingsScreen()
            }
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
    fun ManagePlantsDestination() {
        val managePlantViewModel: ManagePlantsViewModel = hiltViewModel()
        ManagePlantsScreen(plants = mutableListOf())
    }

    @Composable
    fun AddPlantDestination(navController: NavHostController) {
        val viewModel: AddPlantViewModel = hiltViewModel()
    }
}