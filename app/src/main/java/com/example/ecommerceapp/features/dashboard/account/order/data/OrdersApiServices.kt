package com.example.ecommerceapp.features.dashboard.account.order.data


import com.example.ecommerceapp.features.dashboard.account.order.domain.CreatedOrderResponse
import com.example.ecommerceapp.features.dashboard.account.order.domain.Order
import com.example.ecommerceapp.features.dashboard.account.order.domain.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OrdersApiServices {
    @GET("/getAllOrders")
    suspend fun getAllUserOrders(@Query("userId") userId: String): Response<OrderResponse>

    @POST("/createOrder")
    suspend fun createOrder(@Body order: Order): Response<CreatedOrderResponse>
}
