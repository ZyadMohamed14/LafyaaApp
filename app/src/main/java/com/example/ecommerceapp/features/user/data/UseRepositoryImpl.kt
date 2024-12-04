package com.example.ecommerceapp.features.user.data


import android.util.Log
import com.example.docappincompose.core.networking.ErrorResponse
import com.example.docappincompose.core.networking.mapErrorToException
import com.example.ecommerceapp.core.local.SharedPreferencesHelper
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.user.domain.UserDetailsModel
import com.example.ecommerceapp.features.user.domain.UseRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UseRepositoryImpl @Inject constructor(
    private val apiService: UserApiServices,
    private val userDao: UserDao,
   private val sharedPreferencesHelper: SharedPreferencesHelper
) : UseRepository {
    override suspend fun getUserDetails(userId: String): Flow<Resource<UserDetailsModel?>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getUserDetails(userId)
            if (response.isSuccessful) {

                emit(Resource.Success(response.body()))
            } else {

                val errorResponse: ErrorResponse? = response.errorBody()?.string()?.let {
                    // Parse the error body into ErrorResponse
                    Gson().fromJson(it, ErrorResponse::class.java)
                }
                emit(
                    Resource.Error(
                        errorResponse = errorResponse
                    )
                )
            }
        } catch (error: Exception) {

            val exception = mapErrorToException(error)
            emit(
                Resource.Error(
                    exception = exception,
                )
            )
        }
    }

    override suspend fun logoutUser(): Flow<Resource<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserDetails(userRequest: UpdateUserRequest): Flow<Resource<Boolean>> = flow {

        try {
            emit(Resource.Loading())

            val response = apiService.updateUserDetails(userRequest.user_id,userRequest)

            if (response.isSuccessful) {
                Log.e("updateUserDetailssssss", response.body().toString())
                if (response.body() != null){
                  //  sharedPreferencesHelper.saveUserDetails(response.body()!!)
                     userDao.insertOrUpdateUser(response.body()!!.data)
                    emit(Resource.Success(true))

                }else{
                    emit(Resource.Success(false))
                }

            }else {

                val errorResponse: ErrorResponse? = response.errorBody()?.string()?.let {
                    // Parse the error body into ErrorResponse
                    Gson().fromJson(it, ErrorResponse::class.java)


                }
                Log.e("userDetailsModel","errorResponse!!.message"+ errorResponse!!.message)
                emit(
                    Resource.Error(
                        errorResponse = errorResponse
                    )
                )
            }
        } catch (error: Exception) {
            Log.e("esdsdsrror", error.message.toString())

            val exception = mapErrorToException(error)
            emit(
                Resource.Error(
                    exception = exception,
                )
            )
        }

    }

    override suspend fun getUserFromLocal(id: String): Flow<UserDetailsModel?>  = flow{
        emit(userDao.getUserById(id))
    }


}