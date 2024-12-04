package com.example.ecommerceapp.features.auth.register.data

import android.util.Log
import com.example.docappincompose.core.networking.ErrorResponse
import com.example.docappincompose.core.networking.mapErrorToException
import com.example.ecommerceapp.core.local.SharedPreferencesHelper
import com.example.ecommerceapp.core.utils.CrashlyticsUtils
import com.example.ecommerceapp.core.utils.LoginException
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.auth.login.domain.AppAuthProvider

import com.example.ecommerceapp.features.auth.register.domain.model.RegisterRequestModel
import com.example.ecommerceapp.features.auth.register.domain.repo.RegisterRepository
import com.example.ecommerceapp.features.user.domain.UserDetailsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val apiService: RegisterApiServices,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val sharedPreferences: SharedPreferencesHelper
) : RegisterRepository {
    override suspend fun registerUser(registerRequestModel: RegisterRequestModel): Flow<Resource<UserDetailsModel?>> =
        flow{
       try{
           emit(Resource.Loading())
           val response = apiService.registerUser(registerRequestModel)
           if(response.isSuccessful){
               Log.d("RegisterRepositoryImpl", "isSuccessful: ${response.body()}")
               val  userDetails = response.body()?.data
               emit(Resource.Success(userDetails))
           }else{

               val errorResponse: ErrorResponse? = response.errorBody()?.string()?.let {
                   // Parse the error body into ErrorResponse
                   Gson().fromJson(it, ErrorResponse::class.java)
               }
               Log.d("RegisterRepositoryImpl", "errorResponse:${errorResponse!!.message}")
               emit(
                   Resource.Error(
                       errorResponse = errorResponse
                   )
               )
           }
       }
       catch (e:Exception){
           Log.d("RegisterRepositoryImpl", "Exception${e.message}:")
           val exception = mapErrorToException(e)
           emit(
               Resource.Error(
                   exception = exception,
               )
           )
       }
    }
    override suspend fun registerWithGoogle(idToken: String): Flow<Resource<UserDetailsModel?>> {
        return flow {
            try {
                emit(Resource.Loading())
                // Perform Firebase auth sign-in request
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(credential).await()

                Log.d("RegisterRepositoryImpl", "authResult: $authResult")

                val userId = authResult.user?.uid

                if (userId == null) {
                    val msg = "Sign up UserID not found"
                    logAuthIssueToCrashlytics(msg, AppAuthProvider.GOOGLE.name)
                    emit(Resource.Error(Exception(msg)))
                    return@flow
                }

                // Check if user already exists in Firestore
                val userDocument = firestore.collection("users").document(userId).get().await()
                if (userDocument.exists()) {
                    val msg = "User already exists"
                    emit(Resource.Error(Exception(msg)))
                    return@flow
                }


                  val email = sharedPreferences.getUserEmail()
                // Create user details object
                val userDetails = UserDetailsModel(
                    id = userId,
                    name = authResult.user?.displayName ?: "",
                    email = email ?: "",
                )

                // Save user details to Firestore
                firestore.collection("users").document(userId).set(userDetails).await()
                emit(Resource.Success(userDetails))
            } catch (e: Exception) {
                logAuthIssueToCrashlytics(
                    e.message ?: "Unknown error from exception = ${e::class.java}",
                    AppAuthProvider.GOOGLE.name
                )
                emit(Resource.Error(e)) // Emit error
            }
        }
    }

    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<LoginException>(
            msg,
            CrashlyticsUtils.LOGIN_KEY to msg,
            CrashlyticsUtils.LOGIN_PROVIDER to provider,
        )
    }

}