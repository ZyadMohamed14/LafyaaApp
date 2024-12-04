package com.example.ecommerceapp.features.auth.login.data

import android.util.Log
import com.example.ecommerceapp.core.local.SharedPreferencesHelper
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.user.domain.UserDetailsModel
import com.example.ecommerceapp.core.utils.CrashlyticsUtils
import com.example.ecommerceapp.core.utils.LoginException
import com.example.ecommerceapp.features.auth.login.domain.AppAuthProvider
import com.example.ecommerceapp.features.auth.login.domain.LoginRepository
import com.example.ecommerceapp.features.user.data.UserDao
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao,
    private val preferencesHelper: SharedPreferencesHelper
): LoginRepository {
    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<UserDetailsModel>> = login(AppAuthProvider.EMAIL) {
        auth.signInWithEmailAndPassword(email, password).await()
    }




    override suspend fun loginWithGoogle(idToken: String) = login(AppAuthProvider.GOOGLE) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }

    // Example usage for Facebook login
    override suspend fun loginWithFacebook(token: String) = login(AppAuthProvider.FACEBOOK) {
        val credential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential).await()
    }
    private suspend fun login(
        provider: AppAuthProvider,
        signInRequest: suspend () -> AuthResult,
    ): Flow<Resource<UserDetailsModel>> = flow {
        try {
            emit(Resource.Loading())

            // perform firebase auth sign in request
            val authResult = signInRequest()
            val userId = authResult.user?.uid

            if (userId == null) {
                val msg = "Sign in UserID not found"
                logAuthIssueToCrashlytics(msg, provider.name)

                emit(Resource.Error(Exception(msg)))
                return@flow
            }

//            if (authResult.user?.isEmailVerified == false) {
//                authResult.user?.sendEmailVerification()?.await()
//                val msg = "Email not verified, verification email sent to user"
//                logAuthIssueToCrashlytics(msg, provider.name)
//                emit(Resource.Error(Exception(msg)))
//                return@flow
//            }

            // get user details from firestore
            val userDoc = firestore.collection("users").document(userId).get().await()
            if (!userDoc.exists()) {
                val msg = "Logged in user not found in firestore"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
                return@flow
            }

            // map user details to UserDetailsModel
            val userDetails = userDoc.toObject(UserDetailsModel::class.java)
            userDetails?.let {
                preferencesHelper.saveUserId(userDetails.id!!)
                userDao.insertOrUpdateUser(userDetails)
                emit(Resource.Success(userDetails))
            } ?: run {
                val msg = "Error mapping user details to UserDetailsModel, user id = $userId"
                logAuthIssueToCrashlytics(msg, provider.name)
                emit(Resource.Error(Exception(msg)))
            }
        } catch (e: Exception) {
            Log.e("LoginRepositoryImpl", "Error during login ${e.message.toString()}")
            logAuthIssueToCrashlytics(
                e.message ?: "Unknown error from exception = ${e::class.java}", provider.name
            )
            emit(Resource.Error(e)) // Emit error
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