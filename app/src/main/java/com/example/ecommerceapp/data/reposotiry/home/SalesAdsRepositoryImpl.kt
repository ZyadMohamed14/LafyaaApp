package com.example.ecommerceapp.data.reposotiry.home

import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.model.sale_ads.SalesAdModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SalesAdsRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
) : SalesAdsRepository {
    override fun getSalesAds(): Flow<Resource<List<SalesAdModel>>>  = flow {
        try {
            emit(Resource.Loading())
            val salesAds = fireStore.collection("sales_ads").get().await().toObjects(SalesAdModel::class.java)
            //emit(Resource.Success(salesAds.map { it.toUIModel() }))
        }catch (e: Exception) {
            emit(Resource.Error(e))
        }

    }


}