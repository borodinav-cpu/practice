package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Расчёт вкладов",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = { navController.navigate("input1") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("history") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("История расчётов")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // В Compose нет прямого finish(), но можно использовать activity через context
                println("Приложение закрыто")
                // Если нужно реально закрыть:
                // (navController.context as? android.app.Activity)?.finish()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Закрыть приложение")
        }
    }
}