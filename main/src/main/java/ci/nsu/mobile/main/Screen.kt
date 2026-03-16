package ci.nsu.mobile.main

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Profile : Screen("profile")
}