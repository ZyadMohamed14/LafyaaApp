package com.example.ecommerceapp.data.reposotiry.user

import com.example.ecommerceapp.data.model.user.CountryData
import com.example.ecommerceapp.data.model.user.UserDetailsPreferences
import com.example.ecommerceapp.ui.auth.country.model.CountryUIModel
import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    fun getUserDetails(): Flow<UserDetailsPreferences>
    suspend fun updateUserId(userId: String)
    suspend fun getUserId(): Flow<String>
    suspend fun clearUserPreferences()
    suspend fun updateUserDetails(userDetailsPreferences: UserDetailsPreferences)
    suspend fun saveUserCountry(countryId: CountryUIModel)
    fun getUserCountry():  Flow<CountryData>
}