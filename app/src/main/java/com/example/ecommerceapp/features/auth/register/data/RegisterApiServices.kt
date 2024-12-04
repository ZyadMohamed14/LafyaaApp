package com.example.ecommerceapp.features.auth.register.data


import com.example.ecommerceapp.features.auth.register.domain.model.RegisterRequestModel
import com.example.ecommerceapp.features.auth.register.domain.model.RegisterResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApiServices {
    @POST("registerUser")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequestModel
    ): Response<RegisterResponseModel?>
}