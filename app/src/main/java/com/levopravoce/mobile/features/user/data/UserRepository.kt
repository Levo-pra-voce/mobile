package com.levopravoce.mobile.features.user.data

import com.levopravoce.mobile.features.auth.data.dto.JwtResponseDTO
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface UserRepository {

    @GET("/api/auth/register/{userType}")
    suspend fun register(
        @Path("userType") userType: String,
        @Body user: UserDTO
    ): JwtResponseDTO
}