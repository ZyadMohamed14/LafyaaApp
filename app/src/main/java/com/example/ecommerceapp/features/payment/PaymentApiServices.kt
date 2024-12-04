package com.example.ecommerceapp.features.payment

import com.example.docappincompose.core.api.ApiConstants
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface PaymentApiServices {
    @FormUrlEncoded
   @POST
    suspend fun getClientSecret(
        @Url url: String ,
        @Header("Authorization") apiKey: String,
        @Field("amount") amount: Int,
        @Field("currency") currency: String
    ): Response<PaymentResponse>
}


