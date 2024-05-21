package com.levopravoce.mobile.features.user.data

import com.levopravoce.mobile.features.auth.data.dto.JwtResponseDTO
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.forgotPassword.data.PasswordCodeDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.OPTIONS
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserRepository {

    @POST("/api/auth/register/{userType}")
    @Headers("isAuthorized: false")
    suspend fun register(
        @Path("userType") userType: String,
        @Body user: UserDTO
    ): Response<JwtResponseDTO>

    @PUT("/api/user")
    suspend fun update(
        @Body user: UserDTO
    ): Response<Unit>
    @DELETE("/api/user")
    suspend fun delete(): Response<Unit>

    @POST("/api/user/forgot-password/{email}")
    @Headers("isAuthorized: false")
    suspend fun forgotPassword(
        @Path("email") email: String
    ): Response<Unit>

    @POST("/forgot-password/exist-code")
    @Headers("isAuthorized: false")
    suspend fun existCode(
        @Body passwordCodeDTO: PasswordCodeDTO
    ): Response<Unit>

    @POST("/api/user/forgot-password/change-password")
    @Headers("isAuthorized: false")
    suspend fun changePassword(
        @Body passwordCodeDTO: PasswordCodeDTO
    ): Response<Unit>


}