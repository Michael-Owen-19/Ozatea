package com.ozatea.core.response

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
) {
    companion object {
        fun <T> success(data: T? = null, message: String = "Success"): ApiResponse<T> =
            ApiResponse(success = true, message = message, data = data)

        fun <T> failure(message: String): ApiResponse<T> =
            ApiResponse(success = false, message = message, data = null)
    }
}
