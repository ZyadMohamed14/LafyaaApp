package com.example.ecommerceapp.features.dashboard.account.order.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.account.order.domain.Order
import com.example.ecommerceapp.features.dashboard.account.order.domain.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

   val isOrdersFetched = mutableStateOf(false)
    private val _ordersState = MutableStateFlow<Resource<List<Order>?>>(Resource.Idle())
    val ordersState : StateFlow<Resource<List<Order>?>> = _ordersState.asStateFlow()

    fun getOrders (userid: String){
        viewModelScope.launch {
            orderRepository.getOrders(userid).collect{
                _ordersState.value = it
            }
        }
    }

    private val _createOrderState = MutableStateFlow<Boolean>(false)
    val createOrderState : StateFlow<Boolean> = _createOrderState.asStateFlow()
    val isOrderCreated = mutableStateOf(false)
    fun createOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.createOrder(order).collect {
                _createOrderState.value = it
                isOrderCreated.value = true
            }
        }
    }
}