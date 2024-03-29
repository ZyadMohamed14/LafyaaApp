package com.example.ecommerceapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.datasource.repository.user.UserPreferenceRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    fun saveLoginState(isLoggedIn: Boolean) {
        viewModelScope.launch {


        }
    }
}