package com.example.ecommerceapp.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.ecommerceapp.features.auth.register.domain.model.RegisterRequestModel

import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.auth.register.domain.repo.RegisterRepository
import com.example.ecommerceapp.core.utils.isValidEmail
import com.example.ecommerceapp.features.user.domain.UserDetailsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
   private val registerRepo: RegisterRepository
) : ViewModel() {
    private val _registerState = MutableSharedFlow<Resource<UserDetailsModel?>>()
    val registerState: SharedFlow<Resource<UserDetailsModel?>> = _registerState.asSharedFlow()

    val name = MutableStateFlow("")

    val email = MutableStateFlow("")

    val password = MutableStateFlow("")

    val confirmPassword = MutableStateFlow("")


     val isRegisterIsValid = combine(
        name, email, password, confirmPassword
    ) { name, email, password, confirmPassword ->
        email.isValidEmail() && password.length >= 6 && name.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
    }

    fun updateName(name: String) {
        this.name.value = name
    }
    fun updateEmail(email: String) {
        this.email.value = email
    }
    fun updatePassword(password: String) {
        this.password.value = password
    }
    fun updateConfirmPassword(confirmPassword: String) {
        this.confirmPassword.value = confirmPassword
    }
    fun isNameValid()= name.value.isNotEmpty()

    fun isEmailValid() = email.value.isValidEmail()
    fun isPasswordValid() = password.value.length >= 6
    fun isConfirmPasswordValid() = confirmPassword.value.isNotEmpty() && password.value == confirmPassword.value


   fun clearFields() {
        name.value = ""
        email.value = ""
        password.value = ""
        confirmPassword.value = ""
   }
    fun registerUserWithEmailAndPassword(
        registerRequestModel: RegisterRequestModel
    ) = viewModelScope.launch(IO) {
        registerRepo.registerUser(registerRequestModel).collect{ state->
            when(state){
                is Resource.Error -> {
                    _registerState.emit(
                        Resource.Error(
                        exception = state.exception,
                        errorResponse = state.errorResponse

                    ))
                }
                is Resource.Loading -> {
                    _registerState.emit(Resource.Loading())
                }
                is Resource.Success -> {
                    _registerState.emit(Resource.Success(state.data))
                }

                is Resource.Idle -> _registerState.emit(Resource.Idle())
            }

        }
    }

    fun registerWithGoogle(idToken: String) = viewModelScope.launch {
        registerRepo.registerWithGoogle(idToken).collect {
            _registerState.emit(it)
        }
    }


}