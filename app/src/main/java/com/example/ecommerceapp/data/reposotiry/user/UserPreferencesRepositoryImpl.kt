package com.example.ecommerceapp.data.reposotiry.user

import android.app.Application
import android.content.Context
import com.example.ecommerceapp.data.datasource.local.userDetailsDataStore
import com.example.ecommerceapp.data.model.user.CountryData
import com.example.ecommerceapp.data.model.user.UserDetailsPreferences
import com.example.ecommerceapp.ui.auth.country.model.CountryUIModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferenceRepositoryImpl @Inject constructor(val context: Context) :
    UserPreferenceRepository {
    override fun getUserDetails(): Flow<UserDetailsPreferences> {
        return context.userDetailsDataStore.data
    }

    override suspend fun updateUserId(userId: String) {
        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().setId(userId).build()
        }
    }

    override suspend fun getUserId(): Flow<String> {
        return context.userDetailsDataStore.data.map { it.id }
    }

    override suspend fun clearUserPreferences() {
        context.userDetailsDataStore.updateData { preferences ->
            preferences.toBuilder().clear().build()
        }
    }

    override suspend fun updateUserDetails(userDetailsPreferences: UserDetailsPreferences) {
        context.userDetailsDataStore.updateData { userDetailsPreferences }
    }

    override suspend fun saveUserCountry(countryId: CountryUIModel) {

        val countryData = CountryData.newBuilder().setId(countryId.id).setCode(countryId.code)
            .setName(countryId.name).setCurrency(countryId.currency)
            .setCurrencySymbol(countryId.currencySymbol).build()
        context.userDetailsDataStore.updateData { preferences ->
           preferences.toBuilder().setCountry(countryData).build()
        }


    }

    override fun getUserCountry(): Flow<CountryData> {

       return context.userDetailsDataStore.data.map { it.country }

    }
}