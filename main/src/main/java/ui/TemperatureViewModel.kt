package ci.nsu.mobile.main.lab4.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class ConversionDirection {
    CELSIUS_TO_FAHRENHEIT,
    FAHRENHEIT_TO_CELSIUS
}

data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = "",
    val direction: ConversionDirection = ConversionDirection.CELSIUS_TO_FAHRENHEIT
)

fun TemperatureUiState.isCelsiusValid(): Boolean = celsius.toDoubleOrNull() != null
fun TemperatureUiState.isFahrenheitValid(): Boolean = fahrenheit.toDoubleOrNull() != null

class TemperatureViewModel : ViewModel() {

    var uiState by mutableStateOf(TemperatureUiState())
        private set

    fun onCelsiusFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            uiState = uiState.copy(
                direction = ConversionDirection.CELSIUS_TO_FAHRENHEIT
            )
        }
    }

    fun onFahrenheitFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            uiState = uiState.copy(
                direction = ConversionDirection.FAHRENHEIT_TO_CELSIUS
            )
        }
    }

    fun onCelsiusChanged(newValue: String) {
        val fahrenheit = if (newValue.isNotBlank()) {
            val celsius = newValue.toDoubleOrNull()
            if (celsius != null) {
                String.format("%.2f", celsius * 9 / 5 + 32)
            } else ""
        } else ""

        uiState = uiState.copy(
            celsius = newValue,
            fahrenheit = fahrenheit
        )
    }

    fun onFahrenheitChanged(newValue: String) {
        val celsius = if (newValue.isNotBlank()) {
            val fahrenheit = newValue.toDoubleOrNull()
            if (fahrenheit != null) {
                String.format("%.2f", (fahrenheit - 32) * 5 / 9)
            } else ""
        } else ""

        uiState = uiState.copy(
            celsius = celsius,
            fahrenheit = newValue
        )
    }

    fun clearAllFields() {
        uiState = TemperatureUiState()
    }
}