package com.example.ecommerceapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.data.reposotiry.auth.FirebaseAuthRepository
import com.example.ecommerceapp.data.reposotiry.user.UserPreferencesRepository

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