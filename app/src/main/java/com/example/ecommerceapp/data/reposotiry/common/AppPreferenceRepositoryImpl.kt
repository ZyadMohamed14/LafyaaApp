package com.example.ecommerceapp.data.reposotiry.common

import com.example.ecommerceapp.data.datasource.local.AppPreferencesDataSource
import kotlinx.coroutines.flow.Flow



class AppDataStoreRepositoryImpl(
    private val appPreferencesDataSource: AppPreferencesDataSource
) : AppPreferenceRepository {

    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        appPreferencesDataSource.saveLoginState(isLoggedIn)
    }

    override suspend fun isLoggedIn(): Flow<Boolean> {
        return appPreferencesDataSource.isUserLoggedIn
    }
}