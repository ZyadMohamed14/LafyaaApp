package com.example.ecommerceapp.features.dashboard.home.domain.model


import com.example.ecommerceapp.features.dashboard.home.data.sales.SalesAdModel

fun SalesAdModel.toUIModel(): SalesAdUIModel {
    return SalesAdUIModel(
        id = this.id,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        type = this.type,
        productId = this.productId,
        categoryId = this.categoryId,
        externalLink = this.externalLink,
        endAt = this.endAt
    )
}