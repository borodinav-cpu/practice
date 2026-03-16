package ci.nsu.mobile.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ci.nsu.mobile.main.Screen
import ci.nsu.mobile.main.ui.HomeScreen
import ci.nsu.mobile.main.ui.ProfileScreen
import ci.nsu.mobile.main.ui.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}