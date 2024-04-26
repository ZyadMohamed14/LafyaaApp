package com.example.ecommerceapp.ui.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.data.datasource.local.AppPreferencesDataSource
import com.example.ecommerceapp.data.reposotiry.auth.FirebaseAuthRepositoryImpl
import com.example.ecommerceapp.data.reposotiry.common.AppDataStoreRepositoryImpl
import com.example.ecommerceapp.data.reposotiry.user.UserPreferenceRepositoryImpl

// create viewmodel factory class
class LoginViewModelFactory(
    private val contextValue: Context
) : ViewModelProvider.Factory {

    private val appPreferenceRepository =
        AppDataStoreRepositoryImpl(AppPreferencesDataSource(contextValue))
    private val userPreferenceRepository = UserPreferenceRepositoryImpl(contextValue)
    private val authRepository = FirebaseAuthRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginViewModel(
                appPreferenceRepository,
                userPreferenceRepository,
                authRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}