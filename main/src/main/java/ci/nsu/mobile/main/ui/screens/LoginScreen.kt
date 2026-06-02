package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.viewmodel.AuthUiState

@Composable
fun LoginScreen(
    state: AuthUiState,
    onLogin: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Вход")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onLogin(login, password) },
            enabled = !state.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }

        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Нет аккаунта? Зарегистрироваться")
        }

        if (state.loading) {
            CircularProgressIndicator()
        }

        state.error?.let {
            Text(
                text = it,
                color = Color.Red
            )
        }
    }
}