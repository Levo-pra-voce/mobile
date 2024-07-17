package com.levopravoce.mobile.features.rating.data

import com.levopravoce.mobile.features.order.data.dto.RatingDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface RatingRepository {
    @POST("api/order/review/{orderId}")
    suspend fun sendReview(
        @Path("orderId") id: Long,
        @Body reviewDTO: RatingDTO
    ): Response<Unit>
}