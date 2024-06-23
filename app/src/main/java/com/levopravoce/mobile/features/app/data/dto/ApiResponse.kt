package com.levopravoce.mobile.features.app.data.dto

sealed interface ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>
    data class Error<T>(val message: String) : ApiResponse<T>
    data class Loading<T>(val data: T? = null) : ApiResponse<T>
}