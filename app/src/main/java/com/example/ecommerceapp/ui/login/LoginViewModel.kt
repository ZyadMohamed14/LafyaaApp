package com.example.ecommerceapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.datasource.repository.auth.FirebaseAuthRepository
import com.example.ecommerceapp.data.datasource.repository.user.UserPreferencesRepository
import com.example.ecommerceapp.model.Resource
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
import kotlinx.coroutines.flow.update
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



        }


/*
 fun loginWithGoogle(idToken:String){
        viewModelScope.launch {
            authRepository.loginWithGoogle(idToken).onEach {resource ->
                when(resource){
                    is Resource.Loading ->{ loginState.update { Resource.Loading() }}
                    is Resource.Success ->{
                        loginState.update { Resource.Success(resource.data?:"Empty UserId") }
                    }
                    is Resource.Error -> {
                        loginState.value = Resource.Error(resource.exception?:Exception("UnknownExcp"))
                    }
                }
            }
        }
        }
 */





