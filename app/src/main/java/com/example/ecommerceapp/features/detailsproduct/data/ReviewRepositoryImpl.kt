package com.example.ecommerceapp.features.detailsproduct.data

import android.net.Uri
import com.example.docappincompose.core.networking.ErrorResponse
import com.example.docappincompose.core.networking.mapErrorToException
import com.example.ecommerceapp.core.utils.Resource
import com.example.ecommerceapp.features.detailsproduct.domain.Review


import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val apiService: ReviewApiService
) : ReviewRepository {

    // This method uploads a list of image URIs and returns their download URLs

    private suspend fun uploadImagesAsUrls(imagePaths: List<String>): List<String> {
        val uploadedImageUrls = mutableListOf<String>()
        try {


            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference

            for (imagePath in imagePaths) {
                val uri = Uri.parse(imagePath) // Convert the string path to a Uri

                // Upload to the specific folder 'usersimagesreviews'
                val imageRef = storageRef.child("usersimagesreviews/${uri.lastPathSegment}")

                // Upload the image
                val uploadTask = imageRef.putFile(uri).await() // Awaiting the upload task

                // Check if the upload was successful
                if (uploadTask.task.isSuccessful) {
                    // If successful, get the download URL
                    val downloadUrl = imageRef.downloadUrl.await().toString()
                    uploadedImageUrls.add(downloadUrl)
                } else {
                    // Return empty list on failure for any upload

                    return emptyList()
                }
            }
            return uploadedImageUrls // Return the list of successfully uploaded image URLs
        } catch (e: Exception) {
            // Handle any exception and log the error

            return emptyList() // Return an empty list to indicate failure
        }

    }

    override fun addReview(review: Review): Flow<Resource<com.example.ecommerceapp.features.detailsproduct.data.AddReviewResponse?>> = flow {

        try {
            emit(Resource.Loading())
            if (review.images.isNotEmpty()) {
                val imageUrls = uploadImagesAsUrls(review.images)
                if (imageUrls.isNotEmpty()) {
                    review.images.clear()
                    review.images.addAll(imageUrls)
                } else {
                    emit(
                        Resource.Error(
                            exception = Exception("Failed to upload images")
                        )
                    )
                }

            }
            val response = apiService.addReview(review)
            if (response.isSuccessful) {

                emit(Resource.Success(response.body()))
            } else {

                val errorResponse: ErrorResponse? = response.errorBody()?.string()?.let {
                    // Parse the error body into ErrorResponse
                    Gson().fromJson(it, ErrorResponse::class.java)
                }
                emit(
                    Resource.Error(
                        errorResponse = errorResponse
                    )
                )
            }
        } catch (error: Exception) {

            val exception = mapErrorToException(error)
            emit(
                Resource.Error(
                    exception = exception,
                )
            )
        }
    }

    override fun getReviewsById(productId: String): Flow<Resource<List<Review>?>> = flow {

        try {
            emit(Resource.Loading())
            val response = apiService.getReviewById(productId)
            if (response.isSuccessful) {

                val reviews = response.body()?.data
                var reviewsList = mutableListOf<Review>()
                if (reviews != null) {
                    reviewsList.addAll(reviews)
                }
//                repeat(20) { i ->
//                    // Modify the rating or other fields as needed for each duplicate
//                    val newReviews = reviews?.map { review ->
//                        review.copy(rating = (1 + i % 5).toFloat()) // Cycle ratings between 1 and 5
//                    }
//                    if (newReviews != null) {
//                        reviewsList.addAll(newReviews)
//                    }
//                }
                //  emit(Resource.Success(response.body()?.data))
                emit(Resource.Success(reviews))
            } else {

                val errorResponse: ErrorResponse? = response.errorBody()?.string()?.let {
                    // Parse the error body into ErrorResponse
                    Gson().fromJson(it, ErrorResponse::class.java)
                }
                emit(
                    Resource.Error(
                        errorResponse = errorResponse
                    )
                )
            }
        } catch (error: Exception) {

            val exception = mapErrorToException(error)
            emit(
                Resource.Error(
                    exception = exception,
                )
            )
        }
    }

}