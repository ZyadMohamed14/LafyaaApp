package com.example.ecommerceapp.data.datasource.repository.auth

import com.example.ecommerceapp.model.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.tasks.await


class FirebaseAuthRepositoryImpl(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) : FirebaseAuthRepository {
    override suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            // suspends the coroutine until the task is complete
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.let { user ->
                emit(Resource.Success(user.uid)) // Emit the result
            } ?: run {
                emit(Resource.Error(Exception("User not found")))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Flow<Resource<String>> =
        flow {
            try {
                emit(Resource.Loading())
                val credential = GoogleAuthProvider.getCredential(idToken,null)
                val authResult = auth.signInWithCredential(credential).await()
                authResult?.user?.let { user  ->
                    emit(Resource.Success(user.uid))
                }?:run {
                    emit(Resource.Error(Exception("User Not Found")))
                }
            }catch (e:Exception){
                emit(Resource.Error(e))
            }
        }



    override suspend fun loginWithFacebook(token: String): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}