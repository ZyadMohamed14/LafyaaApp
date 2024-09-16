package com.example.ecommerceapp.data.reposotiry.product

import com.example.ecommerceapp.data.model.ProductModel
import com.example.ecommerceapp.data.model.Resource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getCategoryProducts(categoryID: String, pageLimit: Int): Flow<List<ProductModel>>

    fun getSaleProducts(
        countryID: String, saleType: String, pageLimit: Int
    ): Flow<List<ProductModel>>

    suspend fun getAllProductsPaging(
        countryID: String, pageLimit: Long, lastDocument: DocumentSnapshot? = null
    ): Flow<Resource<QuerySnapshot>>
}