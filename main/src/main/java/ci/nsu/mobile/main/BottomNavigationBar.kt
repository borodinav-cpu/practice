package ci.nsu.mobile.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Главная") },
            selected = currentRoute == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Настройки") },
            selected = currentRoute == Screen.Settings.route,
            onClick = {
                navController.navigate(Screen.Settings.route)
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Профиль") },
            selected = currentRoute == Screen.Profile.route,
            onClick = {
                navController.navigate(Screen.Profile.route)
            }
        )
    }
}