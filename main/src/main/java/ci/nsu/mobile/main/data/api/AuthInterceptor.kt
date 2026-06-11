package ci.nsu.mobile.main.data.api

import ci.nsu.mobile.main.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Не добавляем Authorization для запросов регистрации и логина
        val isAuthRequest = originalRequest.url.encodedPath.contains("auth/login") ||
                originalRequest.url.encodedPath.contains("auth/register")

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")

        if (!isAuthRequest) {
            TokenManager.token?.let { token ->
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}