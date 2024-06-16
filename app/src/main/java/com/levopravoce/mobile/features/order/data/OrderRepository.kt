package com.levopravoce.mobile.features.order.data

import com.levopravoce.mobile.features.auth.data.dto.JwtResponseDTO
import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderRepository {

    @POST("/api/order")
    @Headers("isAuthorized: false")
    suspend fun requestOrder(
        @Body user: OrderDTO
    ): Response<OrderDTO>

    @GET("/api/order/deliveries-pending")
    suspend fun findAllPending(): List<OrderDTO>

    @GET("/api/order/{id}")
    suspend fun findById(
        @Path("id") id: String
    ): OrderDTO
}