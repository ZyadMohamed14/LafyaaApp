package com.example.ecommerceapp.features.dashboard.home.domain


import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.home.data.products.ProductModel
import com.example.ecommerceapp.features.dashboard.home.data.products.ProductsResponse
import com.example.ecommerceapp.features.detailsproduct.domain.ProductOrder
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getProducts(pageLimit: Int, lastVisibleId: String?): Flow<Resource<ProductsResponse>>
    fun getCategoryProducts(categoryID: String, pageLimit: Int): Flow<List<ProductModel>>

    fun getSaleProducts(
         saleType: String, pageLimit: Int
    ):  Flow<Resource<List<ProductUIModel?>>>



    suspend fun searchProducts(
        query: String, pageLimit: Int
    ): Flow<Resource<List<ProductUIModel?>>>


   suspend fun getProductByID(productID: String): Flow<Resource<ProductUIModel?>>

    // for cart
    suspend fun saveProduct(productOrder: ProductOrder):Flow<Boolean>

    suspend fun deleteProduct(productOrder: ProductOrder):Flow<Boolean>
    suspend fun getSavedProducts():Flow<List<ProductOrder>>
}