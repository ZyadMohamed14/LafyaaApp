package com.example.ecommerceapp.features.detailsproduct.domain


import com.example.ecommerceapp.features.dashboard.home.data.products.ProductModel


fun ProductUIModel.toProductModel(): ProductModel {
    return ProductModel(
        id = id,
        name = name,
        description = description,
        category = category,      // Added category
        images = images,
        price = price,
        rate = rate,
        salePercentage = salePercentage,
        saleType = saleType,
        colors = colors
        // 'sizes' is not present in ProductUIModel, but you can handle it separately if needed
    )
}

// Function to convert ProductModel to ProductUIModel
fun ProductModel.toProductUIModel(): ProductUIModel {
    return ProductUIModel(
        id = id ?: throw IllegalArgumentException("Product ID is missing"),
        name = name ?: "No Name",
        description = description ?: "No Description",
        category = category ?: "", // Ensuring non-null category
        images = images ?: emptyList(),
        price = price ?: 0.0,
        rate = rate ?: 0f,
        salePercentage = salePercentage,
        saleType = saleType,
        sizes = sizes,
        colors = colors ?: emptyList()
    )
}

