package com.example.docappincompose.core.networking

/**
 * Base exception class for the application.
 * All other exceptions should inherit from this class.
 */
open class AppException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)

/**
 * Exception that indicates a network-related error.
 * This could be triggered by network timeouts or connectivity issues.
 */
class NetworkException(
    message: String? = null,
    cause: Throwable? = null
) : AppException(message, cause)

/**
 * Exception that indicates a server-related error.
 * This might occur due to issues like 500 Internal Server Error.
 */
class ServerException(
    message: String? = null,
    cause: Throwable? = null
) : AppException(message, cause)

/**
 * Exception that indicates a client-related error.
 * This might be thrown for errors like 400 Bad Request.
 */
class ClientException(
    message: String? = null,
    cause: Throwable? = null
) : AppException(message, cause)

/**
 * Exception for unknown errors that do not fall into the other categories.
 */
class UnknownException(
    message: String? = null,
    cause: Throwable? = null
) : AppException(message, cause)