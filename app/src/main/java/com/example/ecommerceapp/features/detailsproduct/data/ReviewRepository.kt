package com.example.ecommerceapp.features.detailsproduct.data


import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.detailsproduct.domain.Review

import kotlinx.coroutines.flow.Flow

interface ReviewRepository {

    fun addReview (review: Review) : Flow<Resource<com.example.ecommerceapp.features.detailsproduct.data.AddReviewResponse?>>

    fun getReviewsById(productId:String) :Flow<Resource<List<Review>?>>
}