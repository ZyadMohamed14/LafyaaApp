package com.example.ecommerceapp.features.auth.forgotpassword

import com.example.ecommerceapp.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ForgetPasswordRepository {
   fun forgetPassword(email: String): Flow<Resource<Boolean>>

}