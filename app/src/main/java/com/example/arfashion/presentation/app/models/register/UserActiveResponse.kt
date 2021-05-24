package com.example.arfashion.presentation.app.models.active

import com.google.gson.annotations.SerializedName

data class UserActiveResponse(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("status_code")
        val status_code: Int
) {
    val statusCode: StatusCode
        get() =
            when (status_code) {
                0 -> StatusCode.HAD_BEEN_ACTIVATED
                1 -> StatusCode.SUCCESS
                2 -> StatusCode.INCORRECT
                else -> StatusCode.MISSING_CREDENTIALS
            }
}

enum class StatusCode {
    HAD_BEEN_ACTIVATED,
    SUCCESS,
    INCORRECT,
    MISSING_CREDENTIALS
}

