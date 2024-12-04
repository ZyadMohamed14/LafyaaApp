package com.example.ecommerceapp.features.payment

import com.google.gson.annotations.SerializedName

data class PaymentResponse(@SerializedName("client_secret")val clientSecret: String)