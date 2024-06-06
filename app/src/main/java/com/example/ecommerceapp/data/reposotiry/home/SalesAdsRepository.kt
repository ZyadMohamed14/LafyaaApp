package com.example.ecommerceapp.data.reposotiry.home

import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.model.sale_ads.SalesAdModel
import kotlinx.coroutines.flow.Flow

interface SalesAdsRepository {
    fun getSalesAds(): Flow<Resource<List<SalesAdModel>>>
}