package com.example.ecommerceapp.features.payment

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _paymentState = MutableStateFlow<Resource<String?>>(Resource.Idle())
    val paymentState: StateFlow<Resource<String?>> = _paymentState

    private val _paymentResult = MutableStateFlow<PaymentState>(PaymentState.Ready)
    val paymentResult: StateFlow<PaymentState> = _paymentResult

    fun startPayment(amount: Int, currency: String) {
        viewModelScope.launch {
            paymentRepository.getClientSecret(amount, currency).collect { result ->
                Log.d("zyadPaymentRepository", "startPayment: ${result}")
                when (result) {

                    is Resource.Success -> {
                        _paymentState.value = result
                        _paymentResult.value = PaymentState.ClientSecretReceived(
                            clientSecret = result.data,
                            publishableKey = null
                        )
                    }
                    is Resource.Error -> {
                        Log.d("zyadPaymentRepository", "startPayment: ${result.exception?.message}")
                        _paymentState.value = result
                        _paymentResult.value = PaymentState.Error("Error during payment")
                    }
                    else -> Unit
                }
            }
        }
    }

    fun onPaymentSuccess() {
        _paymentResult.value = PaymentState.Success
    }

    fun onPaymentCancelled() {
        _paymentResult.value = PaymentState.Ready
    }

    fun onPaymentFailed(errorMessage: String?) {
        _paymentResult.value = PaymentState.Error(errorMessage ?: "Unknown error")
    }
}


sealed class PaymentState {
    data object Ready : PaymentState()
    data object Loading : PaymentState()
    data object Success : PaymentState()
    class Error(val message: String) : PaymentState()
    class ClientSecretReceived(
        val clientSecret: String?,
        val publishableKey: String?
    ) : PaymentState()
}

