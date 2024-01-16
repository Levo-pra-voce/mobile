package com.levopravoce.mobile.features.auth.domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authStore: AuthStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val token: String? = runBlocking {
            authStore.getAccessToken.first()
        }

        val originalRequest = chain.request()

        val shouldAddAuthHeaders = originalRequest.header("isAuthorized")?.toBoolean() ?: true

        if (token.isNullOrEmpty() || !shouldAddAuthHeaders) {
            return chain.proceed(chain.request())
        }


        val requestBuilder = originalRequest.newBuilder()

        requestBuilder.addHeader("Authorization", "Bearer $token")

        return chain.proceed(requestBuilder.build())
    }
}