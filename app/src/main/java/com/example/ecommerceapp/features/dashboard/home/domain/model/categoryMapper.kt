package com.example.ecommerceapp.features.dashboard.home.domain.model

import com.example.ecommerceapp.features.dashboard.home.data.category.CategoryModel


fun CategoryModel.toUIModel(): CategoryUIModel {
    return CategoryUIModel(
        id = id, name = name, icon = icon
    )
}