package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InputScreen2(
    navController: NavController,
    amountStr: String,   // ← получаем из аргументов
    termStr: String      // ← получаем из аргументов
) {
    var monthlyContribution by remember { mutableStateOf("") }
    val termMonths = termStr.toIntOrNull() ?: 0

    // Определяем доступные ставки и выбранную
    val availableRates = when {
        termMonths <= 0 -> emptyList<Double>()
        termMonths < 6 -> listOf(15.0)
        termMonths < 12 -> listOf(10.0)
        else -> listOf(5.0)
    }

    val selectedRate = availableRates.firstOrNull() ?: 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Этап 2: Дополнительные параметры", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        if (termMonths <= 0) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("⚠️ Срок вклада не указан или некорректен!", color = MaterialTheme.colorScheme.onErrorContainer)
                    Text("Вернитесь назад и укажите срок ≥ 1 месяца.", style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Text("Процентная ставка (автоматически): ${selectedRate}%", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = monthlyContribution,
            onValueChange = { monthlyContribution = it },
            label = { Text("Ежемесячное пополнение (необяз.)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = termMonths > 0
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate("result/$amountStr/$termStr/$selectedRate/$monthlyContribution")
            },
            enabled = termMonths > 0,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }
    }
}