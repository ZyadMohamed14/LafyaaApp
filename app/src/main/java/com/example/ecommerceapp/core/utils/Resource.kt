package com.example.ecommerceapp.core.utils

import com.example.docappincompose.core.networking.ErrorResponse

sealed class Resource<T>(
    val data: T? = null, val exception: Exception? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Idle<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(exception: Exception?=null , val errorResponse: ErrorResponse? = null) : Resource<T>(exception= exception)

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Loading -> "Loading[data=$data]"
            is Error -> "Error[exception=$exception, data=$data]"
            is Idle -> "Idle[data=$data]"
        }
    }
}