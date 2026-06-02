package ci.nsu.mobile.main.viewmodel

import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.UserDto

data class AuthUiState(
    val loading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val users: List<UserDto> = emptyList(),
    val groups: List<GroupDto> = emptyList(),
    val error: String? = null
)