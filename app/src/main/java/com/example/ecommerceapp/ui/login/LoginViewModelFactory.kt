package com.example.ecommerceapp.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.data.datasource.remote.repository.auth.FirebaseAuthRepository
import com.example.ecommerceapp.data.datasource.remote.repository.user.UserPreferencesRepository

class LoginViewModelFactory(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val authRepository: FirebaseAuthRepository
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginViewModel(
            userPreferencesRepository,authRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}