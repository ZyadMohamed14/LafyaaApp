package com.example.ecommerceapp.features.dashboard.home.data.products

import android.util.Log
import com.example.docappincompose.core.networking.ErrorResponse
import com.example.docappincompose.core.networking.mapErrorToException

import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.core.utils.capitalizeWords
import com.example.ecommerceapp.features.dashboard.home.domain.ProductsRepository
import com.example.ecommerceapp.features.detailsproduct.domain.ProductOrder
import com.example.ecommerceapp.features.detailsproduct.domain.ProductUIModel
import com.example.ecommerceapp.features.detailsproduct.domain.toProductUIModel

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val productDao: ProductDao,
    private val apiService: ProductsApiServices
) : ProductsRepository {
    override suspend fun getProducts(pageLimit: Int, lastVisibleId: String?):  Flow<Resource<ProductsResponse>>  = flow{

        try{
            emit(Resource.Loading())
            val response = apiService.getAllProducts(pageLimit, lastVisibleId)
            if (response.isSuccessful && response.body() != null) {

               emit(Resource.Success(response.body()!!))
            } else {
                Log.d("benz", "getProducts: errorResponse")
                // Parse error response
                val errorResponse: ErrorResponse? = response.errorBody()?.string()?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }
                emit(Resource.Error(errorResponse = errorResponse))
            }
        }
        catch (e:Exception){
            Log.d("benz", "getProducts: Exception ${e.message}")
            val exception = mapErrorToException(e)
            emit(Resource.Error(exception = exception))
        }

    }

    override fun getCategoryProducts(categoryID: String, pageLimit: Int): Flow<List<ProductModel>> {
        return flow {
            val products =
                firestore.collection("products").whereArrayContains("categeories_ids", categoryID)
                    .limit(pageLimit.toLong()).get().await().toObjects(ProductModel::class.java)

            emit(products)
        }
    }


    override fun getSaleProducts(
        saleType: String,
        pageLimit: Int
    ): Flow<Resource<List<ProductUIModel?>>> = flow {
        try {

            emit(Resource.Loading())
         //   delay(8000)
            val response = apiService.getSaleProducts(saleType, pageLimit)

            if (response.isSuccessful) {
                Log.d("benz", "getSaleProducts: ${response.body()}")
                // Safely unwrap the response body and ensure it is not null
                val responseBody = response.body() // This should match the Response class
                if (responseBody != null) {
                    // Extract the products from the response object
                    val products = responseBody.data

                    emit(Resource.Success(products.map { it.toProductUIModel() }))
                } else {
                    emit(Resource.Error(Exception("Response body is null")))
                }
            } else {
                Log.d("benz", "getSaleProducts: errorResponse")
                // Parse error response
                val errorResponse: ErrorResponse? = response.errorBody()?.string()?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }
                emit(Resource.Error(errorResponse = errorResponse))
            }
        } catch (error: Exception) {
            Log.d("benz", "getSaleProducts: Exception ${error.message}")
            val exception = mapErrorToException(error)
            emit(Resource.Error(exception = exception))
        }
    }




    override suspend fun searchProducts(query: String, pageLimit: Int): Flow<Resource<List<ProductUIModel?>>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.searchProducts(query.capitalizeWords()) // Assuming this returns a Response object

            if (response.isSuccessful) {
                // Safely unwrap the response body and ensure it is not null
                val responseBody = response.body() // This should match the Response class
                if (responseBody != null) {
                    // Extract the products from the response object
                    val products = responseBody.data

                    emit(Resource.Success(products.map { it.toProductUIModel() }))
                } else {
                    emit(Resource.Error(Exception("Response body is null")))
                }
            } else {
                // Parse error response
                val errorResponse: ErrorResponse? = response.errorBody()?.string()?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }
                emit(Resource.Error(errorResponse = errorResponse))
            }
        } catch (error: Exception) {
            val exception = mapErrorToException(error)
            emit(Resource.Error(exception = exception))
        }
    }

    override suspend fun getProductByID(productID: String): Flow<Resource<ProductUIModel?>> = flow{
        try {
            emit(Resource.Loading())
            val response = apiService.getProductById(productID)
            if(response.isSuccessful && response.body()!=null){
                Log.d("benzsdsdsdsd", "isSuccessful getProducts: ${response.body()!!.data}")
                emit(Resource.Success(response.body()!!.data.toProductUIModel()))
            }else{
                // Parse error response
                val errorResponse: ErrorResponse? = response.errorBody()?.string()?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }
                Log.d("benzsdsdsdsd", "${errorResponse!!.message}")
                emit(Resource.Success(null))
            }
        }
        catch (e:Exception){
            Log.d("benzsdsdsdsd", "${e.message}")
            val exception = mapErrorToException(e)
            emit(Resource.Error(exception = exception))
        }

    }

    override suspend fun saveProduct(productOrder: ProductOrder): Flow<Boolean> = flow {
        try {
            productDao.insertProduct(productOrder)
            emit(true)  // Emit true if insertion is successful
        } catch (e: Exception) {

            emit(false)  // Emit false if there’s an exception
        }
    }

    override suspend fun deleteProduct(productOrder: ProductOrder): Flow<Boolean> = flow {
        try {
            productDao.deleteProduct(productOrder)
            emit(true)  // Emit true if insertion is successful
        } catch (e: Exception) {

            emit(false)  // Emit false if there’s an exception
        }
    }

    override suspend fun getSavedProducts(): Flow<List<ProductOrder>> =flow {
       try {
           val products = productDao.getAllProducts()
           Log.d("benz", "getSavedProducts: $products")
           emit(products)
       }catch (e:Exception){
           emit(emptyList())
       }
    }

}