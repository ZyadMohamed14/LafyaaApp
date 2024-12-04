package com.example.ecommerceapp.features.detailsproduct.domain

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.Date


@Parcelize
@Keep
data class Review(
    @SerializedName("id")
    var reviewId: String?=null,
    @SerializedName ("product_id")
    var productId: String,
    @SerializedName("user_id")
    var userId: String,
    val rating: Float,
    val comment: String,          // Ensuring the comment is nullable and has a default value
    var images: MutableList<String> = mutableListOf(), // Default to an empty list if no images
    @SerializedName("created_at")
    var createdAt: CreatedAt? = null,
    @SerializedName("user")
    var userReview: UserReview?=null
) : Parcelable

@Parcelize
data class CreatedAt(
    @SerializedName("_seconds") val seconds: Long,      // Changed to `seconds` instead of `_seconds` for clarity
    @SerializedName("_nanoseconds") val nanoseconds: Int
) : Parcelable{
    fun toDate(): Date {
        return Date(seconds * 1000 + nanoseconds / 1000000)
    }

    companion object {
        fun fromTimestamp(timestamp: Timestamp): CreatedAt {
            return CreatedAt(timestamp.seconds, timestamp.nanoseconds)
        }
    }
}


@Parcelize
data class UserReview(
    val name: String,
    val image: String? = null // Default value for nullable image
) : Parcelable