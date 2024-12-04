package com.example.ecommerceapp.features.auth.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.core.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(private val authRepository: ForgetPasswordRepository) : ViewModel() {

    private val _forgetPasswordState = MutableSharedFlow<Resource<Boolean>>()
    val forgetPasswordState: SharedFlow<Resource<Boolean>> = _forgetPasswordState.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    // Function to update email
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }




    fun sendUpdatePasswordEmail() = viewModelScope.launch(IO) {
        if (email.value.isValidEmail()) {
            authRepository.forgetPassword(email.value).collect {
                _forgetPasswordState.emit(it)
            }
        } else {
            _forgetPasswordState.emit(Resource.Error(Exception("Invalid email")))
        }
    }
}
