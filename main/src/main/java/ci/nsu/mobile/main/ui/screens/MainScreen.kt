package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.viewmodel.AuthUiState

@Composable
fun MainScreen(
    state: AuthUiState,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выйти")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Список пользователей")

        Spacer(modifier = Modifier.height(8.dp))

        if (state.loading) {
            CircularProgressIndicator()
        }

        LazyColumn {
            items(state.users) { user ->
                Text(
                    text = "Логин: ${user.login}, Email: ${user.email ?: "-"}",
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }

        state.error?.let {
            Text(
                text = it,
                color = Color.Red
            )
        }
    }
}