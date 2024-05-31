package com.levopravoce.mobile.features.relatory.data

import com.levopravoce.mobile.features.app.data.dto.Page
import com.levopravoce.mobile.features.relatory.data.dto.RelatoryDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import java.time.LocalDate

interface RelatoryRepository {
    @GET("/api/relatory")
    suspend fun getRelatories(
        @Query("deliveryDate") deliveryDate: LocalDate? = null,
    ): Response<Page<RelatoryDTO>>

    @Streaming
    @GET("/api/relatory/xlsx")
    suspend fun getRelatoriesXlsx(
        @Query("deliveryDate") deliveryDate: LocalDate? = null,
    ): ResponseBody
}