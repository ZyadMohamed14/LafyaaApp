package com.example.ecommerceapp.features.user.data

import com.example.ecommerceapp.features.user.domain.UserDetailsModel
import com.example.ecommerceapp.features.user.domain.UserDetailsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiServices {
    @GET("getUserDataById/{user_id}")
    suspend fun getUserDetails(@Path("user_id") id: String): Response<UserDetailsModel>

    @PUT("updateUserData/{user_id}")
    suspend fun updateUserDetails(
        @Path("user_id") id: String,
        @Body userData: UpdateUserRequest
    ): Response<UserDetailsResponse>

}

data class UpdateUserRequest(
    val user_id: String,
    val email: String?=null,
    val name: String?=null,
    val image: String?=null,
    val phone: String?=null,
    val disabled: Boolean?=null
)