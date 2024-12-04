package com.example.ecommerceapp.features.detailsproduct.data


import com.example.ecommerceapp.features.detailsproduct.domain.Review
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface ReviewApiService {
    @Headers("Content-Type: application/json")
    @POST("addReview") // This is your Cloud Function endpoint
    suspend fun addReview(@Body review: Review): Response<com.example.ecommerceapp.features.detailsproduct.data.AddReviewResponse?>
    @GET("getReviewsByProductId")
    suspend fun getReviewById(@Query("product_id") productId: String): Response<com.example.ecommerceapp.features.detailsproduct.data.ReviewResponse?>

}
