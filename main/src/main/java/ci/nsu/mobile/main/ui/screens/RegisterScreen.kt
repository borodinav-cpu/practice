package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.PersonDto
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.viewmodel.AuthUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: AuthUiState,
    onRegister: (RegisterRequest) -> Unit,
    onBackClick: () -> Unit
) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var middleName by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Регистрация")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Фамилия") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = middleName,
            onValueChange = { middleName = it },
            label = { Text("Отчество") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Дата рождения, например 2004-05-12") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Пол") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedGroup?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Группа") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                state.groups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.name) },
                        onClick = {
                            selectedGroup = group
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Телефон") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val group = selectedGroup ?: return@Button

                val person = PersonDto(
                    firstName = firstName,
                    lastName = lastName,
                    middleName = middleName,
                    birthDate = birthDate,
                    gender = gender,
                    groupId = group.id
                )

                val request = RegisterRequest(
                    login = login,
                    password = password,
                    email = email,
                    phoneNumber = phoneNumber,
                    roleId = 1,
                    authAllowed = true,
                    person = person
                )

                onRegister(request)
            },
            enabled = !state.loading && selectedGroup != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
        }

        TextButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
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