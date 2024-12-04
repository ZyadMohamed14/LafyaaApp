package com.example.ecommerceapp.features.auth.login.data

import com.example.ecommerceapp.features.user.domain.UserDetailsModel
import retrofit2.Response

interface LoginApiServices {
    suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ): Response<UserDetailsModel?>

    suspend fun loginWithGoogle(
        idToken: String
    ): Response<UserDetailsModel?>

    suspend fun loginWithFacebook(token: String): Response<UserDetailsModel?>

}