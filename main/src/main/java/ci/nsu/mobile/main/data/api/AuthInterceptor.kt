package ci.nsu.mobile.main.data.api

import ci.nsu.mobile.main.data.storage.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")

        TokenManager.token?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}