package ci.nsu.mobile.main.viewmodel

import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.UserDto

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: UserDto? = null,
    val users: List<UserDto> = emptyList(),
    val groups: List<GroupDto> = emptyList()
)