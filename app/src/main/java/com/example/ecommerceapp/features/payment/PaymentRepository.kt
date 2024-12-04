package com.example.ecommerceapp.features.payment

import android.util.Log
import com.example.docappincompose.core.api.ApiConstants
import com.example.docappincompose.core.networking.ErrorResponse
import com.example.docappincompose.core.networking.mapErrorToException
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.account.order.data.OrdersApiServices
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PaymentRepository @Inject constructor(private val paymentApiServices: PaymentApiServices,

    ) {


    suspend fun getClientSecret(amount: Int, currency: String): Flow<Resource<String?>> = flow {
/*

 */
        try {
            emit(Resource.Loading())
            val response = paymentApiServices.getClientSecret(
                ApiConstants.PaymentBaseURL,
                ApiConstants.apiKey,
                amount*100,
                currency
            )
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!.clientSecret))
            } else {

                val errorResponse: ErrorResponse? = response.errorBody()?.string()?.let {
                    // Parse the error body into ErrorResponse
                    Gson().fromJson(it, ErrorResponse::class.java)
                }
                Log.e("zyadPaymentRepository", "errorResponse:${response.errorBody()?.string()}}")
                emit(
                    Resource.Error(
                        errorResponse = errorResponse
                    )
                )
            }
        } catch (error: Exception) {
            Log.e("zyadPaymentRepository", "getClientSecret: $error")
            val exception = mapErrorToException(error)

            emit(
                Resource.Error(
                    exception = exception,
                )
            )
        }
    }
}
