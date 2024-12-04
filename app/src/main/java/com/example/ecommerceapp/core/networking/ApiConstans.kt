package com.example.docappincompose.core.api

object ApiConstants {

  const val BASE_URL = "https://us-central1-onsale-7f4cb.cloudfunctions.net/"
    const val PREFIX = "https://ecomerceincompose.page.link"
  const val PaymentBaseURL = "https://api.stripe.com/v1/payment_intents"

    const val apiKey = "Bearer sk_test_51PwrMJF8IpAJ1lBRqN5vzYKJvsX98PmBHPgQAIGnTxZOwGPVpBWgXPk0DuCCfRhBdo4GWbaxDBtnbAzS3WtL194r00prVtLxu7"

    const val InegrationId= "4830712"
    const val PayMobApikey = "ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmpiR0Z6Y3lJNklrMWxjbU5vWVc1MElpd2ljSEp2Wm1sc1pWOXdheUk2T1RrME9Ea3lMQ0p1WVcxbElqb2lhVzVwZEdsaGJDSjkuMEMwNkwwcW5PV2IwcUJtZE5UcC1XNThuSFNSZUdNd0FIVHp4MEU0ZHZOV2l1eXNGQW1wcG4wcWN4TkpZZEM5Tlk0QUl4bFo5V2pmcHdHWXRvSGR6Y1E=="
   const val IFRamLink ="https://accept.paymob.com/api/acceptance/iframes/867693?payment_token={payment_key_obtained_previously}"
}

object ApiErrors {
    const val BAD_REQUEST_ERROR = "badRequestError"
    const val NO_CONTENT = "noContent"
    const val FORBIDDEN_ERROR = "forbiddenError"
    const val UNAUTHORIZED_ERROR = "unauthorizedError"
    const val NOT_FOUND_ERROR = "notFoundError"
    const val CONFLICT_ERROR = "conflictError"
    const val INTERNAL_SERVER_ERROR = "internalServerError"
    const val UNKNOWN_ERROR = "unknownError"
    const val TIMEOUT_ERROR = "timeoutError"
    const val DEFAULT_ERROR = "defaultError"
    const val CACHE_ERROR = "cacheError"
    const val NO_INTERNET_ERROR = "noInternetError"
    const val NETWORK_ERROR = "Network Error Please Check Your Internet Connection"
    const val LOADING_MESSAGE = "loading_message"
    const val RETRY_AGAIN_MESSAGE = "retry_again_message"
    const val OK = "Ok"
}
