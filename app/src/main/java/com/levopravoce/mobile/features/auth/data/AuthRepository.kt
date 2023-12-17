package com.levopravoce.mobile.features.auth.data

import retrofit2.http.GET

interface AuthRepository {
    @GET("/me")
    suspend fun me(): Any
}