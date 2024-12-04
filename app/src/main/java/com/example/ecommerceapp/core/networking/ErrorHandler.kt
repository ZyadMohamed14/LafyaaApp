package com.example.docappincompose.core.networking

import com.example.docappincompose.core.api.ApiErrors
import retrofit2.HttpException
import java.io.IOException
data class ErrorResponse(
    val message: String,
    val code: Int
)


 fun mapErrorToException(error: Throwable): AppException {
    return when (error) {
        is HttpException -> when (error.code()) {
            400 -> ServerException("${ApiErrors.BAD_REQUEST_ERROR}: ${error.code()}", error)
            401 -> ServerException("${ApiErrors.UNAUTHORIZED_ERROR}: ${error.code()}", error)
            403 -> ServerException("${ApiErrors.FORBIDDEN_ERROR}: ${error.code()}", error)
            404 -> ServerException("${ApiErrors.NOT_FOUND_ERROR}: ${error.code()}", error)
            422 -> ServerException("${ApiErrors.NO_CONTENT}: ${error.code()}", error)
            500 -> ServerException("${ApiErrors.INTERNAL_SERVER_ERROR}: ${error.code()}", error)
            else -> UnknownException("${ApiErrors.UNKNOWN_ERROR}: ${error.code()}", error)
        }

        is IOException -> NetworkException(ApiErrors.NETWORK_ERROR, error)
        else -> UnknownException(ApiErrors.UNKNOWN_ERROR, error)
    }
}
 fun mapCodeToException(code: Int): AppException {
    return when (code) {
        400 -> ServerException(ApiErrors.BAD_REQUEST_ERROR)
        401 -> ServerException(ApiErrors.UNAUTHORIZED_ERROR)
        403 -> ServerException(ApiErrors.FORBIDDEN_ERROR)
        404 -> ServerException(ApiErrors.NOT_FOUND_ERROR)
        422 -> ServerException(ApiErrors.NO_CONTENT)
        500 -> ServerException(ApiErrors.INTERNAL_SERVER_ERROR)
        else -> UnknownException("${ApiErrors.UNKNOWN_ERROR}: $code")
    }
}
 fun extractErrorCode(error: Throwable): String {
    return when (error) {
        is HttpException -> "#ER${error.code()}"
        else -> error.cause?.message ?: ApiErrors.UNKNOWN_ERROR
    }
}