package com.example.ecommerceapp.features.dashboard.home.data.sales

import android.util.Log
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.home.domain.model.toUIModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SalesAdsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : SalesAdsRepository {
    val TAG = "SalesAdsRepositoryImpl"
    override  fun getSalesAds() = flow {
        try {
            emit(Resource.Loading())
            delay(8000)
            val salesAds =
                firestore.collection("sales_ads")
                    .get().await().toObjects(SalesAdModel::class.java)
            Log.d(TAG, "getSalesAds: ${salesAds.get(0).id}")

            emit(Resource.Success(salesAds.map { it.toUIModel() }))
        } catch (e: Exception) {
            Log.d(TAG, "getSalesAds: ${e.message}")
            emit(Resource.Error(e))
        }
    }


}