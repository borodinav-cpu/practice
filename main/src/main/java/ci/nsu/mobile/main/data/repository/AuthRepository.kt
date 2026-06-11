package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.api.NetworkModule
import ci.nsu.mobile.main.data.model.*
import ci.nsu.mobile.main.data.local.TokenManager
import android.util.Log

class AuthRepository {

    private val api = NetworkModule.api

    suspend fun login(
        login: String,
        password: String
    ): Result<UserDto> {
        return try {
            Log.d("AuthRepository", "Login request: login=$login")

            val authResponse = api.login(
                LoginRequest(
                    login = login,
                    password = password
                )
            )

            if (authResponse.isSuccessful) {
                val token = authResponse.body()?.token

                if (token != null) {
                    TokenManager.token = token
                    Log.d("AuthRepository", "Token saved successfully")

                    val usersResponse = api.getUsers()

                    if (usersResponse.isSuccessful) {
                        val users = usersResponse.body() ?: emptyList()
                        val user = users.find { it.login == login }

                        if (user != null) {
                            Log.d("AuthRepository", "Login successful, user: ${user.login}")
                            Result.success(user)
                        } else {
                            Log.e("AuthRepository", "User not found with login: $login")
                            Result.failure(Exception("Пользователь не найден"))
                        }
                    } else {
                        Log.e("AuthRepository", "Failed to get users, code: ${usersResponse.code()}")
                        Result.failure(Exception("Не удалось получить данные пользователя"))
                    }
                } else {
                    Log.e("AuthRepository", "Token is null")
                    Result.failure(Exception("Токен не получен"))
                }
            } else {
                val errorBody = authResponse.errorBody()?.string()
                Log.e("AuthRepository", "Login failed. Code: ${authResponse.code()}")
                Result.failure(Exception("Ошибка входа: ${authResponse.code()}"))
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
            Log.d("AuthRepository", "Register request: ${request.login}")
            val response = api.register(request)

            if (response.isSuccessful) {
                Log.d("AuthRepository", "Registration successful")
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Registration failed. Code: ${response.code()}")
                Result.failure(Exception("Ошибка регистрации: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Registration exception", e)
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            Log.d("AuthRepository", "Getting users...")
            val response = api.getUsers()

            if (response.isSuccessful) {
                val users = response.body() ?: emptyList()
                Log.d("AuthRepository", "Users loaded: ${users.size}")
                Result.success(users)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Get users failed: ${response.code()}")
                Result.failure(Exception("Ошибка загрузки пользователей: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Get users exception", e)
            Result.failure(e)
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            Log.d("AuthRepository", "Getting groups...")
            val response = api.getGroups()

            if (response.isSuccessful) {
                val groups = response.body() ?: emptyList()
                Log.d("AuthRepository", "Groups loaded: ${groups.size}")
                Result.success(groups)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Get groups failed: ${response.code()}")
                Result.failure(Exception("Ошибка загрузки групп: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Get groups exception", e)
            Result.failure(e)
        }
    }
}