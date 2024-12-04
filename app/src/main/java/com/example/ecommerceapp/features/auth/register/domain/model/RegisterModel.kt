package com.example.ecommerceapp.features.auth.register.domain.model

import com.example.ecommerceapp.features.user.domain.UserDetailsModel

data class RegisterRequestModel(
    val provider: String,          // "EMAIL", "GOOGLE", or "FACEBOOK"
    val email: String? = null,      // Required for email/password registration
    val password: String? = null,   // Required for email/password registration
    val fullName: String? = null,   // Required for email registration
    val idToken: String? = null,    // Required for Google registration
    val accessToken: String? = null,// Required for Facebook registration
    val image: String? = null       // Optional user profile image URL
)

data class RegisterResponseModel (
    val message: String,
    val code: Int,
    val data: UserDetailsModel
)
