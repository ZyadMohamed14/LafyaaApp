package com.example.ecommerceapp.features.dashboard.home.data.category


import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.home.domain.model.CategoryUIModel
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

    fun getCategories(): Flow<Resource<List<CategoryUIModel>>>
}