package com.levopravoce.mobile.common.error

import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException


object ErrorUtils {
    fun parseError(response: Response<*>): String {
        val gson = GsonBuilder().serializeNulls().create()
        val error = gson.fromJson(response.errorBody()?.string(), APIError::class.java)
        return error.message ?: "Unknown error"
    }
}