package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.data.CalculationRepository
import ci.nsu.mobile.main.data.room.AppDatabase
import ci.nsu.mobile.main.ui.screens.*
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import ci.nsu.mobile.main.viewmodel.ViewModelFactory
import androidx.compose.ui.Modifier


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация БД и Repository
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = CalculationRepository(database.calculationDao())
        val factory = ViewModelFactory(repository)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                // Передаём factory в viewModel
                val vm: DepositViewModel = viewModel(factory = factory)
                AppNavigation(navController = rememberNavController(), viewModel = vm)
            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: DepositViewModel
) {
    NavHost(navController = navController, startDestination = "main") {

        // ✅ ГЛАВНЫЙ ЭКРАН — ОБЯЗАТЕЛЬНО ДОБАВИТЬ!
        composable("main") {
            MainScreen(navController)
        }

        // Этап 1: ввод основных параметров
        composable("input1") {
            InputScreen1(navController)
        }

        // Этап 2: дополнительные параметры (с аргументами)
        composable(
            route = "input2/{amount}/{term}",
            arguments = listOf(
                navArgument("amount") { type = NavType.StringType },
                navArgument("term") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount") ?: ""
            val term = backStackEntry.arguments?.getString("term") ?: ""
            InputScreen2(navController, amount, term)
        }

        // Экран результата (с аргументами)
        composable(
            route = "result/{amount}/{term}/{rate}/{contribution}",
            arguments = listOf(
                navArgument("amount") { type = NavType.StringType },
                navArgument("term") { type = NavType.StringType },
                navArgument("rate") { type = NavType.StringType },
                navArgument("contribution") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount") ?: ""
            val term = backStackEntry.arguments?.getString("term") ?: ""
            val rate = backStackEntry.arguments?.getString("rate") ?: ""
            val contribution = backStackEntry.arguments?.getString("contribution") ?: ""

            ResultScreen(
                navController = navController,
                viewModel = viewModel,
                amountStr = amount,
                termStr = term,
                rateStr = rate,
                contributionStr = contribution
            )
        }

        // История расчётов
        composable("history") {
            HistoryScreen(navController, viewModel)
        }
    }
}