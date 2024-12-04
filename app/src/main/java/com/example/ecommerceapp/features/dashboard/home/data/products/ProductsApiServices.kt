package com.example.ecommerceapp.features.dashboard.home.data.products

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApiServices {

    @GET("getAllProducts")
    suspend fun getAllProducts(
        @Query("pageSize") pageSize: Int,
        @Query("lastVisibleId") lastVisibleId: String? = null
    ): Response<ProductsResponse>
    @GET("searchProducts")
   suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Response<ProductsResponse>
    @GET("getSaleProducts")
    suspend fun getSaleProducts(
        @Query("saleType") saleType: String,  // Type of sale (e.g., "flash_sale")
        @Query("pageLimit") pageLimit: Int    // Limit the number of products to retrieve
    ): Response<ProductsResponse>

    @GET("getProductById")
    suspend fun getProductById(@Query("product_id") productId: String): Response<ProductResponse>
}