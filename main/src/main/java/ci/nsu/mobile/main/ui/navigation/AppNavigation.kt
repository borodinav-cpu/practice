package ci.nsu.mobile.main.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.LoginScreen
import ci.nsu.mobile.main.ui.screens.MainScreen
import ci.nsu.mobile.main.ui.screens.RegisterScreen
import ci.nsu.mobile.main.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    viewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val state = viewModel.state

    NavHost(
        navController = navController,
        startDestination = if (state.isLoggedIn) "main" else "login"
    ) {
        composable("login") {
            LoginScreen(
                state = state,
                onLogin = { login, password ->
                    viewModel.login(login, password)
                    navController.navigate("main") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            LaunchedEffect(Unit) {
                viewModel.loadGroups()
            }

            RegisterScreen(
                state = state,
                onRegister = { request ->
                    viewModel.register(request) {
                        navController.popBackStack()
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("main") {
            LaunchedEffect(Unit) {
                viewModel.loadUsers()
            }

            MainScreen(
                state = state,
                onLogout = {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo("main") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}