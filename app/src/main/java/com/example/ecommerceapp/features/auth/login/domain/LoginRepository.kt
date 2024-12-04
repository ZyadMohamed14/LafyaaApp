package com.example.ecommerceapp.features.auth.login.domain

import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.user.domain.UserDetailsModel
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ): Flow<Resource<UserDetailsModel>>

    suspend fun loginWithGoogle(
        idToken: String
    ): Flow<Resource<UserDetailsModel>>

    suspend fun loginWithFacebook(token: String): Flow<Resource<UserDetailsModel>>

}