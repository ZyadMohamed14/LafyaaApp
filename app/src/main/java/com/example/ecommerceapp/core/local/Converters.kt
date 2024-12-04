package com.example.ecommerceapp.core.local

import androidx.room.TypeConverter
import com.example.ecommerceapp.features.dashboard.home.data.products.Size
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>?): String? = Gson().toJson(value)

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        return try {
            if (value.isNullOrBlank()) emptyList() else Gson().fromJson(value, object : TypeToken<List<String>>() {}.type)
        } catch (e: Exception) {
            // Handle exception (optional, e.g., logging)
            emptyList() // Return an empty list, or handle as desired
        }
    }
    @TypeConverter
    fun fromMap(value: Map<String, Size>?): String? = Gson().toJson(value)

    @TypeConverter
    fun toMap(value: String?): Map<String, Size> =
        if (value == null) emptyMap() else Gson().fromJson(value, object : TypeToken<Map<String, Size>>() {}.type)

    @TypeConverter
    fun fromTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.seconds
    }

    @TypeConverter
    fun toTimestamp(seconds: Long?): Timestamp? {
        return if (seconds == null) null else Timestamp(seconds, 0)
    }
}