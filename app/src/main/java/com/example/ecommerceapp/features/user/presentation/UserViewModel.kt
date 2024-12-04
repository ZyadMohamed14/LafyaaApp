package com.example.ecommerceapp.features.user.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.core.local.SharedPreferencesHelper
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.user.data.UpdateUserRequest
import com.example.ecommerceapp.features.user.domain.UseRepository
import com.example.ecommerceapp.features.user.domain.UserDetailsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
   private val userRepository: UseRepository,
    private val sharedPreferences: SharedPreferencesHelper
    ): ViewModel() {


    private  val _updateUserState = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val updateUserState : StateFlow<Resource<Boolean>> = _updateUserState

    private  val _isDataUpdated = MutableStateFlow<Boolean>(false)
    val isDataUpdated : StateFlow<Boolean> = _isDataUpdated
    fun updateUser(user: UpdateUserRequest) {

        viewModelScope.launch {

            userRepository.updateUserDetails(user).collect{
                Log.d("UserViewModel", "updateUser: ${it.data.toString()}")
                _updateUserState.value = it
                _isDataUpdated.value = true
            }

        }
    }

    private val _userDetails = MutableStateFlow<UserDetailsModel?>(null)
    val userDetails: StateFlow<UserDetailsModel?> = _userDetails
    fun loadUserDetails() {
        viewModelScope.launch {
            val id = sharedPreferences.getUserId()
            if (id != null) {
                userRepository.getUserFromLocal(id).collect {
                    _userDetails.value = it
                    Log.d("UserViewModel", "loadUserDetails: ${it.toString()}")
                }
            }
        }
    }

    override fun onCleared() {
        Log.d("UserViewModel", "onCleared: ")
        super.onCleared()
    }

}