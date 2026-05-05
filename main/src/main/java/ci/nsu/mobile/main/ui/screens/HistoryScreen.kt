package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.data.room.CalculationEntity
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.text.NumberFormat

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: DepositViewModel
) {
    val calculations by viewModel.allCalculations.collectAsState(initial = emptyList())
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    var selectedCalculation by remember { mutableStateOf<CalculationEntity?>(null) }

    if (selectedCalculation != null) {
        // Экран деталей
        CalculationDetailScreen(
            calculation = selectedCalculation!!,
            onBack = { selectedCalculation = null }
        )
    } else {
        // Список истории
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("История расчётов", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            if (calculations.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("История пуста")
                }
            } else {
                LazyColumn {
                    items(calculations) { calculation ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selectedCalculation = calculation },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Дата: ${dateFormat.format(Date(calculation.timestamp))}")
                                Text("Взнос: ${String.format("%.0f ₽", calculation.initialAmount)}")
                                Text("Итого: ${String.format("%.0f ₽", calculation.totalAmount)}")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack("main", inclusive = false) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("В начало")
            }
        }
    }
}

// Отдельный экран для деталей расчёта
@Composable
fun CalculationDetailScreen(
    calculation: CalculationEntity,
    onBack: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val format = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Детали расчёта", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Дата: ${dateFormat.format(Date(calculation.timestamp))}")
                Text("Стартовый взнос: ${format.format(calculation.initialAmount)}")
                Text("Срок: ${calculation.termMonths} мес.")
                Text("Ставка: ${calculation.interestRate}%")
                if (calculation.monthlyContribution > 0) {
                    Text("Пополнение: ${format.format(calculation.monthlyContribution)}/мес")
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Итого: ${format.format(calculation.totalAmount)}", style = MaterialTheme.typography.titleMedium)
                Text("Проценты: ${format.format(calculation.totalInterest)}")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад к списку")
        }
    }
}