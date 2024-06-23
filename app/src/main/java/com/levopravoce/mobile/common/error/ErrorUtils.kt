package com.levopravoce.mobile.common.error

import com.google.gson.GsonBuilder
import retrofit2.Response


object ErrorUtils {
    fun parseError(response: Response<*>): String {
        val gson = GsonBuilder().serializeNulls().create()
        val error = gson.fromJson(response.errorBody()?.string(), APIError::class.java)
        return error.message ?: "Unknown error"
    }
}