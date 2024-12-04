package com.example.ecommerceapp.features.user.domain


import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.user.data.UpdateUserRequest
import kotlinx.coroutines.flow.Flow

interface UseRepository {
    suspend fun getUserDetails(userId: String): Flow<Resource<UserDetailsModel?>>
    suspend fun logoutUser(): Flow<Resource<Boolean>>
    suspend fun updateUserDetails(userDetailsModel: UpdateUserRequest): Flow<Resource<Boolean>>
    suspend fun getUserFromLocal(id: String): Flow<UserDetailsModel?>

}