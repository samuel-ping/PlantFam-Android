package com.plantfam.plantfam.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomAppBarContent(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        modifier = Modifier
            .height(65.dp)
            .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
        cutoutShape = CircleShape,
        elevation = 22.dp
    ) {
        BottomNavigation(elevation = 10.dp) {
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = "Go to your plants"
                    )
                },
                label = { Text(text = "My Plants") },
                alwaysShowLabel = false,
                selected = (currentRoute == "manageplants"),
                onClick = {
                    navController.navigate("manageplants") {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                })
            BottomNavigationItem(icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Go to settings"
                )
            },
                label = { Text(text = "Settings") },
                alwaysShowLabel = false,
                selected = (currentRoute == "settings"),
                onClick = {
                    navController.navigate("settings") {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}