package ci.nsu.mobile.main.lab4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.focus.onFocusEvent

@Composable
fun TemperatureScreen(
    viewModel: TemperatureViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    val isCelsiusValid = uiState.isCelsiusValid()
    val isFahrenheitValid = uiState.isFahrenheitValid()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Конвертер температур",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = uiState.celsius,
            onValueChange = { viewModel.onCelsiusChanged(it) },
            label = { Text("Градусы Цельсия (°C)") },
            isError = uiState.celsius.isNotBlank() && !isCelsiusValid,
            supportingText = {
                if (uiState.celsius.isNotBlank() && !isCelsiusValid) {
                    Text("Введите корректное число")
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        viewModel.onCelsiusFocusChanged(true)
                    }
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (uiState.direction == ConversionDirection.CELSIUS_TO_FAHRENHEIT) "▼" else "▲",
            fontSize = 48.sp,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.fahrenheit,
            onValueChange = { viewModel.onFahrenheitChanged(it) },
            label = { Text("Градусы Фаренгейта (°F)") },
            isError = uiState.fahrenheit.isNotBlank() && !isFahrenheitValid,
            supportingText = {
                if (uiState.fahrenheit.isNotBlank() && !isFahrenheitValid) {
                    Text("Введите корректное число")
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        viewModel.onFahrenheitFocusChanged(true)
                    }
                }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.clearAllFields() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(
                text = "Очистить все поля",
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}