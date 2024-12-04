package com.example.ecommerceapp.features.auth.register.domain.repo


import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.auth.register.domain.model.RegisterRequestModel
import com.example.ecommerceapp.features.user.domain.UserDetailsModel
import kotlinx.coroutines.flow.Flow


interface RegisterRepository {
    suspend fun registerUser(
        registerRequestModel: RegisterRequestModel
    ): Flow<Resource<UserDetailsModel?>>

    suspend fun registerWithGoogle(
        idToken: String
    ): Flow<Resource<UserDetailsModel?>>

}