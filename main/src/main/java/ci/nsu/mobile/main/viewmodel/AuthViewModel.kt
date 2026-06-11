package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.local.TokenManager
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

    init {
        if (!_state.value.isLoggedIn) {
            loadGroups()
        } else {
            loadUsers()
        }
    }

    fun login(
        login: String,
        password: String,
        onSuccess: () -> Unit
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

            Log.d("AuthViewModel", "Login attempt: $login")

            repository.login(login, password)
                .onSuccess { user ->
                    Log.d("AuthViewModel", "Login success: ${user.login}")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        user = user,  // <-- ТЕПЕРЬ РАБОТАЕТ, ТАК КАК ПОЛЕ ЕСТЬ
                        error = null
                    )
                    loadUsers()
                    loadGroups()
                    onSuccess()
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

            Log.d("AuthViewModel", "Register request: ${registerRequest.login}")

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
            Log.d("AuthViewModel", "Loading users...")
            repository.getUsers()
                .onSuccess { users ->
                    Log.d("AuthViewModel", "Users loaded: ${users.size}")
                    _state.value = _state.value.copy(
                        users = users,
                        error = null
                    )
                }
                .onFailure { throwable ->
                    Log.e("AuthViewModel", "Load users error", throwable)
                    _state.value = _state.value.copy(
                        error = throwable.message ?: "Ошибка загрузки пользователей"
                    )
                }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            Log.d("AuthViewModel", "Loading groups...")
            repository.getGroups()
                .onSuccess { groups ->
                    Log.d("AuthViewModel", "Groups loaded: ${groups.size}")
                    groups.forEach { group ->
                        Log.d("AuthViewModel", "Group: id=${group.id}, name=${group.name}")
                    }
                    _state.value = _state.value.copy(
                        groups = groups,
                        error = null
                    )
                }
                .onFailure { throwable ->
                    Log.e("AuthViewModel", "Load groups error", throwable)
                    _state.value = _state.value.copy(
                        error = throwable.message ?: "Ошибка загрузки групп"
                    )
                }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        TokenManager.clear()
        _state.value = AuthUiState(
            isLoggedIn = false
        )
        loadGroups()
        onSuccess()
    }

    fun clearError() {
        _state.value = _state.value.copy(
            error = null
        )
    }
}