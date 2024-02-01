package com.levopravoce.mobile.features.user.data

import com.levopravoce.mobile.features.auth.data.dto.JwtResponseDTO
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface UserRepository {

    @POST("/api/auth/register/{userType}")
    suspend fun register(
        @Path("userType") userType: String,
        @Body user: UserDTO
    ): JwtResponseDTO
}