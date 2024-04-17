package com.example.ecommerceapp.ui.login

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
     val loginState :MutableStateFlow<Resource<String>?> = MutableStateFlow(null)

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
                authRepository.loginWithEmailAndPassword(email, password).onEach {
                    when(it){
                        is Resource.Loading ->{ loginState.update { Resource.Loading() }}
                        is Resource.Success ->{
                            loginState.update { Resource.Success("") }
                        }
                        is Resource.Error -> {
                            loginState.value = Resource.Error(it.exception?:Exception("UnknownExcp"))
                        }
                    }
                }
            }else{
                loginState.value = Resource.Error(Exception("inVaild Email or Password"))

            }
        }

    }

    fun saveLoginState(isLoggedIn: Boolean) {
        viewModelScope.launch {


        }
    }
}