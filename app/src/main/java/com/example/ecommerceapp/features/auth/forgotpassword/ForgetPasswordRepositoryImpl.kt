package com.example.ecommerceapp.features.auth.forgotpassword

import com.example.docappincompose.core.networking.mapErrorToException
import com.example.ecommerceapp.core.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ForgetPasswordRepositoryImpl @Inject constructor(
    private val auth:FirebaseAuth
) : ForgetPasswordRepository {
    override fun forgetPassword(email: String): Flow<Resource<Boolean>> =
        flow{
            try{
                emit(Resource.Loading())
                val response =auth.sendPasswordResetEmail(email).await()

                // Send password reset email
                auth.sendPasswordResetEmail(email).await()

                // Emit success if the email was sent successfully
                emit(Resource.Success(true))
            }
            catch (e:Exception){
                val exception = mapErrorToException(e)
                emit(
                    Resource.Error(
                        exception = exception,
                    )
                )
            }
        }
}