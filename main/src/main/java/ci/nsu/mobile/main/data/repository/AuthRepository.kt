package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.api.NetworkModule
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.storage.TokenManager

class AuthRepository {

    private val api = NetworkModule.api

    suspend fun login(
        login: String,
        password: String
    ): Result<UserDto> {
        return try {
            val response = api.login(
                LoginRequest(
                    login = login,
                    password = password
                )
            )

            val user = response.body()

            if (response.isSuccessful && user != null) {
                user.token?.let { token ->
                    TokenManager.token = token
                }

                Result.success(user)
            } else {
                Result.failure(
                    Exception("Ошибка входа: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        request: RegisterRequest
    ): Result<Unit> {
        return try {
            val response = api.register(request)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception("Ошибка регистрации: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = api.getUsers()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(
                    Exception("Ошибка загрузки пользователей: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = api.getGroups()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(
                    Exception("Ошибка загрузки групп: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}