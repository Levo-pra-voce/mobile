package com.levopravoce.mobile.features.auth.data

import com.levopravoce.mobile.features.auth.data.dto.JwtResponseDTO
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthRepository {
    @GET("/api/user/me")
    suspend fun me(): Response<UserDTO>

    @POST("/api/auth/login")
    @Headers("isAuthorized: false")
    suspend fun login(
        @Body user: UserDTO
    ): JwtResponseDTO
}