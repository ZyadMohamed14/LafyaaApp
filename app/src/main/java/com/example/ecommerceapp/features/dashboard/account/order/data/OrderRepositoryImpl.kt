package com.example.ecommerceapp.features.dashboard.account.order.data

import android.util.Log
import com.example.docappincompose.core.networking.ErrorResponse
import com.example.docappincompose.core.networking.mapErrorToException
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.account.order.domain.Order
import com.example.ecommerceapp.features.dashboard.account.order.domain.OrderRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val apiServices: OrdersApiServices
) : OrderRepository {
    override suspend fun getOrders(userId: String):Flow<Resource<List<Order>?>>  = flow{
        try {
            emit(Resource.Loading())

            val response = apiServices.getAllUserOrders(userId)
            if (response.isSuccessful && response.body() != null) {

                emit(Resource.Success(response.body()!!.data))
            } else {

                val errorResponse: ErrorResponse? = response.errorBody()?.string()?.let {
                    // Parse the error body into ErrorResponse
                    Gson().fromJson(it, ErrorResponse::class.java)
                }

                Log.d("zizofefefef", "getOrders: ${errorResponse!!.message}")
                emit(
                    Resource.Error(
                        errorResponse = errorResponse
                    )
                )
            }
        } catch (error: Exception) {
             Log.d("zizofefefef", "getOrders: ${error.message}")
            val exception = mapErrorToException(error)
            emit(
                Resource.Error(
                    exception = exception,
                )
            )
        }
    }

    override suspend fun createOrder(order: Order): Flow<Boolean> = flow {
        try {
            val response = apiServices.createOrder(order)
            if (response.isSuccessful && response.body() != null) {
                emit(true)
            } else {
                val errorResponse: ErrorResponse? = response.errorBody()?.string()?.let {
                    // Parse the error body into ErrorResponse
                    Gson().fromJson(it, ErrorResponse::class.java)
                }
                Log.d("zizofefefef", "createOrder errorResponse: ${errorResponse!!.message}")
                emit(false)
            }
        } catch (error: Exception) {
            Log.d("zizofefefef", "createOrder Exception : ${error.message}")
            emit(false)
        }
    }
}