package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InputScreen1(navController: NavController) {
    var initialAmount by remember { mutableStateOf("") }
    var termMonths by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Этап 1: Основные параметры",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = initialAmount,
            onValueChange = { initialAmount = it },
            label = { Text("Стартовый взнос") },
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = termMonths,
            onValueChange = { termMonths = it },
            label = { Text("Срок вклада (месяцев)") },
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.popBackStack("main", inclusive = false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (initialAmount.isNotEmpty() && termMonths.isNotEmpty()) {
                    navController.navigate("input2/$initialAmount/$termMonths")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Далее")
        }
    }
}