package ci.nsu.mobile.main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.LoginScreen
import ci.nsu.mobile.main.ui.screens.RegisterScreen
import ci.nsu.mobile.main.ui.screens.UserListScreen
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState = authViewModel.state.value

    NavHost(
        navController = navController,
        startDestination = if (authState.isLoggedIn) "users" else "login"
    ) {
        composable(route = "login") {
            LoginScreen(
                onLoginClick = { login, password ->
                    authViewModel.login(login, password)
                },
                onRegisterClick = {
                    navController.navigate(route = "register")
                },
                state = authState,
                onClearError = { authViewModel.clearError() }
            )
        }

        composable(route = "register") {
            RegisterScreen(
                state = authState,
                onRegister = { registerRequest ->
                    authViewModel.register(
                        registerRequest = registerRequest,
                        onSuccess = {
                            navController.popBackStack()
                            navController.navigate("login")
                        }
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = "users") {
            UserListScreen(
                users = authState.users,
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                state = authState
            )
        }
    }
}