package com.example.ecommerceapp.features.dashboard.account.order.domain

import com.example.ecommerceapp.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    suspend fun getOrders(userId: String): Flow<Resource<List<Order>?>>

    suspend fun createOrder(order: Order): Flow<Boolean>

}