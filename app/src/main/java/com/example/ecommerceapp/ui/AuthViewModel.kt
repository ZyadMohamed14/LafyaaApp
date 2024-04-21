package com.example.ecommerceapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel: ViewModel() {

    val useId = MutableStateFlow("")
}