package com.example.ecommerceapp.features.dashboard.cart.ui

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.features.dashboard.account.order.domain.CustomerData
import com.example.ecommerceapp.features.dashboard.account.order.domain.Order

import com.example.ecommerceapp.features.dashboard.home.domain.ProductsRepository
import com.example.ecommerceapp.features.detailsproduct.domain.ProductOrder
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {


    private val _order = mutableStateOf(
        Order(
            products = mutableListOf(),
            customerData = CustomerData("", "", ""), // Adjust to your default customer data structure
            isPaid = false,
            price = 0.0,

        )
    )
    val order: State<Order> = _order

    private val _productsState = MutableStateFlow<List<ProductOrder>>(emptyList())
    val productsState: StateFlow<List<ProductOrder>> = _productsState.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            productsRepository.getSavedProducts().collect { products ->
                _productsState.value = products
                calculateTotalPrice() // Calculate total price whenever products are fetched
            }
        }
    }

    fun incrementQuantity(productId: String) {
        _productsState.value = _productsState.value.map { product ->
            if (product.id == productId) product.copy(quantity = product.quantity + 1) else product
        }
        calculateTotalPrice() // Recalculate total price after increment
    }

    fun decrementQuantity(productId: String) {
        _productsState.value = _productsState.value.map { product ->
            if (product.id == productId && product.quantity > 1) product.copy(quantity = product.quantity - 1) else product
        }
        calculateTotalPrice() // Recalculate total price after decrement
    }

    private fun calculateTotalPrice() {
        _totalPrice.value = _productsState.value.sumOf { product ->
            product.price * product.quantity
        }
    }

    fun removeFromCart(product: ProductOrder) {
        viewModelScope.launch {
            productsRepository.deleteProduct(product).collect{
                Log.d("CartViewModsdsddel", "Product removed from cart: $it")
            }
            getProducts()

        }

    }
}