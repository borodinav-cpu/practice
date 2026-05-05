package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: DepositViewModel,
    amountStr: String,
    termStr: String,
    rateStr: String,
    contributionStr: String
) {
    // Преобразуем строки в числа
    val initialAmount = amountStr.toDoubleOrNull() ?: 0.0
    val termMonths = termStr.toIntOrNull() ?: 0
    val interestRate = rateStr.toDoubleOrNull() ?: 0.0
    val monthlyContribution = contributionStr.toDoubleOrNull() ?: 0.0

    // Рассчитываем итоги
    val totalAmount = calculateTotal(initialAmount, termMonths, interestRate, monthlyContribution)
    val totalInterest = totalAmount - initialAmount - (monthlyContribution * termMonths)

    // Форматируем числа под рубли
    val format = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Результат расчёта", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Стартовый взнос: ${format.format(initialAmount)}")
                Text("Срок вклада: $termMonths мес.")
                Text("Процентная ставка: $interestRate%")
                if (monthlyContribution > 0) {
                    Text("Ежемесячное пополнение: ${format.format(monthlyContribution)}")
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Итоговая сумма: ${format.format(totalAmount)}", style = MaterialTheme.typography.titleMedium)
                Text("Начисленные проценты: ${format.format(totalInterest)}")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.saveCalculation(
                    initialAmount = initialAmount,
                    termMonths = termMonths,
                    interestRate = interestRate,
                    monthlyContribution = monthlyContribution,
                    totalAmount = totalAmount,
                    totalInterest = totalInterest
                )
                navController.popBackStack("main", inclusive = false)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.popBackStack("main", inclusive = false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}

// 🔢 Функция расчёта итоговой суммы с ежемесячным пополнением
fun calculateTotal(
    initial: Double,
    months: Int,
    annualRate: Double,
    monthlyAdd: Double
): Double {
    var total = initial
    val monthlyRate = annualRate / 100 / 12

    for (i in 1..months) {
        total += total * monthlyRate + monthlyAdd
    }

    return total
}