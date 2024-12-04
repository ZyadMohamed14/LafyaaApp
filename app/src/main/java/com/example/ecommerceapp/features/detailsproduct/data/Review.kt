package com.example.ecommerceapp.features.detailsproduct.data

import com.example.ecommerceapp.features.detailsproduct.domain.Review
import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    val code: Int,
    val message: String,
    @SerializedName("data") val data: List<Review>

)
data class AddReviewResponse(
    val code: Int,
    val message: String,
    @SerializedName("data") val data: Review

)