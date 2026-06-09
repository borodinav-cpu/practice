package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.storage.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _state = MutableStateFlow(
        AuthUiState(
            isLoggedIn = TokenManager.token != null
        )
    )
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun login(
        login: String,
        password: String
    ) {
        if (login.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(
                error = "Введите логин и пароль"
            )
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            repository.login(login, password)
                .onSuccess {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        error = null
                    )
                    loadUsers()
                    loadGroups()
                }
                .onFailure { throwable ->
                    Log.e("AuthViewModel", "Login error", throwable)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Ошибка входа"
                    )
                }
        }
    }

    fun register(
        registerRequest: RegisterRequest,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            Log.d("AuthViewModel", "Register request: $registerRequest")

            repository.register(registerRequest)
                .onSuccess {
                    Log.d("AuthViewModel", "Registration success")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = null
                    )
                    onSuccess()
                }
                .onFailure { throwable ->
                    Log.e("AuthViewModel", "Registration error", throwable)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Ошибка регистрации"
                    )
                }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            repository.getUsers()
                .onSuccess { users ->
                    _state.value = _state.value.copy(
                        users = users,
                        error = null
                    )
                }
                .onFailure { throwable ->
                    _state.value = _state.value.copy(
                        error = throwable.message ?: "Ошибка загрузки пользователей"
                    )
                }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            repository.getGroups()
                .onSuccess { groups ->
                    _state.value = _state.value.copy(
                        groups = groups,
                        error = null
                    )
                }
                .onFailure { throwable ->
                    _state.value = _state.value.copy(
                        error = throwable.message ?: "Ошибка загрузки групп"
                    )
                }
        }
    }

    fun logout() {
        TokenManager.clear()
        _state.value = AuthUiState(
            isLoggedIn = false
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(
            error = null
        )
    }
}