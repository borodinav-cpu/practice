package ci.nsu.mobile.main.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.storage.TokenManager
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    var state by mutableStateOf(
        AuthUiState(
            isLoggedIn = TokenManager.token != null
        )
    )
        private set

    fun login(
        login: String,
        password: String
    ) {
        if (login.isBlank() || password.isBlank()) {
            state = state.copy(
                error = "Введите логин и пароль"
            )
            return
        }

        viewModelScope.launch {
            state = state.copy(
                loading = true,
                error = null
            )

            repository.login(login, password)
                .onSuccess {
                    state = state.copy(
                        loading = false,
                        isLoggedIn = true,
                        error = null
                    )
                    loadUsers()
                }
                .onFailure { throwable ->
                    state = state.copy(
                        loading = false,
                        error = throwable.message ?: "Ошибка входа"
                    )
                }
        }
    }

    fun register(
        request: RegisterRequest,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            state = state.copy(
                loading = true,
                error = null
            )

            repository.register(request)
                .onSuccess {
                    state = state.copy(
                        loading = false,
                        error = null
                    )
                    onSuccess()
                }
                .onFailure { throwable ->
                    state = state.copy(
                        loading = false,
                        error = throwable.message ?: "Ошибка регистрации"
                    )
                }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            state = state.copy(
                loading = true,
                error = null
            )

            repository.getUsers()
                .onSuccess { users ->
                    state = state.copy(
                        loading = false,
                        users = users,
                        error = null
                    )
                }
                .onFailure { throwable ->
                    state = state.copy(
                        loading = false,
                        error = throwable.message ?: "Ошибка загрузки пользователей"
                    )
                }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            repository.getGroups()
                .onSuccess { groups ->
                    state = state.copy(
                        groups = groups,
                        error = null
                    )
                }
                .onFailure { throwable ->
                    state = state.copy(
                        error = throwable.message ?: "Ошибка загрузки групп"
                    )
                }
        }
    }

    fun logout() {
        TokenManager.clear()

        state = AuthUiState(
            isLoggedIn = false
        )
    }

    fun clearError() {
        state = state.copy(
            error = null
        )
    }
}