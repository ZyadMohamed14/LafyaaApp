package com.example.ecommerceapp.data.reposotiry.user

import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.model.user.UserDetailsModel
import kotlinx.coroutines.flow.Flow

interface UserFirestoreRepository {
    suspend fun getUserDetails(userId: String): Flow<Resource<UserDetailsModel>>
}