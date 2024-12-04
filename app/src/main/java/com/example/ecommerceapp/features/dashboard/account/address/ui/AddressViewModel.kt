package com.example.ecommerceapp.features.dashboard.account.address.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.core.utils.isValidPhoneNumber
import com.example.ecommerceapp.features.dashboard.account.address.domain.Address
import com.example.ecommerceapp.features.dashboard.account.address.domain.AddressRepository
import com.facebook.internal.Utility.logd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(private val addressRepository: AddressRepository) : ViewModel() {


    // data section
    var name = mutableStateOf("")
    var phone = mutableStateOf("")
    var addressDetails = mutableStateOf("")
    var city = mutableStateOf("")
    var isSubmittedAddress = mutableStateOf(false)
    fun updateName(newName: String) { name.value = newName }
    fun updatePhone(newPhone: String) { phone.value = newPhone}
    fun updateAddressDetails(newAddressDescription: String) { addressDetails.value = newAddressDescription}
    fun updateCity(newCity: String) { city.value = newCity}
    fun resetFields() {
        name.value = ""
        phone.value = ""
        addressDetails.value = ""
        city.value = ""
    }
    fun validateFields(): Boolean {
        return name.value.isNotEmpty() && phone.value.isNotEmpty() && phone.value.isValidPhoneNumber() && addressDetails.value.isNotEmpty() && city.value.isNotEmpty()
    }
    private var _addOrUpdateAddress = MutableStateFlow<Boolean>(false)
    val addOrUpdateAddress: StateFlow<Boolean> = _addOrUpdateAddress.asStateFlow()
  fun addOrUpdateAddress(address: Address) {
      viewModelScope.launch {
          addressRepository.AddOrUpdateAddress(address).collect{
              _addOrUpdateAddress.value = it
          }
      }
  }
    private var _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses.asStateFlow()
    fun getAddresses() {
        viewModelScope.launch {
            addressRepository.getAddresses().collect{
                _addresses.value = it

            }
            Log.d("getAddresses:" ,"${_addresses.value.size}" )
        }
    }
    private val _address = MutableStateFlow<Address?>(null)
    val address: StateFlow<Address?> = _address.asStateFlow()
    fun getAddressById(id: Int) {
        viewModelScope.launch {
            addressRepository.getAddressById(id).collect{
                _address.value = it
            }
        }
    }

    fun deleteAddress(address: Address) {
        viewModelScope.launch {
            addressRepository.deleteAddress(address)

            getAddresses()
        }
    }

    // Method to handle address selection and reset others
    fun selectAddress(address: Address) {
        viewModelScope.launch {
            addressRepository.resetAllAddressSelection(address.id)
            // Refresh addresses list
            getAddresses()
        }
    }
}