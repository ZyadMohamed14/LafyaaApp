package com.example.ecommerceapp.features.dashboard.home.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope


import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.home.data.category.CategoriesRepository
import com.example.ecommerceapp.features.dashboard.home.data.products.ProductModel
import com.example.ecommerceapp.features.dashboard.home.data.sales.SalesAdsRepository
import com.example.ecommerceapp.features.dashboard.home.domain.ProductsRepository
import com.example.ecommerceapp.features.dashboard.home.data.products.ProductSaleType
import com.example.ecommerceapp.features.dashboard.home.data.products.ProductsResponse
import com.example.ecommerceapp.features.detailsproduct.domain.ProductOrder
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    salesAdsRepository: SalesAdsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val productsRepository: ProductsRepository,
) : ViewModel() {

    private var isFetching = false
     var lastVisibleId: String? = null



    private val _getAllProductsState = MutableStateFlow<Resource<ProductsResponse>>(Resource.Idle())
    val getAllProductsState: StateFlow<Resource<ProductsResponse>> = _getAllProductsState.asStateFlow()
    fun getAllProducts(pageLimit:Int, lastVisibleId: String?) {

        if (isFetching) return // Avoid duplicate fetches
        isFetching = true
        viewModelScope.launch {

            productsRepository.getProducts(pageLimit, lastVisibleId).collect {
                Log.d("HomeViewModel", "getAllProducts: $it")
                _getAllProductsState.emit(it)
            }
            isFetching = false
        }
    }

      val _prdouctIdState = MutableStateFlow<Resource<ProductUIModel?>>(Resource.Loading())
      val prdouctIdState: StateFlow<Resource<ProductUIModel?>> = _prdouctIdState.asStateFlow()
    fun getProductById(productId: String){
        viewModelScope.launch {
            productsRepository.getProductByID(productId).collect{
                _prdouctIdState.emit(it)
            }
        }
    }

    val salesAdsState = salesAdsRepository.getSalesAds().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )
    val categoriesState = categoriesRepository.getCategories().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )

    // Get Sale Products Flow as StateFlow

    val flashSaleState: StateFlow<Resource<List<ProductUIModel?>>> = getProductsSales(ProductSaleType.FLASH_SALE)

    val megaSaleState: StateFlow<Resource<List<ProductUIModel?>>> = getProductsSales(ProductSaleType.MEGA_SALE)

    // Map the StateFlow to LiveData to observe in the UI
    val isEmptyFlashSale = flashSaleState.map { it is Resource.Success && it.data.isNullOrEmpty() }
        .asLiveData()

    val isEmptyMegaSale = megaSaleState.map { it is Resource.Success && it.data.isNullOrEmpty() }
        .asLiveData()

    // Function to get sale products using StateFlow
    fun getProductsSales(productSaleType: ProductSaleType): StateFlow<Resource<List<ProductUIModel?>>> {
        return productsRepository.getSaleProducts(productSaleType.type, 20)
            .stateIn(viewModelScope + IO, SharingStarted.Eagerly, Resource.Loading())
    }

    private  val _productSearchState = MutableStateFlow<Resource<List<ProductUIModel?>?>>(Resource.Idle())
    val productSearchState: StateFlow<Resource<List<ProductUIModel?>?>> = _productSearchState.asStateFlow()
fun searchProducts(query: String){
   viewModelScope.launch {
       val response = productsRepository.searchProducts(query,pageLimit = 10)

       response.collect{ state->
           Log.d("ReviewsViewModel", "state: $state.")
           when(state){
               is Resource.Error -> {
                   _productSearchState.emit(
                       Resource.Error(
                           exception = state.exception,
                           errorResponse = state.errorResponse

                       ))
               }
               is Resource.Loading -> {
                   _productSearchState.emit(Resource.Loading())
               }
               is Resource.Success -> {
                   _productSearchState.emit(Resource.Success(state.data))
               }

               is Resource.Idle -> _productSearchState.emit(Resource.Idle())
           }
       }
   }
}

    val _isAddSuccessfully = MutableStateFlow(false)
    val isAddSuccessfully = _isAddSuccessfully.asSharedFlow()
fun addToCart(product: ProductOrder){
    viewModelScope.launch {
        productsRepository.saveProduct(product).collect{
            _isAddSuccessfully.value = it
        }

    }
}





}
