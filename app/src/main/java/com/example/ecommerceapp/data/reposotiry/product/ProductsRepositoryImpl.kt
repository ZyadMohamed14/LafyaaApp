package com.example.ecommerceapp.data.reposotiry.product

import android.util.Log
import com.example.ecommerceapp.data.model.ProductModel
import com.example.ecommerceapp.data.model.Resource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductsRepository {
    override fun getCategoryProducts(categoryID: String, pageLimit: Int): Flow<List<ProductModel>> {
        return flow {
            val products =
                firestore.collection("products").whereArrayContains("categeories_ids", categoryID)
                    .limit(pageLimit.toLong()).get().await().toObjects(ProductModel::class.java)

            emit(products)
        }
    }


    override fun getSaleProducts(
        countryID: String,
        saleType: String,
        pageLimit: Int
    ): Flow<List<ProductModel>> =
        flow {
            delay(4000)
            Log.d("ProductsRepositoryImpl", "getSaleProducts: $countryID, $saleType")
            val products = firestore.collection("products").whereEqualTo("sale_type", saleType)
                .whereEqualTo("country_id", countryID).orderBy("price").limit(pageLimit.toLong())
                .get().await().toObjects(ProductModel::class.java)
            emit(products)
        }


    override suspend fun getAllProductsPaging(
        countryID: String,
        pageLimit: Long,
        lastDocument: DocumentSnapshot?
    ): Flow<Resource<QuerySnapshot>> {
        TODO("Not yet implemented")
    }
}