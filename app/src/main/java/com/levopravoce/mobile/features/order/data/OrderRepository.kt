package com.levopravoce.mobile.features.order.data

import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.features.order.data.dto.GoogleDistanceMatrixRequestDTO
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderRepository {

    @POST("/api/order")
    suspend fun requestOrder(
        @Body user: OrderDTO
    ): Response<OrderDTO>

    @GET("/api/order/deliveries-pending")
    suspend fun findAllPending(): List<OrderDTO>

    @GET("/api/order/{id}")
    suspend fun findById(
        @Path("id") id: String
    ): OrderDTO
    @GET("/api/map/distance")
    suspend fun getDistance(
        @Query("originLat") originLat: Double,
        @Query("originLng") originLng: Double,
        @Query("destinationLat") destinationLat: Double,
        @Query("destinationLng") destinationLng: Double
    ): Response<GoogleDistanceMatrixRequestDTO>

    @GET("/api/order")
    suspend fun getAvailableDeliveryUsers(
        @Query("userType") userType: UserType
    ): Response<List<UserDTO>>

    @GET("/api/order/last-progress")
    suspend fun getLastProgress(): Response<OrderDTO?>

    @PUT("/api/order/finish")
    suspend fun finishOrder(): Response<Unit>
}
