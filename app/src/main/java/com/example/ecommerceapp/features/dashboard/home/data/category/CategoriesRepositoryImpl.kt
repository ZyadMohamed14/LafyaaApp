package com.example.ecommerceapp.features.dashboard.home.data.category

import android.util.Log


import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.dashboard.home.domain.model.toUIModel
import com.example.ecommerceapp.features.dashboard.home.domain.model.CategoryUIModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    CategoriesRepository {
    override fun getCategories(): Flow<Resource<List<CategoryUIModel>>> {
        return flow {
            try {
                emit(Resource.Loading())
             //   delay(8000)
                val categories = firestore.collection("categories").get().await()
                    .toObjects(CategoryModel::class.java)
                Log.d(tag, "categories = ${categories}")



                emit(Resource.Success(categories.map { it.toUIModel() }))
            } catch (e: Exception) {

                Log.e(tag, "getCategories: error", e)
                emit(Resource.Error(e))
            }
        }
    }
    companion object{
        val tag = "CategoriesRepository"
    }
}