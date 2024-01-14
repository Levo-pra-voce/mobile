package com.levopravoce.mobile.features.auth.data.dto


data class JwtResponseDTO(
    val token: String,
    val userType: UserType
)

