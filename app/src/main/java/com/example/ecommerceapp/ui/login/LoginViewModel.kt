package com.example.ecommerceapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.reposotiry.auth.FirebaseAuthRepository
import com.example.ecommerceapp.data.reposotiry.user.UserPreferencesRepository
import com.example.ecommerceapp.utils.isVaildEamil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {
    private val _loginState = MutableSharedFlow<Resource<String>>()
    val loginState: SharedFlow<Resource<String>> = _loginState.asSharedFlow()

    val emailState = MutableStateFlow("")
    val passwordState = MutableStateFlow("")
    val isLoginValid: Flow<Boolean> = combine(emailState, passwordState) { email, password ->
        email.isVaildEamil() && password.length >= 6
    }

    fun login() {

        val email = emailState.value
        val password = passwordState.value
        viewModelScope.launch {
            if (isLoginValid.first()) {
                authRepository.loginWithEmailAndPassword(email, password).onEach { resource ->
                    Log.d("tag", "Emitted resource: $resource")
                    when (resource) {
                        is Resource.Loading -> _loginState.emit(Resource.Loading())
                        is Resource.Success -> {
//                            userPrefs.saveUserEmail(email)
                            _loginState.emit(Resource.Success(resource.data ?: "Empty User Id"))
                        }

                        is Resource.Error -> _loginState.emit(
                            Resource.Error(resource.exception ?: Exception("Unknown error"))
                        )
                    }
                }.launchIn(viewModelScope)
            } else {
                _loginState.emit(Resource.Error(Exception("Invalid email or password")))
            }

        }
    }
    fun loginWithGoogle(idToken: String) = viewModelScope.launch {
        authRepository.loginWithGoogle(idToken).onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    //TODO get user details from the user id
                    _loginState.emit(Resource.Success(resource.data ?: "Empty User Id"))
                }

                else -> _loginState.emit(resource)
            }
        }.launchIn(viewModelScope)
    }

    fun loginWithFacebook(token: String) = viewModelScope.launch {
        authRepository.loginWithFacebook(token).onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    _loginState.emit(Resource.Success(resource.data ?: "Empty User Id"))
                }

                else -> _loginState.emit(resource)
            }
        }.launchIn(viewModelScope)
    }




}





