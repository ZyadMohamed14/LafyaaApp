package com.example.ecommerceapp.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.user.domain.UserDetailsModel
import com.example.ecommerceapp.core.utils.isValidEmail
import com.example.ecommerceapp.features.auth.login.domain.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
  //  private val appPreferenceRepository: AppPreferenceRepository,
    private val authRepository: LoginRepository,


    ) : ViewModel() {
    private val _loginState= MutableSharedFlow<Resource<UserDetailsModel>>()
    val loginState: SharedFlow<Resource<UserDetailsModel>> = _loginState.asSharedFlow()


    val emailState = MutableStateFlow("")
    val passwordState = MutableStateFlow("")
    val isLoginValid: Flow<Boolean> = combine(emailState, passwordState) { email, password ->
        email.isValidEmail() && password.length >= 6
    }

    fun updateEmail(email: String) {
        this.emailState.value = email
    }
    fun updatePassword(password: String) {
        this.passwordState.value = password
    }
    fun isEmailValid() = emailState.value.isValidEmail()
    fun isPasswordValid() = passwordState.value.length >= 6
    fun loginWithEmailAndPassword() = viewModelScope.launch(IO) {
        val email = emailState.value
        val password = passwordState.value
        if (isLoginValid.first()) {
           handleLoginFlow { authRepository.loginWithEmailAndPassword(email, password) }
        } else {
            _loginState.emit(Resource.Error(Exception("Invalid email or password")))
        }
    }

    //TODO Login with Google
    fun loginWithGoogle(idToken: String) {
        handleLoginFlow { authRepository.loginWithGoogle(idToken) }
    }

    fun loginWithFacebook(token: String) {
        handleLoginFlow { authRepository.loginWithFacebook(token) }
    }
    private fun handleLoginFlow(loginFlow: suspend () -> Flow<Resource<UserDetailsModel>>) =
        viewModelScope.launch(IO) {
            loginFlow().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        savePreferenceData(resource.data!!)
                        _loginState.emit(Resource.Success(resource.data))
                    }

                    else -> _loginState.emit(resource)
                }
            }
        }


    private suspend fun savePreferenceData(userDetailsModel: UserDetailsModel) {
       // appPreferenceRepository.saveLoginState(true)
      //  userPreferenceRepository.updateUserDetails(userDetailsModel.toUserDetailsPreferences())
    }

}





