package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.LoginScreen
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.screens.RegisterScreen
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.viewmodel.AuthUiState

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (authState.isLoggedIn) "users" else "login"
    ) {
        composable(route = "login") {
            LoginScreen(
                onLoginClick = { login, password ->
                    authViewModel.login(login, password) {
                        navController.navigate("users") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                },
                state = authState,
                onClearError = { authViewModel.clearError() }
            )
        }

        composable("register") {
            RegisterScreen(
                state = authState,
                onRegister = { registerRequest ->
                    authViewModel.register(registerRequest) {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onLoadGroups = { authViewModel.loadGroups() }  // <-- ДОБАВЬТЕ
            )
        }

        composable(route = "users") {
            MainScreen(
                state = authState,
                onLogout = {
                    authViewModel.logout {
                        navController.navigate("login") {
                            popUpTo("users") { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}