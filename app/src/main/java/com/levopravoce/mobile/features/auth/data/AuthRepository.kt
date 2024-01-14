package com.levopravoce.mobile.features.auth.data

import com.levopravoce.mobile.features.auth.data.dto.JwtResponseDTO
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthRepository {
    @GET("/api/user/me")
    suspend fun me(): UserDTO

    @POST("/api/auth/login")
    suspend fun login(email: String, password: String): JwtResponseDTO
}