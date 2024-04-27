package com.example.ecommerceapp.ui.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel: ViewModel() {

    val useId = MutableStateFlow("")
}