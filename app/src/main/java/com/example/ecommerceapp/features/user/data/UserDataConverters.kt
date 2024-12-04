package com.example.ecommerceapp.features.user.data

import androidx.room.TypeConverter
import com.google.firebase.Timestamp

class UserDetailsTypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it / 1000, 0) }
    }

    @TypeConverter
    fun toTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.seconds?.times(1000)
    }

    @TypeConverter
    fun fromReviewsString(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    @TypeConverter
    fun toReviewsString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}
