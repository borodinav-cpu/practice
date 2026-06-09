package ci.nsu.mobile.main.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: Int? = null,
    val login: String,
    val email: String? = null,
    val phoneNumber: String? = null,
    val token: String? = null

)