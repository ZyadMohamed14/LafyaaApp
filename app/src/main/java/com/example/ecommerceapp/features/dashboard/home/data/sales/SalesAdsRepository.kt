package com.example.ecommerceapp.features.dashboard.home.data.sales


import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.home.domain.model.SalesAdUIModel
import kotlinx.coroutines.flow.Flow

interface SalesAdsRepository {
    fun getSalesAds(): Flow<Resource<List<SalesAdUIModel>>>
}