package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.api.NetworkModule
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.storage.TokenManager
import android.util.Log

class AuthRepository {

    private val api = NetworkModule.api

    suspend fun login(
        login: String,
        password: String
    ): Result<UserDto> {
        return try {
            Log.d("AuthRepository", "Login request: login=$login")

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
                    Log.d("AuthRepository", "Token saved successfully")
                }

                Log.d("AuthRepository", "Login successful")
                Result.success(user)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Login failed. Code: ${response.code()}")
                Log.e("AuthRepository", "Error body: $errorBody")

                Result.failure(
                    Exception("Ошибка входа: ${response.code()}. ${errorBody ?: "Unknown error"}")
                )
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login exception", e)
            Result.failure(e)
        }
    }

    suspend fun register(
        request: RegisterRequest
    ): Result<Unit> {
        return try {
            Log.d("AuthRepository", "========== REGISTER REQUEST ==========")
            Log.d("AuthRepository", "Login: ${request.login}")
            Log.d("AuthRepository", "Email: ${request.email}")
            Log.d("AuthRepository", "PhoneNumber: ${request.phoneNumber}")
            Log.d("AuthRepository", "RoleId: ${request.roleId}")
            Log.d("AuthRepository", "AuthAllowed: ${request.authAllowed}")
            Log.d("AuthRepository", "Person: ${request.person}")
            Log.d("AuthRepository", "Full request: $request")

            val response = api.register(request)

            if (response.isSuccessful) {
                Log.d("AuthRepository", "Registration successful")
                Log.d("AuthRepository", "Response body: ${response.body()}")
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Registration failed. Code: ${response.code()}")
                Log.e("AuthRepository", "Error body: $errorBody")
                Log.e("AuthRepository", "Message: ${response.message()}")

                Result.failure(
                    Exception("Ошибка регистрации: ${response.code()}. $errorBody")
                )
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Registration exception", e)
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val response = api.getUsers()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Get users failed: ${response.code()}, $errorBody")
                Result.failure(
                    Exception("Ошибка загрузки пользователей: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Get users exception", e)
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            val response = api.getGroups()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Get groups failed: ${response.code()}, $errorBody")
                Result.failure(
                    Exception("Ошибка загрузки групп: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Get groups exception", e)
            Result.failure(e)
        }
    }
}