package com.example.ecommerceapp.ui.dashboard.home.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.model.ProductModel
import com.example.ecommerceapp.data.model.ProductSaleType
import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.model.user.CountryData
import com.example.ecommerceapp.data.reposotiry.home.category.CategoriesRepository
import com.example.ecommerceapp.data.reposotiry.home.sales.SalesAdsRepository
import com.example.ecommerceapp.data.reposotiry.product.ProductsRepository
import com.example.ecommerceapp.data.reposotiry.user.UserPreferenceRepository
import com.example.ecommerceapp.domain.toProductUIModel
import com.example.ecommerceapp.ui.products.ProductUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    salesAdsRepository: SalesAdsRepository,
    private val categoriesRepository: CategoriesRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val productsRepository: ProductsRepository,
) : ViewModel() {


    val salesAdsState = salesAdsRepository.getSalesAds().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )
    val categoriesState = categoriesRepository.getCategories().stateIn(
        viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = Resource.Loading()
    )
    val countryState = userPreferenceRepository.getUserCountry().stateIn(
        viewModelScope + IO,
        started = SharingStarted.Eagerly,
        initialValue = CountryData.getDefaultInstance()
    )
    val flashSaleState = getProductsSales(ProductSaleType.FLASH_SALE)
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getProductsSales(productSaleType: ProductSaleType): StateFlow<List<ProductUIModel>> =
        countryState.mapLatest {
            Log.d(TAG, "Countryid for flah sale: ${it.id}")
            productsRepository.getSaleProducts(it.id ?: "0", productSaleType.type, 10)
        }.mapLatest { it.first().map { getProductModel(it) } }.stateIn(
            viewModelScope + IO, started = SharingStarted.Eagerly, initialValue = emptyList()
        )
    private fun getProductModel(product: ProductModel): ProductUIModel {
        val productUIModel = product.toProductUIModel().copy(
            currencySymbol = countryState.value?.currencySymbol ?: ""
        )
        return productUIModel
    }
    fun startTimer() {
        salesAdsState.value.data?.forEach { it.startCountdown() }
    }
    fun stopTimer() {
        salesAdsState.value.data?.forEach { it.stopCountdown() }
    }
}
