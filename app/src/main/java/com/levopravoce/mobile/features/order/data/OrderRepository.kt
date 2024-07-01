package com.levopravoce.mobile.features.order.data

import com.levopravoce.mobile.features.auth.data.dto.UserDTO
import com.levopravoce.mobile.features.auth.data.dto.UserType
import com.levopravoce.mobile.features.deliveryManList.data.dto.RequestDTO
import com.levopravoce.mobile.features.order.data.dto.GoogleDistanceMatrixRequestDTO
import com.levopravoce.mobile.features.order.data.dto.OrderDTO
import com.levopravoce.mobile.features.order.data.dto.RecommendUserDTO
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

    @GET("/api/order/assign-orders")
    suspend fun findAllRequest(): List<RequestDTO>

    @GET("/api/order/{id}")
    suspend fun findById(
        @Path("id") id: String
    ): OrderDTO

    @GET("/api/order/deliverymans")
    suspend fun getAvailableDeliveryUsers(): Response<List<RecommendUserDTO>>

    @GET("/api/order/last-progress")
    suspend fun getLastProgress(): Response<OrderDTO?>

    @GET("/api/order/last-pending")
    suspend fun getLastPending(): Response<OrderDTO?>

    @PUT("/api/order/finish")
    suspend fun finishOrder(): Response<Unit>

    @POST("api/order/assign-deliveryman/{deliverymanId}")
    suspend fun assignDeliveryman(
        @Path("deliverymanId") deliverymanId: Long
    ): Response<Unit>

    @POST("api/order/accept/{id}")
    suspend fun acceptOrder(
        @Path("id") id: Long
    ): Response<Unit>

    @POST("api/order/start/{id}")
    suspend fun startOrder(
        @Path("id") id: Long
    ): Response<Unit>
}
